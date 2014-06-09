package com.wind.quicknote.system.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.wind.quicknote.helper.SpringBeanUtil;

/**
 * https://developers.google.com/accounts/docs/OAuth2WebServer
 * http://www.hurl.it/
 */
public class OAuthClientGoogle {

	private static final String _CLIENT_ID = SpringBeanUtil.getProp("oauth.google.client.id");
	private static final String _CLIENT_SECRET = SpringBeanUtil.getProp("oauth.google.client.secret");
	private static final String _SITE_URL = SpringBeanUtil.getProp("oauth.google.site.url");
	private static final String _RESOURCE_URI = SpringBeanUtil.getProp("oauth.google.resource.uri");
	
	private static OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

	public static String getAuthorizationToken() throws OAuthSystemException,
			IOException {

		OAuthClientRequest request = OAuthClientRequest
				.authorizationProvider(OAuthProviderType.GOOGLE)
				.setClientId(_CLIENT_ID)
				.setRedirectURI(_SITE_URL)
				.setResponseType("code")
				.setScope("profile email")
				.buildQueryMessage();

		return request.getLocationUri();
	}
	
	public static String getAccessToken(String code)
			throws OAuthSystemException, IOException, OAuthProblemException {

		OAuthClientRequest request = OAuthClientRequest
				.tokenProvider(OAuthProviderType.GOOGLE)
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.setClientId(_CLIENT_ID)
				.setClientSecret(_CLIENT_SECRET)
				.setRedirectURI(_SITE_URL)
				.setCode(code)
				.buildBodyMessage();

		OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(
				request, OAuthJSONAccessTokenResponse.class);

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
                .authorizationProvider(OAuthProviderType.GOOGLE)
                .setClientId(_CLIENT_ID)
                .setRedirectURI(_SITE_URL)
                .setResponseType("code")
                .setScope("profile email")
                .buildQueryMessage();

            //in web application you make redirection to uri:
            System.out.println("Visit: " + request.getLocationUri() + "\nand grant permission");

            System.out.print("Now enter the OAuth code you have received in redirect uri ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String code = br.readLine();

            request = OAuthClientRequest
            	.tokenProvider(OAuthProviderType.GOOGLE)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(_CLIENT_ID)
                .setClientSecret(_CLIENT_SECRET)
                .setRedirectURI(_SITE_URL)
                .setCode(code)
                .buildBodyMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            
            // Get Access Token
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request, OAuthJSONAccessTokenResponse.class);
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
        	e.printStackTrace();
            System.out.println("OAuth error: " + e.getError());
            System.out.println("OAuth error description: " + e.getDescription());
        }
    }

}
