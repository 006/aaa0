package io.onme.aaa0.jersey;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.APPLICATION_XML;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAKey;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.onme.aaa0.Base;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

/**
 * @Ttron Mar 12, 2025
 */
public class HelloResource extends Base {

	private static final Logger LOG = LogManager.getLogger(HelloResource.class);

	@Context
	protected HttpHeaders httpHeaders;

	@Context
	protected HttpServletResponse response;

	protected boolean authScope(String scope) {
		String authorizationHeader = httpHeaders.getHeaderString("Authorization");
		// LOG.debug( "Authorization: {}", authorizationHeader );
		if (isEmpty(authorizationHeader)) {
			LOG.error("No Authorization Header");
			throw new NotAuthorizedException(response);
		}
		String accessToken = authorizationHeader.replace("Bearer ", "");
		return verifyJWT(accessToken, scope);
	}

	private boolean verifyJWT(String accessToken, String scope) {
		String jwksUrl = "https://" + AUTH0_DOMAIN + "/";
		DecodedJWT decodedJWT = JWT.decode(accessToken);
		String keyId = decodedJWT.getKeyId();
		String payload = decodedJWT.getPayload();
		// LOG.info( "Payload as Json: {}", payloadJson );
		try {
			JwkProvider jwkProvider = new JwkProviderBuilder(jwksUrl).cached(10, 24, TimeUnit.HOURS).build();
			Algorithm algorithm = Algorithm.RSA256((RSAKey) jwkProvider.get(keyId).getPublicKey());
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwksUrl).build();

			DecodedJWT verifiedJWT = verifier.verify(accessToken);
		} catch (JWTVerificationException | IllegalArgumentException | JwkException e) {
			LOG.error("Invalid JWT: {}", e.getLocalizedMessage());
			throw new NotAuthorizedException(response);
		}

		JSONObject payloadJson = new JSONObject(
				new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8));
		String scopeGranted = payloadJson.getString("scope");
		if (isEmpty(scopeGranted)) {
			return false;
		} else {
			return scopeGranted.contains(scope);
		}
	}

	@POST
	@Produces({ APPLICATION_XML, APPLICATION_JSON })
	public Response createSomething(@FormParam("a") String valueA, @FormParam("b") double valueB,
			@FormParam("c") String valueC, @FormParam("d") String valueD) throws URISyntaxException {
		LOG.info("Create something: {}", valueA);

		/**
		 * verify the access token can access this scope
		 */
		authScope("this scope");

		// TODO

		return Response.ok().entity("Successful").build();
	}
}
