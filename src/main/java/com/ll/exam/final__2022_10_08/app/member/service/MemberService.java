package com.ll.exam.final__2022_10_08.app.member.service;

import com.ll.exam.final__2022_10_08.app.AppConfig;
import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;
import com.ll.exam.final__2022_10_08.app.cash.service.CashService;
import com.ll.exam.final__2022_10_08.app.email.service.EmailService;
import com.ll.exam.final__2022_10_08.app.emailVerification.service.EmailVerificationService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.entity.emum.AuthLevel;
import com.ll.exam.final__2022_10_08.app.member.exception.AlreadyJoinException;
import com.ll.exam.final__2022_10_08.app.member.repository.MemberRepository;
import com.ll.exam.final__2022_10_08.app.security.dto.MemberContext;
import com.ll.exam.final__2022_10_08.app.security.jwt.JwtProvider;
import com.ll.exam.final__2022_10_08.util.Ut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final CashService cashService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Member join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(AuthLevel.NORMAL)
                .build();

        memberRepository.save(member);

        emailVerificationService.send(member)
                .addCallback(
                        sendRsData -> {
                            // 성공시 처리
                        },
                        error -> log.error(error.getMessage())
                );

        return member;
    }


    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public RsData verifyEmail(long id, String verificationCode) {
        RsData verifyVerificationCodeRs = emailVerificationService.verifyVerificationCode(id, verificationCode);

        if (verifyVerificationCodeRs.isSuccess() == false) {
            return verifyVerificationCodeRs;
        }

        Member member = memberRepository.findById(id).get();
        member.setEmailVerified(true);

        return RsData.of("S-1", "이메일인증이 완료되었습니다.");
    }

    public Optional<Member> findByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username, email);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public RsData sendTempPasswordToEmail(Member actor) {
        String title = "[" + AppConfig.getSiteName() + "] 임시 패스워드 발송";
        String tempPassword = Ut.getTempPassword(6);
        String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
        body += "<a href=\"" + AppConfig.getSiteBaseUrl() + "/member/login\" target=\"_blank\">로그인 하러가기</a>";

        RsData sendResultData = emailService.sendEmail(actor.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }

        setTempPassword(actor, tempPassword);

        return RsData.of("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
    }

    @Transactional
    public void setTempPassword(Member actor, String tempPassword) {
        actor.setPassword(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public RsData modifyPassword(Member member, String password, String oldPassword) {
        Optional<Member> opMember = memberRepository.findById(member.getId());

        if (passwordEncoder.matches(oldPassword, opMember.get().getPassword()) == false) {
            return RsData.of("F-1", "기존 비밀번호가 일치하지 않습니다.");
        }

        opMember.get().setPassword(passwordEncoder.encode(password));

        return RsData.of("S-1", "비밀번호가 변경되었습니다.");
    }

    @Transactional
    public RsData beAuthor(Member member, String nickname) {
        Optional<Member> opMember = memberRepository.findByNickname(nickname);

        if (opMember.isPresent()) {
            return RsData.of("F-1", "해당 필명은 이미 사용중입니다.");
        }

        opMember = memberRepository.findById(member.getId());

        opMember.get().setNickname(nickname);
        forceAuthentication(opMember.get());

        return RsData.of("S-1", "해당 필명으로 활동을 시작합니다.");
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Transactional
    public RsData<AddCashRsDataBody> addCash(Member member, long price, BaseEntity relEntity, CashLog.EvenType eventType) {
        CashLog cashLog = cashService.addCash(member, price, relEntity.getModelName(), relEntity.getId(), eventType);

        long newRestCash = getRestCash(member) + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-1",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public long getRestCash(Member member) {
        return memberRepository.findById(member.getId()).get().getRestCash();
    }

    @Transactional
    public String genAccessToken(Member member) {
        String accessToken = member.getAccessToken();

        if (StringUtils.hasLength(accessToken) == false) {
            accessToken = jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60L * 60 * 24 * 365 * 100);
            member.setAccessToken(accessToken);
        }

        return accessToken;
    }

    public boolean verifyWithWhiteList(Member member, String token) {
        return member.getAccessToken().equals(token);
    }

    @Cacheable("member")
    public Map<String, Object> getMemberMapByUsername__cached(String username) {
        Member member = findByUsername(username).orElse(null);

        log.debug("member.toMap() : " + member.toMap());

        return member.toMap();
    }

    public Member getByUsername__cached(String username) {
        MemberService thisObj = (MemberService) AppConfig.getContext().getBean("memberService");
        Map<String, Object> memberMap = thisObj.getMemberMapByUsername__cached(username);

        return Member.fromMap(memberMap);
    }
}
