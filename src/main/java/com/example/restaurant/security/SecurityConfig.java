package com.example.restaurant.security;

import com.example.restaurant.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;

  public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .antMatchers("/api/restaurants/get-all", "/api/restaurants/get/**").permitAll()
            .antMatchers("/api/restaurants/add-new", "/api/restaurants/update/**", "/restaurant/edit/**")
            .hasAnyRole("SENIOR", "ADMIN")
            .antMatchers("/api/restaurants/delete/**").hasRole("ADMIN")
            .antMatchers("/auth/register", "/auth/login", "/login", "/register", "/css/**", "/js/**", "/public/**",
                "/auth/**")
            .permitAll()
            .anyRequest().permitAll())
        .formLogin(form -> form
            .usernameParameter("username")
            .passwordParameter("password")
            .loginPage("/auth/login")
            .failureUrl("/auth/login?failed")
            .defaultSuccessUrl("/home", true)
            .failureHandler(authenticationFailureHandler())
            .permitAll())
        .logout(logout -> logout
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll())
        .sessionManagement(session -> session
            .maximumSessions(1000)
            .expiredUrl("/auth/login?expired"));

    return http.build();
  }

  @Bean
  public NoOpPasswordEncoder passwordEncoder() {
    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return (request, response, exception) -> {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write("Geçersiz kullanıcı adı veya şifre\n");
      response.getWriter().write(exception.getMessage());
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return auth.build();
  }
}
