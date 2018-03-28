package org.test.zk.controllers.login;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.test.zk.constants.SystemConstants;
import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.CDatabaseConnectionConfig;
import org.test.zk.database.dao.OperatorDAO;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import commonlibs.utils.ZKUtilities;

public class CLoginController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 2607061613647188753L;

	@Wire
	Textbox textboxUserName;

	@Wire
	Textbox textboxPassword;

	@Wire
	Label labelMessage;


	@Override
	public void doAfterCompose( Component comp ) {

		try {
			super.doAfterCompose(comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}   

	@Listen( "onChanging=#textboxUserName; onChanging=#textboxPassword" )
	public void onChangeTextbox( Event event ) {

		//A si distinguimos cual componente en pantalla lanzo el evento a veces es útil para hacer una cosa u otra en el método
		if ( event.getTarget().equals( textboxUserName ) ) {

			System.out.println( "Textbox operator" );

		}
		else if ( event.getTarget().equals( textboxPassword ) ) {

			System.out.println( "Textbox password" );

		}

		labelMessage.setValue( "" ); //Cuando cambie el texto en cualquiera de los dos textbox quitamos el mensaje. de error.

	} 

	@Listen( "onClick=#buttonLogin; onOK=#windowLogin" )
	public void onClickButtonLogin( Event event ) {

		try {

			//Aquí vamos a conectarnos a la bd y vamos a verificar si los valores introducidos son válidos

			final String strOperator = ZKUtilities.getTextBoxValue( textboxUserName);
			final String strPassword = ZKUtilities.getTextBoxValue( textboxPassword);

			if ( strOperator.isEmpty() == false && strPassword.isEmpty() == false ) {

				CDatabaseConnection databaseConnection = new CDatabaseConnection();

				CDatabaseConnectionConfig databaseConnectionConfig = new CDatabaseConnectionConfig();

				//En esta línea obtenemos la ruta completa del archivo de configuración incluido el /config/
				String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator;

				if ( databaseConnectionConfig.loadConfig( strRunningPath + SystemConstants._Config_Dir +SystemConstants._Database_Connection_Config_File_Name) ) {

					if ( databaseConnection.makeConnectionToDatabase(databaseConnectionConfig)) {

						TBLOperator tblOperator = OperatorDAO.checkValid( databaseConnection, strOperator, strPassword );

						if ( tblOperator != null ) {

							//Si es válido guardamos la conexión creada en la sesioón y guardamos la identidad del operador en la sesión

							labelMessage.setSclass( "" ); 
							labelMessage.setValue( "Bienvenido " + tblOperator.getName() + "!" ); 

							Session currentSession = Sessions.getCurrent();

							//Salvamos la conexión a la sesión actual del usuario, cada usuario/ pestaña tiene su sesión
							currentSession.setAttribute( SystemConstants._DB_Connection_Session_Key, databaseConnection ); //La sesion no es más que un arreglo asociativo

							//Salvamos la entidad del operador en la sesion
							currentSession.setAttribute( SystemConstants._Operator_Credential_Session_Key, tblOperator );

							//Creamos la variable del logpath
							String strLogPath = strRunningPath + SystemConstants._Logs_Dir + strOperator + File.separator + File.separator;

							//La guardamos en la sesion
							currentSession.setAttribute( SystemConstants._Log_Path_Session_Key, strLogPath );

							//Guardamos la fecha y la hora del inicio de sesión
							currentSession.setAttribute( SystemConstants._Login_Date_Time_Session_Key, strLogPath);

							//Creamos la lista de logger de esta sesion
							List<String> loggedSessionLoggers = new LinkedList<String>();

							//Guardamos la lista vacia en la sesion
							currentSession.setAttribute( SystemConstants._Logged_Session_Loggers, loggedSessionLoggers );

							//Actualizamos en bd el último inicio de sesión
							OperatorDAO.updateLastLogin( databaseConnection, tblOperator.getId());                            
							Messagebox.show( "Connection ok" );
							//Redirecionamos hacia el home.zul
							Executions.sendRedirect( "/views/peliculas/manager/peliculasCliente.zul" ); 

						}
						else {

							labelMessage.setValue( "Usuario o contraseña invalida" );

							//Messagebox.show( "Invalid operator name and/or password" );

						}

					}
					else {

						Messagebox.show( "Connection failed" );
						Messagebox.show( "Database connection failed" );

					}

				}
				else {

					Messagebox.show( "Error to read the config file" );
					Messagebox.show( "Error to read the database config file" );

				}

			}

		}
		catch ( Exception ex ) {

			ex.printStackTrace();        

		}

	}    

	@Listen("onTimer=#timerKeepAliveSession" )
	public void onTimer( Event event ) {
		//Este evento se ejecutara cada 120000 milisegundos 1 minuto aprox, esto no es un relog atomico a si que son tiempos aproximados
		//Muestra un tablerito en la parte superior central de la pantalla
		Clients.showNotification( "Automatic renewal of the session successful", "info",  null, "before_center", 2000, true );

	}
}
