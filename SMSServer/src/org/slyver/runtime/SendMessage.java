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
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slyver.db.database;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class SendMessage
{
        int IndexCounter = 0;
        static SerialModemGateway gateway;
        String usbcom;
        String usbmark;
        String usbmodel;
        OutboundMessage msg;
        
	public void doIt() throws Exception
	{
		OutboundNotification outboundNotification = new OutboundNotification();
                getProperties();
		//System.out.println("Example: Send message from a serial gsm modem.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("Version: " + Library.getLibraryVersion());
		gateway = new SerialModemGateway("modem."+usbcom, usbcom, 115200, usbmark, usbmodel);
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
		// Explicit SMSC address set is required for some modems.
		// Below is for VODAFONE GREECE - be sure to set your own!
		//gateway.setSmscNumber("+306942190000");
		Service.getInstance().setOutboundMessageNotification(outboundNotification);
		Service.getInstance().addGateway(gateway);
		Service.getInstance().startService();
		System.out.println();
		System.out.println("Modem Information:");
		System.out.println("  Manufacturer: " + gateway.getManufacturer());
		System.out.println("  Model: " + gateway.getModel());
		System.out.println("  Serial No: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
		System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
		System.out.println();
		// Send a message synchronously
                
                ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                exec.scheduleAtFixedRate(new Runnable() {
              @Override
              public void run() {
                  try {
                      database msjs = new database();
                      
                      if(!"NULL".equals(msjs.getData()[0][0])){
                          
                          //System.out.println(msjs.getData()[0][1]+","+ msjs.getData()[0][2]);
                          
                          msg = new OutboundMessage(msjs.getData()[0][1].trim(), msjs.getData()[0][2].trim());
                          
                          msg.setStatusReport(true);
                          Service.getInstance().sendMessage(msg);
                          System.out.println(msg);
                          String smscode = msg.getMessageStatus().toString();
                          
                          msjs.setEstado(Integer.parseInt(msjs.getData()[0][0].trim()),smscode);
                          
                      }else{
                          System.out.println("No hay mensajes pendientes");
                          //Service.getInstance().stopService();
                      } } catch (SQLException | ClassNotFoundException | TimeoutException | GatewayException | IOException | InterruptedException ex) {
                      Logger.getLogger(SendMessage.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }, 0, 10, TimeUnit.SECONDS);

                		
		// Or, send out a WAP SI message.
		//OutboundWapSIMessage wapMsg = new OutboundWapSIMessage("306974000000",  new URL("http://www.smslib.org/"), "Visit SMSLib now!");
		//Service.getInstance().sendMessage(wapMsg);
		//System.out.println(wapMsg);
		// You can also queue some asynchronous messages to see how the callbacks
		// are called...
		//msg = new OutboundMessage("309999999999", "Wrong number!");
		//srv.queueMessage(msg, gateway.getGatewayId());
		//msg = new OutboundMessage("308888888888", "Wrong number!");
		//srv.queueMessage(msg, gateway.getGatewayId());
		//System.out.println("Now Sleeping - Hit <enter> to terminate.");
		//System.in.read();
		//Service.getInstance().stopService();
	}

	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);

		}
	}

        public void getProperties(){
            Properties prop = new Properties();

            try {
                   //load a properties file
                    InputStream in = getClass().getResourceAsStream("usb.properties");
                    prop.load(in);

                   //get the property value and print it out
                    usbcom = prop.getProperty("usbcom");
                    usbmark = prop.getProperty("usbmark");
                    usbmodel = prop.getProperty("usbmodel");

            } catch (IOException ex) {
                    ex.printStackTrace();
            }
        }
        


        
}