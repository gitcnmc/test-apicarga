package es.cnmc.sede;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>Probador de las cargas al API: <a href="https://api.cnmc.gob.es/doc">doc api</a></p>
 * <p>Utiliza un fichero de propiedades para los datos de prueba. Arrancar con -Dconfiguracion=[Ruta del fichero]</p>
 * @author cnmc
 *
 */
public class TestCargas extends TestBase {
	
    private ObjectMapper om = new ObjectMapper();

    @Override
	protected void log(String mensaje, Object... params) {
    	if (params == null || params.length == 0)
    		System.out.println(mensaje);
    	else
    		System.out.println(MessageFormat.format(mensaje, params));
	}
	
	/** Muestra log del resultado */
	private void showResponse(Response response) {
		log("response={0}", response.getBody());
	}

	private Properties conf = new Properties();

	@Before
	public void init() throws Exception {
		String cfg = System.getProperty("user.home") + "/configuracion/test_cnmc.properties";
		log("Leyendo configuración: ", cfg);
		conf.load(new FileReader(cfg));
		// cargamos la configuración
		super.baseUrl = conf.getProperty("baseUrl", "https://localhost/");
		super.consumerKey = conf.getProperty("consumerKey");
		super.consumerSecret = conf.getProperty("consumerSecret");
		
	}
	
	// ////////////////////////////////////////////////////
	// metodos individuales de cada llamada al API

	private void cargar_fichero_completo(String nifPresentador, String nifEmpresa, String idProcedimiento, 
			String tipoFichero, File fichero) throws IOException {
		OAuthService service = createService(consumerKey, consumerSecret);
		OAuthRequest request = new OAuthRequest(Verb.POST, baseUrl + "/carga/v1/cargar_fichero_completo");
		
		request.addQuerystringParameter("tipoFichero", tipoFichero );
		request.addQuerystringParameter("numeroBytes", String.valueOf(fichero.length()));
		request.addQuerystringParameter("nifPresentador", nifPresentador);
		request.addQuerystringParameter("nifEmpresa", nifEmpresa);
		request.addQuerystringParameter("idProcedimiento", idProcedimiento);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", fichero);
		HttpEntity me = builder.build();
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int)me.getContentLength());
		me.writeTo(bos);
		request.addPayload(bos.toByteArray());

		Header contentType = me.getContentType();
		request.addHeader(contentType.getName(), contentType.getValue());
		Response response = sendOAuthRequest(service, request);
		showResponse(response);		
	}

	private void consultar_estado_carga(OAuthService service, String uuidCarga) {
		Response r = super.simple_get(service, "/carga/v1/consultar_estado_carga/" + uuidCarga);
		showResponse(r);
	}

	private void confirmar_carga(OAuthService service, String uuidCarga) {
		Response r = simple_get(service, "/carga/v1/confirmar_carga/" + uuidCarga);
		showResponse(r);
	}

	private void cancelar_carga(OAuthService service, String uuidCarga) {
		Response r = simple_get(service, "/carga/v1/cancelar_carga/" + uuidCarga);
		showResponse(r);
	}

	private void subir_fichero_completo(OAuthService service, String uuidCarga, String tipoFichero, File fichero) throws Exception {
		OAuthRequest request = new OAuthRequest(Verb.POST, this.baseUrl + "/carga/v1/subir_fichero_completo");
		request.addQuerystringParameter("uuidCarga", uuidCarga);
		request.addQuerystringParameter("tipoFichero", tipoFichero);
		request.addQuerystringParameter("numeroBytes", String.valueOf(fichero.length()));
		request.addQuerystringParameter("nombreFichero", fichero.getName());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", fichero);
		HttpEntity me = builder.build();
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int)me.getContentLength());
		me.writeTo(bos);
		request.addPayload(bos.toByteArray());

		Header contentType = me.getContentType();
		request.addHeader(contentType.getName(), contentType.getValue());
		Response response = sendOAuthRequest(service, request);
		log("subir_fichero_completo: ");
		showResponse(response);		
	}

	private IniciarCarga iniciar_carga(OAuthService service, String idProcedimiento, String nifEmpresa, String nifPresentador) throws Exception {
		OAuthRequest request = new OAuthRequest(Verb.POST, this.baseUrl + "/carga/v1/iniciar_carga");
        request.addHeader("Content-Type", "application/json");
        IniciarCargaRequest iniciarCarga = new IniciarCargaRequest();
        iniciarCarga.setFechaEfecto(new Date());
        iniciarCarga.setIdProcedimiento(Integer.parseInt(idProcedimiento));
        iniciarCarga.setNifEmpresa(nifEmpresa);
        iniciarCarga.setNifPresentador(nifPresentador);
		String json = om.writeValueAsString(iniciarCarga);
		log("json iniciar_carga: {0}", json);
		request.addPayload(json);
		Response response = sendOAuthRequest(service, request);
		log("inicia_carga: {0}", response.getBody());
		return om.readValue(response.getBody(), IniciarCarga.class);
	}
	
	private void listar_cargas(OAuthService service, String uuidCarga, String nifEmpresa, String idProcedimiento, String estado) throws Exception {
		OAuthRequest request = new OAuthRequest(Verb.POST, this.baseUrl + "/carga/v1/listar_cargas");
        request.addHeader("Content-Type", "application/json");
		ListarCargasRequest req = new ListarCargasRequest();
		req.setUuidCarga(uuidCarga);
		req.setIdProcedimiento(idProcedimiento);
		req.setNifEmpresa(nifEmpresa);
		String json = om.writeValueAsString(req);
		log("json listar_cargas: {0}", json);
		request.addPayload(json);
		
		Response response = sendOAuthRequest(service, request);
		log("listar_cargas: ");
		showResponse(response);
	}
	
	/** Prueba de carga con un solo fichero */
	@Test
	public void testCargarFicheroCompleto() throws Exception {
		File f = new File(conf.getProperty("fichero1"));
		cargar_fichero_completo(conf.getProperty("nifPresentador"),
				conf.getProperty("nifEmpresa"),
				conf.getProperty("idProcedimiento"),
				conf.getProperty("tipoFichero"),
				f);
	}
	
	/** Prueba de carga con varios ficheros */
	@Test
	public void testIniciaSubeMultiplesConfirma() {
		OAuthService service = createService(consumerKey, consumerSecret);
		IniciarCarga carga = null;
		try {
			carga = iniciar_carga(service, conf.getProperty("idProcedimiento"),
					conf.getProperty("nifEmpresa"),
					conf.getProperty("nifPresentador"));
			String tf = conf.getProperty("tipoFichero");
			File f = new File(conf.getProperty("fichero1"));
			subir_fichero_completo(service, carga.getUuidCarga(), tf, f);
			File f2 = new File(conf.getProperty("fichero2"));
			subir_fichero_completo(service, carga.getUuidCarga(), tf, f2);
			File f3 = new File(conf.getProperty("fichero3"));
			subir_fichero_completo(service, carga.getUuidCarga(), tf, f3);
			confirmar_carga(service, carga.getUuidCarga());
			consultar_estado_carga(service, carga.getUuidCarga());
		} catch (Exception e) {
			cancelar_carga(service, carga.getUuidCarga());
		}
	}

	/** Prueba de consulta */
	@Test
	public void testConsulta() throws Exception {
		OAuthService service = createService(consumerKey, consumerSecret);
		listar_cargas(service, null,
				conf.getProperty("nifEmpresa"), 
				conf.getProperty("idProcedimiento"), 
				conf.getProperty("estado"));
	}

}
