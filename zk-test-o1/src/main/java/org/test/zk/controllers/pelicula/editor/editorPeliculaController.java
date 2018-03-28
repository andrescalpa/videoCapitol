package org.test.zk.controllers.pelicula.editor;

import org.zkoss.zul.Textbox;
import org.test.zk.constants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.dao.PeliculasDAO;
import org.test.zk.database.datamodel.TBLPeliculas;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

public class editorPeliculaController extends SelectorComposer<Component>{


	private static final long serialVersionUID = -8974410954390598863L;
	protected CDatabaseConnection databaseConnection = null;
	protected ListModelList<String> dataModel = new ListModelList<String>();
	protected Component callerComponent = null;//Variable de clase de tipo protegida
	protected TBLPeliculas peliculaToModify = null;//Guarda la pelicula a ser modificada
	protected TBLPeliculas peliculaToAdd = null;//Guarda la pelicula a ser agregada

	@Wire
	Window windowRegistrarP;
	@Wire
	Button buttonCancel;
	@Wire 
	Button buttonAccept;
	@Wire
	Textbox textboxId;
	@Wire
	Textbox textboxTitulo;
	@Wire
	Textbox textboxDescripcion;
	@Wire
	Textbox textboxActores;
	@Wire
	Textbox textboxDirector;
	@Wire
	Textbox textboxTCantidadInv;

	public void doAfterCompose(Component comp){
		try{
			super.doAfterCompose(comp);
			final Execution execution = Executions.getCurrent();

			Session currentSession = Sessions.getCurrent();

			if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {

				//Recuperamos la conexión a bd de la sesion.
				databaseConnection = (CDatabaseConnection) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key );

				//PeliculaToModify debe venir de la bd y no de la lista pasada como argumento
				if ( execution.getArg().get( "IdPelicula" ) instanceof String ) { 

					//Cargamos la data de la bd
					peliculaToModify = PeliculasDAO.loadData( databaseConnection, (String) execution.getArg().get( "IdPelicula" ) );

				}

			}    

			if(peliculaToModify != null){
				textboxId.setValue(peliculaToModify.getStrId());
				textboxTitulo.setValue(peliculaToModify.getStrTitulo());
				textboxDirector.setValue(peliculaToModify.getStrDirector());
				textboxDescripcion.setValue(peliculaToModify.getStrDescripcion());
				textboxActores.setValue(peliculaToModify.getStrActores());
				textboxTCantidadInv.setValue(peliculaToModify.getStrCantidad());
			}
			//Debe guardar la referencia al componente que envia el controlador del peliculas.zul
			callerComponent = (Component) execution.getArg().get( "callerComponent" ); //Usa un  typecast a Component que es el padre de todos los elementos visuales de zk
		}
		catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Listen("onClick=#buttonAccept")
	public void onClickButtonAccept(Event event){

		windowRegistrarP.detach();
		if(peliculaToModify != null){

			peliculaToModify.setStrId(textboxId.getValue());
			peliculaToModify.setStrTitulo(textboxTitulo.getValue());
			peliculaToModify.setStrDescripcion(textboxDescripcion.getValue());
			peliculaToModify.setStrDirector(textboxDirector.getValue());
			peliculaToModify.setStrActores(textboxActores.getValue());
			peliculaToModify.setStrCantidad(textboxTCantidadInv.getValue());

			PeliculasDAO.updateData(databaseConnection, peliculaToModify ); //Guardamos en la BD Actualizamos
			
			//Lanzamos el evento retornamos la pelicula a modificar
            Events.echoEvent( new Event( "onDialogFinished", callerComponent, peliculaToModify ) ); //Suma importancia que los nombres de los eventos coincidan
		}
		else{
			peliculaToAdd = new TBLPeliculas();//se usa el constructor sin parametros

			peliculaToAdd.setStrId(textboxId.getValue());
			peliculaToAdd.setStrTitulo(textboxTitulo.getValue());
			peliculaToAdd.setStrDescripcion(textboxDescripcion.getValue());
			peliculaToAdd.setStrDirector(textboxDirector.getValue());
			peliculaToAdd.setStrActores(textboxActores.getValue());
			peliculaToAdd.setStrCantidad(textboxTCantidadInv.getValue());

			PeliculasDAO.insertData( databaseConnection, peliculaToAdd ); //Guarda en la BD Insertamos
			
			Events.echoEvent(new Event("onDialogFinished",callerComponent,peliculaToAdd));
		}

	}
	@Listen( "onClick=#buttonCancel" )
	public void onClickButtonCancelar( Event event ) {


		windowRegistrarP.detach();

	}


}
