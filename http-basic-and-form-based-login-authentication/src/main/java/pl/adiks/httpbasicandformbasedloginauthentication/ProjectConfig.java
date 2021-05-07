package pl.adiks.httpbasicandformbasedloginauthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    // Setting the HTTP Basic authentication method
    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic(c -> {
            c.realmName("OTHER");
            // Setting the custom AuthenticationEntryPoint
            c.authenticationEntryPoint(new CustomEntryPoint());
        });

        http.authorizeRequests().anyRequest().authenticated();
    }
     */

    //  Changing the authentication method to a form-based login
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .defaultSuccessUrl("/home", true)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .httpBasic();

        http.authorizeRequests().anyRequest().authenticated();
    }
}
