/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.slyver.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Andres
 */
public class database {
    public static Connection dbcon=null;
    public static Statement statement;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    public static String sql;
    
    private final String url = "jdbc:sqlserver://";
    private final String serverName= "mail.gruamazonas.com";
    private final String portNumber = "1433";
    private final String databaseName= "SMSApp";
    private final String userName = "sa";
    private final String password = "P@n@2013";
    
    private final String selectMethod = "cursor"; 
    
    public database() throws SQLException, ClassNotFoundException {

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
        dbcon = java.sql.DriverManager.getConnection(getConnectionUrl(),userName,password);
   
    }
     
     private String getConnectionUrl(){
          return url+serverName+":"+portNumber+";databaseName="+databaseName+";selectMethod="+selectMethod+";";
     }
     
     public String[][] getData() throws SQLException, ClassNotFoundException{
        
        sql = "SELECT ID, MOVIL, MENSAJE FROM SMSQUEUE WHERE ESTADO = 'PENDIENTE'"; 
        statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = statement.executeQuery(sql);       
        
        int sizeH = 0;
        int i = 0;
        
        rs.last();
        sizeH = rs.getRow();
        rs.beforeFirst();
        
        String[][] msjs = new String[sizeH][3];
        
        if(sizeH>0){ 

            while(rs.next()){

                msjs[i][0] = rs.getString(1);
                msjs[i][1] = rs.getString(2);
                msjs[i][2] = rs.getString(3);

                i++;
            }
        }else{
         msjs = new String[1][1];
          msjs[0][0] = "NULL";
        }
        //System.out.println(msjs[0][0]);
        return msjs;
        
    }
     
    public void setEstado(int id, String response) throws SQLException{
        
        sql = "UPDATE SMSQUEUE SET ESTADO = '"+response+"'  WHERE id = "+id+""; 
        statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement.executeUpdate(sql);
        
    }
    
}
