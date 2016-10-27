package com.cooksys.ftd.assignments.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cooksys.ftd.assignments.jdbc.model.City;
import com.cooksys.ftd.assignments.jdbc.model.Interest;
import com.cooksys.ftd.assignments.jdbc.model.State;

public class Main {
	
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres?currentSchema=\"Cuttlefern\"";

	static final String USER = "postgres";
	static final String PASS = "bondstone";
	
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
        	Class.forName(JDBC_DRIVER);
        	
        	System.out.println("Connecting to database...");
        	conn = DriverManager.getConnection(DB_URL, USER, PASS);
        	
        	System.out.println("Creating statement...");
        	stmt = conn.createStatement();
        	String sql;
        	sql = "SELECT id, first_name, last_name FROM people";
        	ResultSet rs = stmt.executeQuery(sql);
        	
        	while(rs.next()) {
        		int id = rs.getInt("id");
        		String first = rs.getString("first_name");
        		String last = rs.getString("last_name");
        		
        		System.out.println("ID: " + id);
        		System.out.println("First Name: " + first);
        		System.out.println("Last Name: " + last);
        	}
        	rs.close();
        	stmt.close();
        	conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }finally{
             //finally block used to close resources
             try{
                if(stmt!=null)
                   stmt.close();
             }catch(SQLException se2){
             }// nothing we can do
             try{
                if(conn!=null)
                   conn.close();
             }catch(SQLException se){
                se.printStackTrace();
             }//end finally try
          }//end try
          System.out.println("Goodbye!");
       }//end main
}//end FirstExample
