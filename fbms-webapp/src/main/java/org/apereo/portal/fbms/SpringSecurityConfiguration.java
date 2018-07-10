package org.apereo.portal.fbms;

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

/**
 * Spring Security setup designed to accept OAuth OpenID Connect (OIDC) tokens for access to REST
 * APIs.  uPortal 5.1 and above is capable of producing compatible tokens (via the
 * /uPortal/api/v5-1/userinfo URI), but tokens from other services may be used as well.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * String representation of the 'Portal Administrators' group in uPortal.
     */
    private static final String PORTAL_ADMINISTRATORS_AUTHORITY = "Portal Administrators";

    /**
     * Must match the key used by the OIDC token provider to sign the JWT.
     */
    @Value("${" + SIGNATURE_KEY_PROPERTY + ":" + DEFAULT_SIGNATURE_KEY + "}")
    private String signatureKey;

    /**
     * Comma-separated list of authorities (uPortal groups) that have access to create objects across FBMS APIs.
     */
    @Value("${org.apereo.portal.fbms.security.createAuthority:" +  PORTAL_ADMINISTRATORS_AUTHORITY + "}")
    private String createAuthority;

    /**
     * Comma-separated list of authorities (uPortal groups) that have access to update objects across FBMS APIs.
     */
    @Value("${org.apereo.portal.fbms.security.updateAuthority:" +  PORTAL_ADMINISTRATORS_AUTHORITY + "}")
    private String updateAuthority;

    /**
     * Comma-separated list of authorities (uPortal groups) that have access to delete objects across FBMS APIs.
     */
    @Value("${org.apereo.portal.fbms.security.deleteAuthority:" +  PORTAL_ADMINISTRATORS_AUTHORITY + "}")
    private String deleteAuthority;

    /**
     * Comma-separated list of authorities (uPortal groups) that have access to read the submissions of others.
     */
    @Value("${org.apereo.portal.fbms.security.readOthersAuthority:" +  PORTAL_ADMINISTRATORS_AUTHORITY + "}")
    private String readOthersAuthority;

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

                /*
                 * Use the SoffitApiPreAuthenticatedProcessingFilter for identity.
                 */
                .addFilter(filter)
                .authorizeRequests()

                /*
                 * Role-based Security for REST APIs
                 *
                 * Use .hasAuthority() instead of .hasRole() because uPortal group names are not in
                 * the format ROLE_STUDENTS, etc.
                 */

                /*
                 * Authenticated users may read from the /forms enpoints, but only privileged users
                 * may create/update/delete.
                 */
                .antMatchers(HttpMethod.GET,"/api/v1/forms/*").authenticated()
                .antMatchers(HttpMethod.POST,"/api/v1/forms/*").hasAuthority(createAuthority)
                .antMatchers(HttpMethod.PUT,"/api/v1/forms/*").hasAuthority(updateAuthority)
                .antMatchers(HttpMethod.DELETE,"/api/v1/forms/*").hasAuthority(deleteAuthority)

                /*
                 * Privileged users read the submissions of others.
                 */
                .antMatchers(HttpMethod.GET,"/api/v1/submissions/*/users/*").hasAuthority(readOthersAuthority)

                /*
                 * Authenticated users may create & read their own submissions.
                 */
                .antMatchers(HttpMethod.GET,"/api/v1/submissions/*").authenticated()
                .antMatchers(HttpMethod.POST,"/api/v1/submissions/*").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/v1/submissions/*").denyAll()
                .antMatchers(HttpMethod.DELETE,"/api/v1/submissions/*").denyAll()

                /*
                 * Requests to non-API URIs are okay.
                 */
                .anyRequest().denyAll()
            .and()

                /*
                 * CSRF defense is not required because FBMS uses Bearer token AuthN instead of
                 * Basic or cookie-based.
                 * See https://security.stackexchange.com/questions/170388/do-i-need-csrf-token-if-im-using-bearer-jwt.
                 */
                .csrf().disable()

                /*
                 * Session fixation protection is provided by uPortal.  Since portlet tech requires
                 * sessionCookiePath=/, we will make the portal unusable if other modules are changing
                 * the sessionId as well.
                 */
                .sessionManagement().sessionFixation().none();

    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new SoffitApiAuthenticationManager();
    }

}
