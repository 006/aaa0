package io.onme.aaa0;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Ttron Jan 13, 2025
 */
public class Base {
	/**
	 * Replace with Domain from Dashboard>Application>{your
	 * application}>Settings>Basic Infomation>Domain
	 */
	public static final String AUTH0_DOMAIN = "dev-zzz.ca.auth0.com";

	/**
	 * Replace with Domain from Dashboard>Application>{your
	 * application}>Settings>Basic Infomation>Domain
	 */
	public static final String ISSUER = "https://dev-zzz.ca.auth0.com/";

	protected static boolean isEmpty(String test) {
		if (test != null && "".equalsIgnoreCase(test)) {
			return true;
		}
		return false;
	}

	protected static String loadAccessToken(int line) {
		String filePath = "../tokens.txt";
		File accessTokenFile = new File(filePath);
		String accessToken = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(accessTokenFile));
			for (int i = -1; i < line; i++) {
				accessToken = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return accessToken;
	}
}
