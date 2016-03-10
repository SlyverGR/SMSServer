/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.slyver.runtime;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slyver.db.database;

/**
 *
 * @author Andres
 */
public class Test {
    
    public static void main(String args[]) throws SQLException
	{
        try {
            database msjs = new database();
            String dateF = "03/10/2016";
            System.out.println(dateF);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
            System.out.println(formatter);
            LocalDate datesms = LocalDate.parse(dateF, formatter);
            System.out.println(datesms);
            msjs.setData(593985810300L, "Prueba", datesms);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
    
    
}
