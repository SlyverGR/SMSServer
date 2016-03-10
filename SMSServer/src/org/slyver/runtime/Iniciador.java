/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.slyver.runtime;

/**
 *
 * @author Andres
 */
public class Iniciador {
    	public static void main(String args[])
	{	
           //SendMessage app = new SendMessage();
		//try
	//	{
          //          app.doIt();
	//	}
	//	catch (Exception e)
	//	{
         //           e.printStackTrace();
	//	}
                
            SMSListener appR = new SMSListener();
		try
		{
                    appR.doIt();
		}
		catch (Exception e)
		{
                    e.printStackTrace();
		}
	}
        
        
}
