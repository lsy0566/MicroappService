package com.example.myappapiusers.security;


import com.example.myappapiusers.model.LoginRequestModel;
import com.example.myappapiusers.service.UserService;
import com.example.myappapiusers.shared.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
    AuthenticationManager authenticationManager;

    public AuthenticationFilter(UserService userService,
                                Environment env,
                                AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    // check username=(email) passowrd 사용자 유무 확인 (encryptePassword), 토큰 처리작업
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate( // 데이터 검증 작업
                    new UsernamePasswordAuthenticationToken(    // 검증을 위한 객체
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String email = ((User) authResult.getPrincipal()).getUsername(); // email 값 얻기
        UserDto userDetail = userService.getUserDetailsByEmail(email);

        // generate token with userId(email)
        // toekn expire_date (configuration or application.yml)
        String token = Jwts.builder()
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetail.getUserId());
    }
}
