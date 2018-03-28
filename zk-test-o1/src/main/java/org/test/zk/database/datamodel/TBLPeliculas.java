package org.test.zk.database.datamodel;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.RichTextString;

public class TBLPeliculas extends CAuditableDataModel implements Serializable{

	private static final long serialVersionUID = -2856307061627114929L;

	protected String strId;
	protected String strTitulo;
    protected String strDescripcion;
    protected String strDirector;
    protected String strActores;
    protected String strCantidad;
    
  //Constructor
    public TBLPeliculas( String strTitulo, String strDescripcion, String strDirector, String strActores, String strCantidad, String strId ) {
        
    	this.strId = strId;
    	this.strTitulo = strTitulo;
    	this.strDescripcion = strDescripcion;
    	this.strDirector = strDirector;
    	this.strActores = strActores;
        this.strCantidad = strCantidad;
        
    }

    public String getStrId() {
		return strId;
	}

	public void setStrId(String strId) {
		this.strId = strId;
	}

	public TBLPeliculas(){
    	
    }

	public String getStrTitulo() {
		return strTitulo;
	}

	public void setStrTitulo(String strTitulo) {
		this.strTitulo = strTitulo;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}

	public String getStrDirector() {
		return strDirector;
	}

	public void setStrDirector(String strDirector) {
		this.strDirector = strDirector;
	}

	public String getStrActores() {
		return strActores;
	}

	public void setStrActores(String strActores) {
		this.strActores = strActores;
	}

	public String getStrCantidad() {
		return strCantidad;
	}

	public void setStrCantidad(String strCantidad) {
		this.strCantidad = strCantidad;
	}

}
