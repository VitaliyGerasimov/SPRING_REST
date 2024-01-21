package ru.kata.spring.boot_security.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandle successUserHandle;
    private final UserService userService;

    @Autowired
    @Lazy
    public WebSecurityConfig(SuccessUserHandle successUserHandle, UserService userService) {
        this.successUserHandle = successUserHandle;
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        //тестирования REST
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated();



        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**","/api/**").hasRole("ADMIN")
                .antMatchers("/user","api/authorized_user/").hasAnyRole("ADMIN", "USER")
                .antMatchers("/","/registration", "/login").permitAll()
                .antMatchers("/css/bootstrap.min.css", "/js/bootstrap.bundle.min.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successUserHandle)
                .loginProcessingUrl("/login")
                .usernameParameter("j_login")
                .passwordParameter("j_password")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");

    }

    //настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}