package org.test.zk.controllers.home;

import java.io.File;

import org.test.zk.constants.SystemConstants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Messagebox;

public class CHomeController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1040032932048818844L;


		
	public void doAfterCompose( Component comp ) {

		try {

			super.doAfterCompose( comp );

			@SuppressWarnings("unused")
			String strRunningPath = Sessions.getCurrent().getWebApp().getRealPath( SystemConstants._WEB_INF_Dir ) + File.separator;
		}
		catch ( Exception ex ) {

			ex.printStackTrace();
		}


	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen( "onClick = #includeNorthContent #buttonLogout" )  
	public void onClickbuttonLogout( Event event ) {

		Messagebox.show( "¿Está Seguro qué desea salir?", "Logout", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {

			public void onEvent(Event evt) throws InterruptedException {

				if ( evt.getName().equals( "onOK" ) ) {

					//Ok aquí vamos hacer el logout
					Sessions.getCurrent().invalidate(); //Listo obliga limpiar la sessión mejor que ir removeAttribute a removeAttribute

					Executions.sendRedirect( "/views/home/home.zul"); //Lo enviamos al login

				} 

			}

		});     

	}

}
