package es.cnmc.sede;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * JSON para la petición de listar_cargas
 * @author cnmc
 *
 */
@JsonInclude(Include.NON_NULL)
public class ListarCargasRequest {
	private String nifPresentador;
	private String nifEmpresa;
	private String idProcedimiento;
	private String uuidCarga;
	private String estado;
	public String getNifPresentador() {
		return nifPresentador;
	}
	public void setNifPresentador(String nifPresentador) {
		this.nifPresentador = nifPresentador;
	}
	public String getNifEmpresa() {
		return nifEmpresa;
	}
	public void setNifEmpresa(String nifEmpresa) {
		this.nifEmpresa = nifEmpresa;
	}
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	public String getUuidCarga() {
		return uuidCarga;
	}
	public void setUuidCarga(String uuidCarga) {
		this.uuidCarga = uuidCarga;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

}
