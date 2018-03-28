package org.test.zk.controllers.pelicula.manager;

import java.io.File;
import java.util.List;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class peliculasNoClienteController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 5523189278057170467L;
	protected ListModelList<TBLPeliculas> dataModel = null;
	protected CDatabaseConnection databaseConnection = null;

	@Wire 
	Window windowPeliculasNoCliente;
	@Wire
	Listbox listboxPeliculas;
	

	public class rendererHelper implements ListitemRenderer<TBLPeliculas>{
		/*public void render(Listitem listitem, Object data, int index) {
	        Listcell cell = new Listcell();
	        listitem.appendChild(cell);
	        if (data instanceof String[]){
	            cell.appendChild(new Label(((String[])data)[0].toString()));
	        } else if (data instanceof String){
	            cell.appendChild(new Label(data.toString()));
	        } else {
	            cell.appendChild(new Label("UNKNOW:"+data.toString()));
	        }
	    }*/

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
				//Events.echoEvent(new Event("onClick"));
			}
		}
		catch(Exception e){
			e.printStackTrace();

		}
		connectionToDB();
		if (dataModel == null){
			refresh();}
		

	}


	
	public void connectionToDB(){
		Session currentSession = Sessions.getCurrent();

		if( databaseConnection == null){//btnConnectionToDB.getLabel().equalsIgnoreCase("Connect")){

			databaseConnection = new CDatabaseConnection();	


			CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig();

			//En esta línea obtenemos la ruta completa del archivo de configuración incluido el /config/
			String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator + SystemConstants._Config_Dir + File.separator;

			if ( databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Database_Connection_Config_File_Name) ) {

				if ( databaseConnection.makeConnectionToDatabase( databaseConnectionConfig ) ) {

					//Salvamos la conexión a la sesión actual del usuario, cada usuario/ pestaña tiene su sesión
					currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); //La sesion no es más que un arreglo asociativo


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
	}

	public void refresh( ) {

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

	@Listen("onClose=#listBoxPeliculas")
	public void terminarConexión(Event e){
		Session currentSession = Sessions.getCurrent();
		if(databaseConnection != null){
			if(databaseConnection.closeConnectionToDatabase()){
				databaseConnection = null;

				currentSession.removeAttribute(SystemConstants._DB_Connection_Session_Key);//borra la variable de sesión
				System.out.println("Conexión cerrada");
			}
		}
	}
	
	@Listen("onClick=#btnLogin")
	public void onClickbtnLogin(Event e){
		Sessions.getCurrent().invalidate();
		Executions.sendRedirect( "/views/home/home.zul" );
		System.out.println("Conexión cerrada");
	}
	
	@Listen( "onDialogFinished=#listboxPeliculas" ) //Notifica que se agrego o modifico una entidad y que debe refrescar la lista el modelo
	public void onDialogFinishedlistboxPeliculas( Event event ) {

		//En zk es posible lanzar eventos no tan solo definidos por nosotros mismo si no tambien los estandares de zk como el click

		//Forzamos refrescar la lista 

		Events.echoEvent( new Event( "onClick") );  //Lanzamos el evento click de zk


	}

}
