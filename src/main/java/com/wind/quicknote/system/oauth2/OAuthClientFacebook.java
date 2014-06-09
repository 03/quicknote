package com.wind.quicknote.system.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.wind.quicknote.helper.SpringBeanUtil;

public class OAuthClientFacebook {

	static final String _CLIENT_ID = SpringBeanUtil.getProp("oauth.facebook.client.id");
	static final String _CLIENT_SECRET = SpringBeanUtil.getProp("oauth.facebook.client.secret");
	static final String _SITE_URL = SpringBeanUtil.getProp("oauth.facebook.site.url");
	static final String _RESOURCE_URI = SpringBeanUtil.getProp("oauth.facebook.resource.uri");
	
	private static OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
	
	public static String getAuthorizationToken() throws OAuthSystemException,
			IOException {

		OAuthClientRequest request = OAuthClientRequest
				.authorizationProvider(OAuthProviderType.FACEBOOK)
				.setClientId(_CLIENT_ID)
				.setRedirectURI(_SITE_URL)
				.setScope("email")
				.buildQueryMessage();

		return request.getLocationUri();
	}
	
	public static String getAccessToken(String code)
			throws OAuthSystemException, IOException, OAuthProblemException {

		OAuthClientRequest request = OAuthClientRequest
				.tokenProvider(OAuthProviderType.FACEBOOK)
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.setClientId(_CLIENT_ID)
				.setClientSecret(_CLIENT_SECRET)
				.setRedirectURI(_SITE_URL)
				.setCode(code)
				.buildBodyMessage();

		GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request,
				GitHubTokenResponse.class);

		return oAuthResponse.getAccessToken();
	}
	
	public static String accessResource(String accessToken)
			throws OAuthSystemException, IOException, OAuthProblemException {

		OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(
				_RESOURCE_URI).setAccessToken(accessToken).buildQueryMessage();

		OAuthResourceResponse resourceResponse = oAuthClient.resource(
				bearerClientRequest, OAuth.HttpMethod.GET,
				OAuthResourceResponse.class);

		return resourceResponse.getBody();
	}
	
	public static void main(String[] args) throws OAuthSystemException, IOException {

        try {
            OAuthClientRequest request = OAuthClientRequest
                .authorizationProvider(OAuthProviderType.FACEBOOK)
                .setClientId(_CLIENT_ID)
                .setRedirectURI(_SITE_URL)
                .setScope("email")
                .buildQueryMessage();

            //in web application you make redirection to uri:
            System.out.println("Visit: " + request.getLocationUri() + "\nand grant permission");

            System.out.print("Now enter the OAuth code you have received in redirect uri ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String code = br.readLine();

            request = OAuthClientRequest
            	.tokenProvider(OAuthProviderType.FACEBOOK)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(_CLIENT_ID)
                .setClientSecret(_CLIENT_SECRET)
                .setRedirectURI(_SITE_URL)
                .setCode(code)
                .buildBodyMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            // Get Access Token
            //Facebook is not fully compatible with OAuth 2.0 draft 10, access token response is
            //application/x-www-form-urlencoded, not json encoded so we use dedicated response class for that
            //Own response class is an easy way to deal with oauth providers that introduce modifications to
            //OAuth specification
            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
            System.out.println(
                "Access Token: " + oAuthResponse.getAccessToken() + ", Expires in: " + oAuthResponse
                    .getExpiresIn());
            
            // Access Resource
            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(_RESOURCE_URI)
            .setAccessToken(oAuthResponse.getAccessToken()).buildQueryMessage();
    
			OAuthResourceResponse resourceResponse = oAuthClient.resource(
					bearerClientRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			System.out.println("resourceResponse: " + resourceResponse.getBody());
			
            
        } catch (OAuthProblemException e) {
            System.out.println("OAuth error: " + e.getError());
            System.out.println("OAuth error description: " + e.getDescription());
        }
    }

}
