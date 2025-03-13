package io.onme.aaa0;

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

/**
 * @Ttron Jan 13, 2025
 */
public class ViaJWKs extends Base {

	private static final Logger LOG = LogManager.getLogger(ViaJWKs.class);

	public static void main(String[] args) {
		// line 0, first token was expired
		// line 1, second token was valid
		String accessToken = loadAccessToken(1);

		LOG.info("Original JWT: {}", accessToken);

		JwkProvider jwkProvider = new JwkProviderBuilder("https://" + AUTH0_DOMAIN + "/")
				// cache up to 10 JWKs for up to 24 hours
				.cached(10, 24, TimeUnit.HOURS).build();
		LOG.info("{}{}{}", "-".repeat(32), "Decoded", "-".repeat(32));
		DecodedJWT decodedJWT = JWT.decode(accessToken);

		String keyId = decodedJWT.getKeyId();
		LOG.info("Key Id: {}", keyId);
		LOG.info("Algorithm: {}", decodedJWT.getAlgorithm());
		LOG.info("Audienceudience: {}", decodedJWT.getAudience());
		LOG.info("Issuer: {}", decodedJWT.getIssuer());
		LOG.info("ExpiresAt: {}/{}", decodedJWT.getExpiresAtAsInstant().toEpochMilli(), decodedJWT.getExpiresAt());
		String payload = decodedJWT.getPayload();
		LOG.info("Payload as Base64: {}", payload);
		LOG.info("{}{}{}", "-".repeat(32), "Decoded Payload", "-".repeat(32));
		JSONObject payloadJson = new JSONObject(
				new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8));
		LOG.info("Payload as Json: {}", payloadJson);

		LOG.info("Scopes: {}", payloadJson.get("scope"));

		try {
			Algorithm algorithm = Algorithm.RSA256((RSAKey) jwkProvider.get(keyId).getPublicKey());
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
			DecodedJWT verifiedJWT = verifier.verify(accessToken);

			LOG.info("{}{}{}", "-".repeat(32), "Verified", "-".repeat(32));
			LOG.info("Key Id: {}, Audienceudience: {}", verifiedJWT.getKeyId(), verifiedJWT.getAudience());
		} catch (JWTVerificationException | IllegalArgumentException | JwkException e) {
			// Invalid signature/claims
			e.printStackTrace();
		}
	}
}
