package org.test.zk.security;

import java.io.File;
import java.util.Map;

import org.test.zk.constants.SystemConstants;
import org.test.zk.database.datamodel.TBLOperator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;


public class CCheckAuthenticated implements Initiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {
		// TODO Auto-generated method stub
		detectAuthenticatedAndRedirect( page, args );

	}

	@SuppressWarnings("unused")
	public static void detectAuthenticatedAndRedirect( Page page, Map<String, Object> args ) {

		//Ok aqui creamos otro logger distinto al global esto con la finalidad de no sobrecargar el archivo con demasiadas entradas
		//CExtendedLogger extendedLogger = CExtendedLogger.getLogger( ConstantsCommonClasses._Check_Logged_Logger_Name );

		String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator;

		//Aquí por primera vez inicializamos un CLanguage que es para tener múltiples languajes en los archivos de bitacora (Logs) deben estar dentro del path WEB-INF/langs/security/checklogged.init.lang  pero si no existe no es problema se usa ingles para todo
		//CLanguage languaje = CLanguage.getLanguage( extendedLogger, strRunningPath + ConstantsCommonClasses._Langs_Dir + ConstantsCommonClasses._Security_Dir + ConstantsCommonClasses._Check_Logged_Logger_Name + "." + ConstantsCommonClasses._Lang_Ext ); 

		Session currentSession = Sessions.getCurrent();

		//¿recuerdan que salvamos las credenciales del operador cuando este hace login correctamente?
		//Aqui tratamos de recuperarlo, si es null es que no hay un login válido todavía
		TBLOperator tblOperator = (TBLOperator) currentSession.getAttribute( SystemConstants._Operator_Credential_Session_Key );

		//Aqui extraemos el request path que se coloca en el navegador
		String strRequestPath = Executions.getCurrent().getDesktop().getRequestPath();

		if ( tblOperator != null ) {

			//Para ser usado en el arhivo de log 
			String strLoginDateTime = (String) currentSession.getAttribute( SystemConstants._Login_Date_Time_Session_Key );

		}

		//Ok aquí viene la parte interesante
		//Si el tblOperator de la sesion es NULL es que no hay un operador con login válido
		if ( tblOperator == null ) {

			String strRedirectPath = "/views/home/home.zul"; //Lo mandamos para el home.zul que contiene el login.zul

			//Que no sean iguales el uri pedido por el navegador y el que intento redireccionar, con eso paramos el bucle
			if ( strRequestPath.isEmpty() == false && strRedirectPath.equalsIgnoreCase( strRequestPath ) == false ) { 

				Executions.sendRedirect( strRedirectPath ); //Lo enviamos al home.zul

			}

		}
		//Con el condicional anterior establecimos que no es un usuario anonimo que tiene login
		//Sin embargo tampoco debe acceder diectamente por uri a ningún .zul como el manager.zul o el editor.zul
		//Entonces preguantemos si el uri que pide por el navegador es vacio osea o pide un archivo como manager.zul o editor.zul
		//Lo reenviamos para el home.zul
		else if (tblOperator.getStrRole().equalsIgnoreCase("admin")){
			if(strRequestPath.isEmpty()|| strRequestPath.contains("/views/peliculas/manager/peliculasCliente.zul")==true){
				String strRedirectPath = "/views/peliculas/editor/homeAdmin.zul";
				Executions.sendRedirect( strRedirectPath );
			}
			
		}
		
		else if ( strRequestPath.isEmpty() || strRequestPath.contains( "/homeClientes.zul" ) == false ) {

			String strRedirectPath = "/views/home/homeClientes.zul"; //Si no hay request path 

			Executions.sendRedirect( strRedirectPath ); //Lo redireccionamos al home.zul por que tiene un login váĺido

		}

	
	
	}
}
