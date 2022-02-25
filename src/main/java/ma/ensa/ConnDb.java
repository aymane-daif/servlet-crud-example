package ma.ensa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnDb {
	 	public static String URL = "jdbc:mysql://localhost:3306/forum?serverTimezone=UTC";
	    public static String USERNAME = "root";
	    public static String PASSWORD = "root";
	    public static String DRIVER = "com.mysql.cj.jdbc.Driver";
	    
	    private Connection conn;
	    private Statement st;
	    
	    public ConnDb(){
	        try{
	        	Class.forName(DRIVER);
	        	conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

	            st = conn.createStatement();	
	        	String createTableSt = "CREATE TABLE IF NOT EXISTS user (id INT NOT NULL AUTO_INCREMENT, nom VARCHAR(255), prenom VARCHAR(255), tel VARCHAR(20),userId VARCHAR(255) UNIQUE, PRIMARY KEY ( id ));";
	            st.executeUpdate(createTableSt);
	            
	            System.out.println("Table created...");
	        } catch (ClassNotFoundException | SQLException e) {
	        	System.out.println("connection failed...");
	            e.printStackTrace();
	        }
	    }

	    public void close(){
	        try {
	            conn.close();
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    public Statement getSt(){
	        return st;
	    }
	    public Connection getConn(){
	        return conn;
	    }
}
