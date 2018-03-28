package org.test.zk.controllers.pelicula.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.test.zk.constants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.database.dao.PeliculasDAO;
import org.test.zk.database.datamodel.TBLPeliculas;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class peliculasController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -1262763397574490992L;
	protected ListModelList<TBLPeliculas> dataModel = null;
	protected CDatabaseConnection databaseConnection = null;

	@Wire 
	Window windowPeliculas;
	@Wire
	Button btnAdd;
	@Wire
	Button btnModify;
	@Wire
	Button btnDelete;
	@Wire
	Button btnRefresh;
	@Wire
	Listbox listboxPeliculas;

	public class rendererHelper implements ListitemRenderer<TBLPeliculas>{

		public void render(Listitem listitem, TBLPeliculas pelicula, int intIndex)
				throws Exception {
			// TODO Auto-generated method stub
			try{
				Listcell cell = new Listcell();
				cell.setLabel(pelicula.getStrId());
				listitem.appendChild(cell);

				cell = new Listcell();
				cell.setLabel(pelicula.getStrTitulo());
				listitem.appendChild(cell);

				cell = new Listcell();
				cell.setLabel(pelicula.getStrDescripcion());
				listitem.appendChild(cell);

				cell = new Listcell();
				cell.setLabel(pelicula.getStrDirector());
				listitem.appendChild(cell);

				cell = new Listcell();
				cell.setLabel(pelicula.getStrActores());
				listitem.appendChild(cell);

				cell = new Listcell();
				cell.setLabel(pelicula.getStrCantidad());
				listitem.appendChild(cell);

			}
			catch(Exception ex){
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void doAfterCompose( Component comp ){
		try{
			super.doAfterCompose(comp);

			Session currentSession = Sessions.getCurrent();

			if(currentSession.getAttribute(SystemConstants._DB_Connection_Session_Key)instanceof CDatabaseConnection){
				//Recupera de la sesión la anterior conexión
				databaseConnection = (CDatabaseConnection)currentSession.getAttribute(SystemConstants._DB_Connection_Session_Key);
				Events.echoEvent(new Event("onClick",btnRefresh));
			}
		}
		catch(Exception e){
			e.printStackTrace();

		}
		//onClickbtnConnectionToDB();
		if (dataModel == null){
			onClickbuttonRefresh();}

	}


	//@Listen("onOpen=#windowPeliculas")
	public void onClickbtnConnectionToDB(){
		Session currentSession = Sessions.getCurrent();

		if( databaseConnection == null){//btnConnectionToDB.getLabel().equalsIgnoreCase("Connect")){

			databaseConnection = new CDatabaseConnection();	


			CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig();

			//En esta línea obtenemos la ruta completa del archivo de configuración incluido el /config/
			String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir + File.separator;

			if ( databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name) ) {

				if ( databaseConnection.makeConnectionToDatabase( databaseConnectionConfig ) ) {

					//Salvamos la conexión a la sesión actual del usuario, cada usuario/ pestaña tiene su sesión
					//currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); //La sesion no es más que un arreglo asociativo


					//si se conecta lanza mensaje de bienvenida
					Messagebox.show( "Bienvenido a Video Capitol" );

				}
				else {

					Messagebox.show( "Conexión fallida" );

				}

			}
			else{
				Messagebox.show("Error al leer el archivo");
			}

		}
		else{
			if(databaseConnection != null){
				if(databaseConnection.closeConnectionToDatabase()){
					databaseConnection = null;
					Messagebox.show("Conexión creada");

					currentSession.removeAttribute(SystemConstants._DB_Connection_Session_Key);//borra la variable de sesión
				}
				else{
					Messagebox.show("Error al cerrar la conexión");
				}
			}
			else{
				Messagebox.show("¡No está conectado!");
			}
		}

		//Forzar a que se refresque la lista
		Events.echoEvent(new Event("onClick", btnRefresh));
	}

	@Listen("onClick=#btnRefresh")
	public void onClickbuttonRefresh( ) {

		//Aquí vamos a cargar el modelo con la data de la bd
		//Este evento se ejecuta no tan solo con el click del mouse del usuario, si no cuando recibimos onDialogFinished

		listboxPeliculas.setModel( (ListModelList<?>) null ); //Limpiamos el anterior modelo    

		Session currentSession = Sessions.getCurrent();

		if ( currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ) instanceof CDatabaseConnection ) {

			//Recuperamos la conexión a bd de la sesion.
			databaseConnection = (CDatabaseConnection) currentSession.getAttribute( SystemConstants._DB_Connection_Session_Key ); //Aquí vamos de nuevo con el typecast tambien llamado conversión de tipos forzado

			List<TBLPeliculas> listData = PeliculasDAO.searchData( databaseConnection );

			//Recreamos el modelo nuevamente

			dataModel = new ListModelList<TBLPeliculas>( listData );  //Creamos el modelo a partir de la lista que no retorna la bd

			//Activa la seleccion multiple de elementos util para operacion de borrado de multiples elementos a la vez
			dataModel.setMultiple( true );

			listboxPeliculas.setModel( dataModel );      

			listboxPeliculas.setItemRenderer( new rendererHelper() ); //Aqui lo asociamos al listbox

		}

	} 

	@Listen("onClick=#btnAdd")
	public void onClickbtnAdd(Event e){

		//Se pasa la referencia btnAdd
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("callerComponent", listboxPeliculas);
		//Trae a registrar.zul y lo sobrepone en pantalla
		Window win = (Window) Executions.createComponents("/views/peliculas/editor/editorPelicula.zul", null, params); //attach to page as root if parent is null
		win.doModal();
	}

	@Listen("onClick=#btnModify")
	public void onClickbtnModify(Event e){

		Set<TBLPeliculas> selectecItems = dataModel.getSelection();

		if(selectecItems != null && selectecItems.size()>0){

			TBLPeliculas pelicula = selectecItems.iterator().next();

			Map<String,Object> params = new HashMap<String, Object>();

			params.put("peliculaToModify", pelicula);
			params.put("IdPelicula", pelicula.getStrId());
			params.put("callerComponent", listboxPeliculas);
			Window win = (Window)Executions.createComponents("/views/peliculas/editor/editorPelicula.zul", null, params);
			win.doModal();			
		}

		else{
			Messagebox.show("No ha seleccionado nada");
		}
		Events.echoEvent(new Event("onClick",btnRefresh));

	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	@Listen( "onClick=#btnDelete" )
	public void onClickbuttonDelete( Event event ) {

		final Set<TBLPeliculas> selectedItems = dataModel.getSelection();

		if ( selectedItems != null && selectedItems.size() > 0 ) {

			//Obtenemos el primero de la lista es una lista por que puedes tener seleccion multiple 

			String strBuffer = null;

			for ( TBLPeliculas peliculas : selectedItems ) {

				if ( strBuffer == null ) {

					strBuffer = peliculas.getStrId() + " " + peliculas.getStrTitulo() + " " + peliculas.getStrDescripcion();

				}
				else {

					strBuffer = strBuffer + "\n" + peliculas.getStrId() + " " + peliculas.getStrTitulo() + " " + peliculas.getStrDescripcion();

				}   

			}

			Messagebox.show( "¿Seguro que desea eliminar los " + Integer.toString( selectedItems.size() ) + " registros?\n" + strBuffer, "Eliminar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {

				public void onEvent(Event evt) throws InterruptedException {

					if ( evt.getName().equals( "onOK" ) ) {

						//Eliminar los registros seleccionados
						while ( selectedItems.iterator().hasNext() ) {

							TBLPeliculas peliculas = selectedItems.iterator().next();

							//selectedItems.iterator().remove();

							PeliculasDAO.deleteData(databaseConnection, peliculas.getStrId());

							dataModel.remove( peliculas );

						}    

					} 

				}

			});		    

			//Messagebox.show( strBuffer );


		}
		else {

			Messagebox.show( "No hay seleccion" );

		}

	}

	@Listen( "onDialogFinished=#listboxPeliculas" ) //Notifica que se agrego o modifico una entidad y que debe refrescar la lista el modelo
	public void onDialogFinishedlistboxPeliculas( Event event ) {

		//En zk es posible lanzar eventos no tan solo definidos por nosotros mismo si no tambien los estandares de zk como el click

		//Forzamos refrescar la lista 

		Events.echoEvent( new Event( "onClick", btnRefresh ) );  //Lanzamos el evento click de zk


	}

	@Listen("onDialogFinished=#btnAdd")
	public void onDialogFinishedbtnAdd(Event e){

		//este evento recibe del controlador registrarPeliculas.zul
		System.out.println("Evento Add recibido");

		if(e.getData() != null){

			TBLPeliculas pelicula = (TBLPeliculas) e.getData();

			Events.echoEvent(new Event("onClick",btnRefresh));
		}

	}
}
