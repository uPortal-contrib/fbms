package org.jasig.portal.fbms;

import org.apereo.portal.soffit.security.SoffitApiAuthenticationManager;
import org.apereo.portal.soffit.security.SoffitApiPreAuthenticatedProcessingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import static org.apereo.portal.soffit.service.AbstractJwtService.DEFAULT_SIGNATURE_KEY;
import static org.apereo.portal.soffit.service.AbstractJwtService.SIGNATURE_KEY_PROPERTY;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${" + SIGNATURE_KEY_PROPERTY + ":" + DEFAULT_SIGNATURE_KEY + "}")
    private String signatureKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * Provide a SoffitApiPreAuthenticatedProcessingFilter (from uPortal) that is NOT a
         * top-level bean in the Spring Application Context.
         */
        final AbstractPreAuthenticatedProcessingFilter filter =
                new SoffitApiPreAuthenticatedProcessingFilter(signatureKey);
        filter.setAuthenticationManager(authenticationManager());

        http
                .addFilter(filter)
                .authorizeRequests()
                // TODO:  Add role-based access
                .antMatchers(HttpMethod.GET,"/api/**").authenticated()
                .antMatchers(HttpMethod.POST,"/api/**").denyAll()
                .antMatchers(HttpMethod.DELETE,"/api/**").denyAll()
                .antMatchers(HttpMethod.PUT,"/api/**").denyAll()
                .anyRequest().permitAll()
            .and()
                /*
                 * Session fixation protection is provided by uPortal.  Since portlet tech requires
                 * sessionCookiePath=/, we will make the portal unusable if other modules are changing
                 * the sessionId as well.
                 */
                .sessionManagement()
                .sessionFixation().none();

    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new SoffitApiAuthenticationManager();
    }

}
