package com.ll.exam.final__2022_10_08.app.security.filter;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.security.dto.MemberContext;
import com.ll.exam.final__2022_10_08.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            // 1차 체크(정보가 변조되지 않았는지 체크)
            if (jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                // 캐시(레디스)를 통해서
                Member member = memberService.getByUsername__cached((String) claims.get("username"));

                // 2차 체크(화이트리스트에 포함되는지)
                if (memberService.verifyWithWhiteList(member, token)) {
                    forceAuthentication(member);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.genAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
