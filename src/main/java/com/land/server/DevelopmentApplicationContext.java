/**
 * 
 */
package com.land.server;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/** This context runs DatabaseInitializer on refreshes */
public class DevelopmentApplicationContext implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @SuppressWarnings("serial")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
        // well, let's set up a security context
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public String getName() {
                return "SYSTEM";
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean arg0) throws IllegalArgumentException {}
        });
        databaseInitializer.init();
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
