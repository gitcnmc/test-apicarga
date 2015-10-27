package es.cnmc.sede;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * JSON con respuesta iniciar_carga
 * @author cnmc
 *
 */
@JsonInclude(Include.NON_NULL)
public class IniciarCarga {
	private String uuidCarga;
	private Long expiracionSegundos;
	public String getUuidCarga() {
		return uuidCarga;
	}
	public void setUuidCarga(String uuidCarga) {
		this.uuidCarga = uuidCarga;
	}
	public Long getExpiracionSegundos() {
		return expiracionSegundos;
	}
	public void setExpiracionSegundos(Long expiracionSegundos) {
		this.expiracionSegundos = expiracionSegundos;
	}

}
