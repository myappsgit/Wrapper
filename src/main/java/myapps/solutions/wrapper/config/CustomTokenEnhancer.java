package myapps.solutions.wrapper.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import myapps.solutions.wrapper.utils.Misc;
import myapps.solutions.wrapper.utils.ResponseCode;

@Configuration
public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		final Map<String, Object> additionalInfo = new HashMap<String, Object>();
		additionalInfo.put("sessionId", Misc.getSessionId(authentication.getName()));
		additionalInfo.put("ResponseCode", ResponseCode.AuthUserSuccessful);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}

}
