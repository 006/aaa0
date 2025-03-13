package io.onme.aaa0.jersey;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @Ttron Mar 12, 2025
 */
public class RestfulApplication extends ResourceConfig {

	public RestfulApplication() {
		register(MoxyJsonFeature.class);
		register(MoxyXmlFeature.class);
		register(MultiPartFeature.class);
		packages(HelloResource.class.getPackage().getName());
	}
}