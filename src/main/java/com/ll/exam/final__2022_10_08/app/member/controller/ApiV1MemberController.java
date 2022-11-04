package com.ll.exam.final__2022_10_08.app.member.controller;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.member.dto.LoginRequest;
import com.ll.exam.final__2022_10_08.app.member.dto.LoginResponse;
import com.ll.exam.final__2022_10_08.app.member.dto.MeResponse;
import com.ll.exam.final__2022_10_08.app.member.dto.MemberDto;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/member", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ApiV1MemberController", description = "로그인 기능과 로그인 된 회원의 정보를 제공 기능을 담당")
public class ApiV1MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;

    @PostMapping("/login")
    @Operation(summary = "로그인, 엑세스 토큰 발급")
    public ResponseEntity<RsData<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Member member = memberService.findByUsername(loginRequest.getUsername()).orElse(null);

        if (member == null) {
            return Ut.sp.responseEntityOf(
                    RsData.of(
                            "F-2",
                            "일치하는 회원이 존재하지 않습니다."
                    )
            );
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()) == false) {
            return Ut.sp.responseEntityOf(
                    RsData.of(
                            "F-3",
                            "비밀번호가 일치하지 않습니다."
                    )
            );
        }

        log.debug("Util.json.toStr(member.getAccessTokenClaims()) : " + Ut.json.toStr(member.getAccessTokenClaims()));

        String accessToken = memberService.genAccessToken(member);

        return Ut.sp.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        new LoginResponse(accessToken)
                )
        );
    }

    @Operation(summary = "로그인된 사용자의 정보", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/me", consumes = ALL_VALUE)
    public ResponseEntity<RsData<MeResponse>> me() {
        if (rq.isLogout()) { // 임시코드, 나중에는 시프링 시큐리티를 이용해서 로그인을 안했다면, 아예 여기로 못 들어오도록
            return Ut.sp.responseEntityOf(
                    RsData.failOf(
                            null
                    )
            );
        }

        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        new MeResponse(MemberDto.of(rq.getMember()))
                )
        );
    }
}
