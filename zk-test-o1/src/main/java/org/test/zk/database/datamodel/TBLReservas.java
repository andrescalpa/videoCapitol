package org.test.zk.database.datamodel;

import java.io.Serializable;

public class TBLReservas extends CAuditableDataModel implements Serializable {

	private static final long serialVersionUID = -2496069379202109391L;

	protected String strIdOperador;
	protected String strNombreOperador;
	protected String strIdPelicula;
	protected String strTituloPelicula;
	
	public String getStrIdOperador() {
		return strIdOperador;
	}
	public void setStrIdOperador(String strIdOperador) {
		this.strIdOperador = strIdOperador;
	}
	public String getStrNombreOperador() {
		return strNombreOperador;
	}
	public void setStrNombreOperador(String strNombreOperador) {
		this.strNombreOperador = strNombreOperador;
	}
	public String getStrIdPelicula() {
		return strIdPelicula;
	}
	public void setStrIdPelicula(String strIdPelicula) {
		this.strIdPelicula = strIdPelicula;
	}
	public String getStrTituloPelicula() {
		return strTituloPelicula;
	}
	public void setStrTituloPelicula(String strTituloPelicula) {
		this.strTituloPelicula = strTituloPelicula;
	}
}
