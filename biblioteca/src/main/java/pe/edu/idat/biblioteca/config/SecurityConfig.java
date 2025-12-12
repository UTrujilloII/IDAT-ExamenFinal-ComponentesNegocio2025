package pe.edu.idat.biblioteca.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.idat.biblioteca.security.CustomAccessDeniedHandler;
import pe.edu.idat.biblioteca.security.JwtAuthEntryPoint;
import pe.edu.idat.biblioteca.security.JwtAuthFilter;
import pe.edu.idat.biblioteca.service.impl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/usuario").permitAll()
                        .requestMatchers("/v1/rol/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/v1/libro","/v1/prestamo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/v1/prestamo","/v1/prestamo/{id}","/v1/prestamo/devolucion/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/v1/libro/{id}","/v1/usuario/{id}","/v1/prestamo/usuario/{idUsuario}").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT,"/v1/libro/{id}","/v1/usuario/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/v1/libro/{id}","/v1/usuario/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated())
                        .exceptionHandling(ex->ex
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                        );
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.formLogin(form->form.disable());

        return httpSecurity.build();
    }
}
