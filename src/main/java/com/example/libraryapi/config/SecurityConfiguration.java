package com.example.libraryapi.config;

import com.example.libraryapi.security.CustomUserDetailsService;
import com.example.libraryapi.security.LoginSocialSuccessHandler;
import com.example.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler loginSocialSuccessHandler) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(configurer ->
                        configurer.loginPage("/login").permitAll())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->{
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                            .successHandler(loginSocialSuccessHandler)
                            .loginPage("/login");
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    //@Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService){

        return new CustomUserDetailsService(usuarioService);

//        UserDetails user1 = User.builder()
//                .username("usuario")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("administrador")
//                .password(encoder.encode("321"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);

    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }


}
