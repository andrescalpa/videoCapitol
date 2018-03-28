package org.test.zk.database.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.test.zk.database.CDatabaseConnection;
import org.test.zk.database.datamodel.TBLPeliculas;

public class PeliculasDAO{

	public static TBLPeliculas loadData(final CDatabaseConnection databaseConnection, final String strId){

		TBLPeliculas result = null;

		try{
			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				ResultSet resultSet = statement.executeQuery( "Select * From peliculas Where Id = '" + strId + "'" );

				if ( resultSet.next() ) {

					result = new TBLPeliculas();

					result.setStrId( resultSet.getString( "id" ) );
					result.setStrTitulo( resultSet.getString( "titulo" ) );
					result.setStrDescripcion( resultSet.getString( "descripcion" ) );
					result.setStrDirector( resultSet.getString( "director" ) );
					result.setStrActores( resultSet.getString( "actores" ) );
					result.setStrCantidad( resultSet.getString( "cantidad_inv" ) );

					//Los siguientes métodos setCreatedBy vienen de la clase CAuditableDataModel
					result.setCreatedBy( resultSet.getString( "CreatedBy" ) ); 
					result.setCreatedAtDate( resultSet.getDate( "CreatedAtDate" ).toLocalDate() ); 
					result.setCreatedAtTime( resultSet.getTime( "CreatedAtTime" ).toLocalTime() ); 
					result.setUpdatedBy( resultSet.getString( "UpdatedBy" ) ); //Puede ser null, pero no es relevante por que no accedo a metodos de la clase String 
					result.setUpdatedAtDate( resultSet.getDate( "UpdatedAtDate" ) != null ? resultSet.getDate( "UpdatedAtDate" ).toLocalDate() : null ); //Puede ser un null 
					result.setUpdatedAtTime( resultSet.getTime( "UpdatedAtTime" ) != null ? resultSet.getTime( "UpdatedAtTime" ).toLocalTime() : null );  //Pueder ser null de la bd

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

	public static boolean deleteData(final CDatabaseConnection databaseConnection, final String strId){
		boolean bResult = false;
		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				final String strSQL = "Delete from peliculas Where Id = '" + strId + "'";

				//Esta es la parte fastidiosa de no usar un ORM
				statement.executeUpdate( strSQL );

				databaseConnection.getDbConnection().commit();//Commit de la trasacción

				statement.close();

				bResult = true;

			}    

		}
		catch ( Exception ex ) {

			if(databaseConnection != null && databaseConnection.getDbConnection() != null){

				try{
					databaseConnection.getDbConnection().rollback();//Hace rollback por si alguna transacción falla
				}
				catch(Exception e){

					e.printStackTrace();

				}

			}

			ex.printStackTrace();

		}

		return bResult;
	}

	public static boolean insertData(final CDatabaseConnection databaseConnection, final TBLPeliculas tblPelicula){
		boolean bResult = false;
		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				final String strSQL = "Insert Into peliculas(id,titulo,descripcion,actores,director,cantidad_inv,CreatedBy,CreatedAtDate,CreatedAtTime,UpdatedBy,UpdatedAtDate,UpdatedAtTime) Values('" + tblPelicula.getStrId() + "','" + tblPelicula.getStrTitulo() + "','" + tblPelicula.getStrDescripcion() + "','" + tblPelicula.getStrActores() + "','" + tblPelicula.getStrDirector() + "','" + tblPelicula.getStrCantidad() + "','admin','" + LocalDate.now().toString() + "','" + LocalTime.now().toString() + "','admin" + "',null,null )";

				//Esta es la parte fastidiosa de no usar un ORM
				statement.executeUpdate( strSQL );

				databaseConnection.getDbConnection().commit(); //Commit la transacción

				statement.close();//se cierra la conexión para liberar recursos de memoria

				bResult = true;

			}    

		}
		catch ( Exception ex ) {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				try {

					databaseConnection.getDbConnection().rollback(); //En caso de error rollback todas las operaciones anteriores.

				}
				catch ( Exception e ) {

					e.printStackTrace(); //Podemos tenes problemas en el rollback nos exige un try catch

				} 

			}    
			ex.printStackTrace();

		}

		return bResult;
	}

	public static boolean updateData(final CDatabaseConnection databaseConnection, final TBLPeliculas tblPelicula){
		boolean bResult = false;

		try {

			if ( databaseConnection != null && databaseConnection.getDbConnection() != null ) {

				Statement statement = databaseConnection.getDbConnection().createStatement();

				//
				final String strSQL = "Update peliculas Set id='" + tblPelicula.getStrId() + "', titulo = '" + tblPelicula.getStrTitulo() + "',descripcion = '" + tblPelicula.getStrDescripcion() + "', director = '" + tblPelicula.getStrDirector() + "',actores = '" + tblPelicula.getStrActores() + "', cantidad_inv = '" + tblPelicula.getStrCantidad() + "',UpdatedBy = 'Admin', UpdatedAtDate = '" + LocalDate.now().toString() + "',UpdatedAtTime = '" + LocalTime.now().toString() + "' Where Id = '" + tblPelicula.getStrId() + "'";

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

					e.printStackTrace(); //Podemos tenes problemas en el rollback nos exige un try catch

				} 

			}    

			ex.printStackTrace();

		}
		return bResult;
	}

	public static List<TBLPeliculas> searchData(final CDatabaseConnection databaseConnection){
		List<TBLPeliculas> result = new ArrayList<TBLPeliculas>();

		try{
			if(databaseConnection != null && databaseConnection.getDbConnection() != null){

				Statement stmt = databaseConnection.getDbConnection().createStatement();

				ResultSet rs = stmt.executeQuery("Select * from peliculas");
				while(rs.next()){

					TBLPeliculas tblPeliculas = new TBLPeliculas();

					tblPeliculas.setStrId(rs.getString("id"));
					tblPeliculas.setStrTitulo(rs.getString("titulo"));
					tblPeliculas.setStrDescripcion(rs.getString("descripcion"));
					tblPeliculas.setStrDirector(rs.getString("director"));
					tblPeliculas.setStrActores(rs.getString("actores"));
					tblPeliculas.setStrCantidad(rs.getString("cantidad_inv"));

					//Los siguientes métodos setCreatedBy bienen de la clase CAuditableDataModel
					tblPeliculas.setCreatedBy( rs.getString( "CreatedBy" ) ); 
					tblPeliculas.setCreatedAtDate( rs.getDate( "CreatedAtDate" ).toLocalDate() ); 
					tblPeliculas.setCreatedAtTime( rs.getTime( "CreatedAtTime" ).toLocalTime() ); 
					tblPeliculas.setUpdatedBy( rs.getString( "UpdatedBy" ) ); //Puede ser null, pero no es relevante por que no accedo a metodos de la clase String 
					tblPeliculas.setUpdatedAtDate( rs.getDate( "UpdatedAtDate" ) != null ? rs.getDate( "UpdatedAtDate" ).toLocalDate() : null ); //Puede ser un null 
					tblPeliculas.setUpdatedAtTime( rs.getTime( "UpdatedAtTime" ) != null ? rs.getTime( "UpdatedAtTime" ).toLocalTime() : null );  //Pueder ser null de la bd

					result.add(tblPeliculas);//Agregarlo a la lista de resultado

				}
				//Cuando se termina debemos cerrar los recursos
				rs.close();
				stmt.close();

				//NO Cerrarmos la conexión la mantenemos abierta para usarla en otras operaciones

			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

}
