package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.datamodel.TBLReservas;

public class ReservasDAO {

	public static TBLReservas loadData( final CDatabaseConnection databaseConnection, final String strId ) {

		TBLReservas result = null;

		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				ResultSet resultSet = statement.executeQuery( "Select * From reservas Where id_operator = '" + strId + "'" );

				if ( resultSet.next() ) {

					result = new TBLReservas();

					result.setStrIdOperador( resultSet.getString( "id_operator" ) );
					result.setStrNombreOperador( resultSet.getString( "nombre_operator" ) );
					result.setStrIdPelicula( resultSet.getString( "id_pelicula" ) );
					result.setStrTituloPelicula( resultSet.getString( "nombre_pelicula" ) );

				}    

				//Cuando se termina debemos cerrar los recursos
				resultSet.close();
				statement.close();

				//NO Cerrarmos la conexión la mantenemos abierta para usarla en otras operaciones

			}

		}
		catch ( Exception ex ) {

			ex.printStackTrace();        

		}

		return result;    

	}

	public static boolean deleteData( final CDatabaseConnection databaseConnection, final String strId) {

		boolean bResult = false; 

		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				final String strSQL = "Delete From reservas Where id_operator = '" + strId + "'";

				//Esta es la parte fastidiosa de no usar un ORM
				statement.executeUpdate( strSQL );

				databaseConnection.getDbConnection().commit(); //Commit la transacción

				statement.close();

				bResult = true;

			}    

		}
		catch ( Exception ex ) {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				try {

					databaseConnection.getDbConnection().rollback(); //En caso de error rollback todas las operaciones anteriores.

				}
				catch ( Exception e ) {

					e.printStackTrace();       

				} 
			}    
		}

		return bResult;

	}    

	public static boolean instertData( final CDatabaseConnection databaseConnection, final TBLReservas tblReservas) {

		boolean bResult = false; 

		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				final String strSQL = "Insert Into reservas(id_operator, nombre_operator, id_pelicula,titulo_pelicula) Values('" + tblReservas.getStrIdOperador() + "','" + tblReservas.getStrNombreOperador() + "','" + tblReservas.getStrIdPelicula() + "','" + tblReservas.getStrTituloPelicula() + "')";


				statement.executeUpdate( strSQL );

				databaseConnection.getDbConnection().commit(); //Commit la transacción

				statement.close(); //Cerrar y liberar recursos

				bResult = true;

			}    

		}
		catch ( Exception ex ) {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				try {

					databaseConnection.getDbConnection().rollback(); //En caso de error rollback todas las operaciones anteriores.

				}
				catch ( Exception e ) {

					e.printStackTrace();

				} 
			}    
		}

		return bResult;

	}

	public static boolean updateData( final CDatabaseConnection databaseConnection, final TBLReservas tblReservas) {

		boolean bResult = false; 

		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				//Esto es un dolor de cabeza sin ORM como hibernate o mybatis
				final String strSQL = "Update reservas Set id_operator='" + tblReservas.getStrIdOperador() + "', nombre_operator = '" + tblReservas.getStrNombreOperador() + "', id_pelicula = '" + tblReservas.getStrIdPelicula() + "', titulo_pelicula = '" + tblReservas.getStrTituloPelicula() + "' Where Id = '" + tblReservas.getStrIdOperador() + "'";

				//Esta es la parte fastidiosa de no usar un ORM
				statement.executeUpdate( strSQL );

				databaseConnection.getDbConnection().commit(); //Commit la transacción

				statement.close(); //Cerrar y liberar recursos

				bResult = true;

			}    

		}
		catch ( Exception ex ) {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {
				try {

					databaseConnection.getDbConnection().rollback(); //En caso de error rollback todas las operaciones anteriores.

				}
				catch ( Exception e ) {

					e.printStackTrace();

				} 
			}    
		}

		return bResult;

	}

	public static List<TBLReservas> searchData( final CDatabaseConnection databaseConnection) {

		List<TBLReservas> result = new ArrayList<TBLReservas>(); 

		return result;

	}

}
