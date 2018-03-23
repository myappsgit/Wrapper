package myapps.solutions.wrapper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import myapps.solutions.wrapper.dao.IUserDetailsDAO;

@Component
public class ClientService implements ClientDetailsService {

    @Autowired
    private IUserDetailsDAO userDao;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        BaseClientDetails clientDetails = userDao.getByClientId(s);
        return clientDetails;
    }
}
