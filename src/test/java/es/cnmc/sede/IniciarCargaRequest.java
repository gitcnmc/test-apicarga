package es.cnmc.sede;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * JSON usado como entrada para API iniciar_carga
 * @author cnmc
 *
 */
@JsonInclude(Include.NON_NULL)
public class IniciarCargaRequest {
	private String nifPresentador;
	private String nifEmpresa;
	private Integer idProcedimiento;
	@JsonFormat(shape=Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
	private Date fechaEfecto;
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
	public Integer getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(Integer idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	public Date getFechaEfecto() {
		return fechaEfecto;
	}
	public void setFechaEfecto(Date fechaEfecto) {
		this.fechaEfecto = fechaEfecto;
	}

}
