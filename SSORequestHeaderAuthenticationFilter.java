package com.aws.login;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SSORequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    private HttpSession httpSession;

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    private boolean allowPreAuthenticatedPrincipals = true;

    public SSORequestHeaderAuthenticationFilter() {
        super();
        //TODO Pull this value from a properties file (application.properties, or localstrings.properties)
        //NOTE SM_USER is the default, but you can change it like this (your company may use some other header)
        this.setPrincipalRequestHeader("SM_USER");

    }

    /**
     * This is called when a request is made, the returned object identifies the
     * user and will either be {@literal null} or a String. This method will throw an exception if
     * exceptionIfHeaderMissing is set to true (default) and the required header is missing.
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     */
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String userName= (String) httpSession.getAttribute("userName");
        if (userName == null || userName.trim().equals("")) {
            return userName;
        }

        return userName;
    }

    public boolean isAllowPreAuthenticatedPrincipals() {
        return allowPreAuthenticatedPrincipals;
    }

}
