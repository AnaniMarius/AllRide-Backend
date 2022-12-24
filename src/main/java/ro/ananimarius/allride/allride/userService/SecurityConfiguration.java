package ro.ananimarius.allride.allride.userService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").
                permitAll(); //By default Spring requires OAuth to access URL’s and also block csrf attacks(cross site request forgery). These attacks impact web apps but don’t impact native apps so we don’t need that protection or the OAuth support
        httpSecurity.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //The PasswordEncoder we autowired in the UserSession class is defined explicitly here so the autowire code maps to this
        return new BCryptPasswordEncoder();
    }


}
