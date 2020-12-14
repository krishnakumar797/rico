package ro.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Default configuration class for Security
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn({"log", "security"})
@EnableWebSecurity
@AutoConfigurationPackage
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyCustomLoginSuccessHandler successHandler;

    @Autowired
    MyCustomLogoutSuccessHandler logOutHandler;

    @Autowired
    UserLoginService userLoginService;

    /**
     * Configuring security for rest urls ROLE name should be prefixed with ROLE_ in
     * the database column, but for checking roles we need only actual name without
     * the prefix. It will be automatically added by spring security.
     *
     */
    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/person").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/userLogin")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?err=1")
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler(logOutHandler);
    }

    /**
     * Bcrypt Password encoder bean
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Configuring DAOAuthentication provider
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }


    /**
     * DAO Authentication provider creation using Userloginservice and
     * Passwordencoder.
     *
     * @return
     */
    private DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userLoginService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
