package org.test.zk.constants;

import java.io.File;

public class SystemConstants {

	public static final String _Libs_Dir = "libs" + File.separator;
	public static final String _Lib_Ext = ".jar";

	public static final String _Langs_Dir = "langs" + File.separator;
	public static final String _Lang_Ext = "init.lang";
	public static final String _Common_Lang_File = "Common.init.lang";

	public static final String _Logs_Dir = "logs" + File.separator;

	public static final String _Temp_Dir = "temp" + File.separator;

	public static final String _Security_Dir = "security" + File.separator;

	public static final String _System_Dir = "system" + File.separator;
	public static final String _WEB_INF_Dir = "WEB-INF";// + File.separator;
	public static final String _Config_Dir = "config" + File.separator;

	public static final String _Database_Connection_Config_File_Name = "database.config.xml";

	public static final String _Database_Connection_Production_Config_File_Name = "database.production.config.xml";
	public static final String _Logger_Config_File_Name = "logger.config.xml";

	public static final String _Logger_Config_Production_File_Name = "logger.production.config.xml";

	//Constante para control de login de cliente
	public static final String _Operator_Unknown = "unknown@unknown.com";

	public static final String _DB_Connection_Session_Key = "dbConnection";
	public static final String _Operator_Credential_Session_Key = "operatorCredential";
	public static final String _Login_Date_Time_Session_Key = "loginDateTime";
	public static final String _Log_Path_Session_Key = "logPath";

	public static final String _Check_Logged_Logger_Name = "checklogged";
	public static final String _Check_Logged_File_Log = "checklogged.log";

	public static final String _Log_Class_Method = "*.*";
	public static final boolean _Log_Exact_Match = false;
	public static final boolean _Log_Missing_Translations = false;
	public static final String _Logger_Name_Missing_Translations = "MissingTranslations";
	public static final String _Missing_Translations_File_Log = _Logger_Name_Missing_Translations + ".log";
	public static final String _Log_Level = "ALL";

	//Constantes para el control de login de invitados
	public static final String _Home_Controller_Logger_Name = "home_controller";
	public static final String _Home_Controller_File_Log = "home_controller.log";
	public static final String _Logged_Session_Loggers = "loggedSessionLoggers";
	
	
	public static final String _Person_Manager_Controller_Logger_Name = "person_manager_controller";
	public static final String _Person_Manager_Controller_File_Log = "person_mnager_controller.log";
	
	public static final String _Person_Editor_Controller_Logger_Name = "person_editor_controller";
	public static final String _Person_Editor_Controller_File_Log = "person_editor_controller.log";
	
	
}

