package org.pucar.dristi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.TimeZone;

@Import({TracerConfiguration.class})
@SpringBootApplication
@PropertySource("classpath:application.properties")
@Configuration
@Slf4j
public class Main {
	/**
	 * ES8 cluster default configuration with security enabled forces the use of https for communication to the ES cluster.
	 * This function is used to accept the self signed certificates from the ES8 cluster so SSLCertificateException is not t hrown.
	 * The ideal way to solve this is to import the self signed certificates into the JKS.
	 */

	@Value("${app.timezone}")
	private String timeZone;

	public static void trustSelfSignedSSL() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) {
					// No implementation needed for trusting all certificates
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) {
					// No implementation needed for trusting all certificates
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			};
			ctx.init(null, new TrustManager[]{tm}, null);
			SSLContext.setDefault(ctx);

			// Disable hostname verification
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
	}

	@Bean
	public RestTemplate restTemplate() {
		trustSelfSignedSSL();
		return new RestTemplate();
	}

}
