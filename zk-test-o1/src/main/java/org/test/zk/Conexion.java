package org.test.zk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

	Connection cn;
	Statement st;
	ResultSet rs;
	
	public Conexion() {
		try {
			Class.forName("org.h2.Driver");
			cn = DriverManager.getConnection("jdbc:h2:C:/Users/andre/workspace1/workspace2/zk-test-o1/lib2/movieshop","sa","");
			st = cn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	void probar() {
		try {
			rs = st.executeQuery("SELECT * FROM peliculas");
			while(rs.next()) {
				System.out.println("ID: "+rs.getInt("rut"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	void cerrar() {
		try {
			cn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
