package myapps.solutions.wrapper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import myapps.solutions.wrapper.dao.IUserDetailsDAO;

@Component
public class UserService implements UserDetailsService {

    @Autowired
    private IUserDetailsDAO userDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.getByUsername(s);
        return user;
    }
}
