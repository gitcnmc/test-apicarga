package es.cnmc.sede;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Base para los test sobre servicios REST con autenticación OAuth
 * @author cnmc
 *
 */
public abstract class TestBase {
	
    protected String consumerKey;
    protected String consumerSecret;
    protected String baseUrl;
    
    /** Definicion del API. La 'base' (hostname, port, ...) se define en los test concretos */
    private DefaultApi10a api = new DefaultApi10a() {
    	
    	private String getBase() {
    		return baseUrl;
    	}
    	@Override
    	public String getRequestTokenEndpoint() {
    		return getBase() + "/oauth/request_token";
    	}

    	@Override
    	public String getAccessTokenEndpoint() {
    		return getBase() + "/oauth/access_token";
    	}

    	@Override
    	public String getAuthorizationUrl(Token requestToken) {
    		return "none";
    	}
    };
    
    /** Se acepta cualquier certificado de servidor */
    public void setupSSLyCredenciales() throws Exception {
    	SSLContext sc = SSLContext.getInstance("TLS");
    	TrustManager tm = new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}
			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}
			
		};
    	sc.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
    	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
    	    public boolean verify(String string,SSLSession ssls) {
    	        return true;
    	    }
    	});    	
    }
    
    /** Crea el servicio con OAuth */
    protected OAuthService createService(String key, String secret) {
    	return new ServiceBuilder()
	        .provider(api)
	        .apiKey(key)
	        .apiSecret(secret)
	        .signatureType(SignatureType.Header)
	        .build();
    }
    
    /** Envia una petición firmada: zero-legged (sin request_token) */
    protected Response sendOAuthRequest(OAuthService service, OAuthRequest request) {
    	Token tk = OAuthConstants.EMPTY_TOKEN;
    	service.signRequest(tk, request);
    	log("request_signed: " + request);
    	log("request_signed.getOauthParameters: " +request.getOauthParameters());
    	return request.send();
    }
    
	/** simplificacion de peticion de metodo GET */
    public Response simple_get(OAuthService service, String uri) {
		OAuthRequest request = new OAuthRequest(Verb.GET, baseUrl + uri);
		log("Solicitando GET {0}", uri);
		Response response = sendOAuthRequest(service, request);
		return response;
	}

    /** El log se puede realizar como se desee: log4j, logback, sysout... */
    protected abstract void log(String mensaje, Object... params);
    
}
