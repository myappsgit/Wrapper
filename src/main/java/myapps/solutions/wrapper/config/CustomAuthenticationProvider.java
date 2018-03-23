package myapps.solutions.wrapper.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.utils.Misc;
import myapps.solutions.wrapper.utils.ResponseCode;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private IUserDetailsDAO userDetailsDao;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		AuthorizationResult result = userDetailsDao.canAuthenticate(authentication.getName(),
				Base64.decodeBase64((String) authentication.getCredentials()));
	    System.out.println(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("username"));
		if (result.getResult() == AuthorizationCodes.Success)
			result = userDetailsDao.createSession(result.getUserId(), "", "", "", "", "cashup");
		else {
			AuthorizationCodes status = result.getResult();
			if (status == AuthorizationCodes.Fail)
				throw new BadCredentialsException(ResponseCode.AuthUserFailure);
			else if (status == AuthorizationCodes.DeviceLimitReached)
				throw new BadCredentialsException(ResponseCode.AuthDeviceLimitReached);
			else if (status == AuthorizationCodes.SessionExist)
				throw new BadCredentialsException(ResponseCode.AuthSessionExist);
			else if(status == AuthorizationCodes.MobileNumberNotVerified)
				throw new BadCredentialsException(ResponseCode.EmailNotVerified);
			else if(status == AuthorizationCodes.EmailIdNotVerified)
				throw new BadCredentialsException(ResponseCode.EmailIdNotVerified);
			else if (status == AuthorizationCodes.AccountDeActivated)
				throw new BadCredentialsException(ResponseCode.AccountDeActivated);
			else if (status == AuthorizationCodes.NotFound)
				throw new BadCredentialsException(ResponseCode.AuthUserFailure);

		}
		List<SimpleGrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + result.getUserType()));
		Misc.setSessionId(authentication.getName(), result.getSessionId());
		return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getName(),
				grantedAuths);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
