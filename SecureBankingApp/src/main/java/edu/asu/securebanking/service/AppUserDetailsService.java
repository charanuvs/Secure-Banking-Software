package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikranth on 10/17/2015.
 */
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userDAO.getUser(username);

        if (user == null)
            throw new UsernameNotFoundException("Invalid username/password");

        List<GrantedAuthority> auths = buildAuthorities(user.getUserType());
        User secUser = new User(user.getUserId(), user.getPassword(),
                true, true, true, true, auths);

        return secUser;
    }

    private static List<GrantedAuthority> buildAuthorities(String userType) {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        auths.add(new SimpleGrantedAuthority(userType));

        return auths;
    }
}
