package org.test.zk.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class CDatabaseConnection {

	protected Connection dbConnection;
	protected CDatabaseConnectionConfig dbConnectionConfig;

	public CDatabaseConnection() {

		dbConnection = null;

		dbConnectionConfig = null;

	}

	public Connection getDbConnection() {
		return dbConnection;
	}


	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}



	public CDatabaseConnectionConfig getDbConnectionConfig() {
		return dbConnectionConfig;
	}

	public void setDbConnectionConfig(CDatabaseConnectionConfig dbConnectionConfig) {
		this.dbConnectionConfig = dbConnectionConfig;
	}

	//meotodo para hacer la conexión con la DB
	public boolean makeConnectionToDatabase(CDatabaseConnectionConfig localDBConnectionConfig){
		boolean bResult=false;

		try{
			if ( this.dbConnection == null ) {

				Class.forName( localDBConnectionConfig.strDriver );

				String strDatabaseURL = localDBConnectionConfig.strPrefix + localDBConnectionConfig.strHost + "/" + localDBConnectionConfig.strDatabase;

				Connection localDBConnection = DriverManager.getConnection( strDatabaseURL, localDBConnectionConfig.strUser, localDBConnectionConfig.strPassword );
				//DBConnection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/" + strDatabase, strDBUserName, strDBPassword );

				localDBConnection.setTransactionIsolation( Connection.TRANSACTION_READ_COMMITTED );


				bResult = localDBConnection != null && localDBConnection.isValid( 5 );

				if ( bResult ) {

					localDBConnection.setAutoCommit( false );


					this.dbConnection = localDBConnection; //Save the database connection to this instance object

					this.dbConnectionConfig = localDBConnectionConfig; //Save the config for the connection to this instance object


				}    
				else {

					localDBConnection.close();

					localDBConnection = null;
				}   
			}
			else {

				System.out.println("The database is already initiated");

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return bResult;
	}

	public boolean closeConnectionToDatabase(){
		boolean bResult = false;
		try{
			if(dbConnection != null){
				dbConnection.close();
				System.out.println("Database connection closed successfully");
			}
			else{
				System.out.println("The connection variable is null");
			}
			dbConnection = null;
			dbConnectionConfig = null;

			System.out.println("Set to null connection and conection config variable");


			bResult = true;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return bResult;
	}

	public boolean isValid() {

		boolean bResult = false;

		try {

			System.out.println("Checking for database connection is valid");

			bResult = dbConnection.isValid( 5 ); //wait max for 5 seconds

		} 
		catch ( Exception Ex ) {

			Ex.printStackTrace();

		}

		return bResult;

	}
}
