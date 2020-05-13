package com.example.myappapiusers.security;


import com.example.myappapiusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private Environment env;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(Environment env, UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/**").hasIpAddress(env.getProperty("gateway.ip"))
                .and()
                .addFilter(getAuthenticationFilter());
//                .antMatchers("/**").hasIpAddress(env.getProperty("172.19.199.161"));
                // access() -> role

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception { //핸들링이 필요함
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(userService, env, authenticationManager());
        authenticationFilter.setAuthenticationManager(
                authenticationManager()
        );

        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
        super.configure(auth);
    }
}