package io.onme.aaa0;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.json.JSONObject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @Ttron Jan 13, 2025
 */
public class ViaManagementAPI extends Base {

	private static final Logger LOG = LogManager.getLogger(ViaManagementAPI.class);

	/**
	 * Replace with Domain from Dashboard>Application>{your
	 * application}>Settings>Basic Infomation>Client Id
	 * DO NOT LEAVE IT IN YOUR CODE
	 */
	static final String AUTH0_CLIENT_ID = "GuXAVzMBzzzzzzzzz8eB8Xpd";

	/**
	 * Replace with Domain from Dashboard>Application>{your
	 * application}>Settings>Basic Infomation>Client Secret
	 * DO NOT LEAVE IT IN YOUR CODE
	 */
	static final String AUTH0_CLIENT_SECRET = "pW6UHzzzzzzzzzNsvX_9kZxm";

	private static String getSigningKey(String accessToken) {
		String cert = null;
		try (Client client = ClientBuilder.newBuilder().register(MoxyJsonFeature.class).build()) {
			// WebTarget target = client.target(
			// "https://"+AUTH0_DOMAIN+"/api/v2/keys/signing" );
			WebTarget target = client.target("https://" + AUTH0_DOMAIN + "/api/v2/keys/signing/6Zlqx5vWMKYt82hCIONNC");
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.header("Authorization", "Bearer " + accessToken).get();
			String data = response.readEntity(String.class);
			// LOG.info(data);
			if (!isEmpty(data)) {
				JSONObject json = new JSONObject(data);
				cert = json.getString("cert");
				// LOG.info("cert:\n {}", cert);

				data = data.replaceAll("\\\\r\\\\n", System.lineSeparator());
				data = data.replaceAll("cert\":\"", "cert\":\"" + System.lineSeparator());
				data = data.replaceAll("\",\"pkcs7\":\"",
						System.lineSeparator() + "\",\"pkcs7\":\"" + System.lineSeparator());
				String finalName = "src/main/resources/keys.txt";
				try {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalName)));
					writer.write(data);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cert;
	}

	private static String askAccessToken(String clientId, String clientSecret) {

		String token = null;
		try (Client client = ClientBuilder.newBuilder().register(MoxyJsonFeature.class).build()) {
			JSONObject json = new JSONObject();
			json.put("client_id", clientId);
			json.put("client_secret", clientSecret);
			json.put("grant_type", "client_credentials");
			json.put("audience", "https://" + AUTH0_DOMAIN + "/api/v2/");

			WebTarget target = client.target("https://" + AUTH0_DOMAIN + "/oauth/token");
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));
			String data = response.readEntity(String.class);
			// LOG.info(data);
			if (!isEmpty(data)) {
				JSONObject resp = new JSONObject(data);
				token = resp.getString("access_token");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}

	public static void main(String[] args) throws Exception {

		String managementAccessToken = askAccessToken(AUTH0_CLIENT_ID, AUTH0_CLIENT_SECRET);

		String certStr = getSigningKey(managementAccessToken);

		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) f.generateCertificate(new ByteArrayInputStream(certStr.getBytes()));
		PublicKey publicKey = cert.getPublicKey();
		byte[] encoded = publicKey.getEncoded();
		byte[] b64key = Base64.getEncoder().encode(encoded);
		LOG.info("Public Key: {} ", new String(b64key));

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encoded);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
		// System.out.println("x509EncodedKeySpec = " + new String(key.getEncoded()));
		// BigInteger modulus = pk.getModulus();
		// System.out.println(modulus);
		// BigInteger exponent = pk.getPublicExponent();
		// System.out.println(exponent);

		// line 0, first token was expired
		// line 1, second token was valid
		String accessToken = loadAccessToken(1);

		try {
			Algorithm algorithm = Algorithm.RSA256((RSAKey) key);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
			DecodedJWT verifiedJWT = verifier.verify(accessToken);

			LOG.info("{}{}{}", "-".repeat(32), "Verified", "-".repeat(32));
			LOG.info("Key Id: {}, Audienceudience: {}", verifiedJWT.getKeyId(), verifiedJWT.getAudience());
		} catch (JWTVerificationException | IllegalArgumentException e) {
			// Invalid signature/claims
			e.printStackTrace();
		}
	}
}
