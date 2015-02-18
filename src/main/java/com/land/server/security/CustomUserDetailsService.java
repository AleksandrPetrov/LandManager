package com.land.server.security;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.land.server.dao.UserDao;

/**
 * A custom service for retrieving users from a custom datasource, such as a
 * database.
 * <p>
 * This custom service must implement Spring's {@link UserDetailsService}
 */
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    protected static Logger logger = Logger.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        // logger.debug("loadUserByUsername username=" + username);
        UserDetails user = null;
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl(CurrentUser.ROLE_USER));

        com.land.server.domain.User dbUser = userDao.findByLogin(username);
        if (dbUser == null) throw new UsernameNotFoundException("Username Not Found");
        if (dbUser.getAdmin()) authorities.add(new GrantedAuthorityImpl(CurrentUser.ROLE_ADMIN));

        user = new CustomUserDetails(dbUser.getId(), dbUser.getLogin(), dbUser.getPassword(), true, true, true, true, authorities);

        return user;
    }

}