// This application shows you the basic procedure needed for reading
// SMS messages from your GSM modem, in synchronous mode.
//
// Operation description:
// The application setup the necessary objects and connects to the phone.
// As a first step, it reads all messages found in the phone.
// Then, it goes to sleep, allowing the asynchronous callback handlers to
// be called. Furthermore, for callback demonstration purposes, it responds
// to each received message with a "Got It!" reply.
//
// Tasks:
// 1) Setup Service object.
// 2) Setup one or more Gateway objects.
// 3) Attach Gateway objects to Service object.
// 4) Setup callback notifications.
// 5) Run

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Library;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

public class SMSListener
{
        //private static org.apache.log4j.Logger log= Logger.getLogger(SMSListener.class);
        
        int IndexCounter = 0;
        static SerialModemGateway gateway;
        String usbcom;
        String usbmark;
        String usbmodel;
    
	public void doIt() throws Exception
	{
                
            // Define a list which will hold the read messages.
		List<InboundMessage> msgList;
		// Create the notification callback method for inbound & status report
		// messages.
		InboundNotification inboundNotification = new InboundNotification();
		// Create the notification callback method for inbound voice calls.
		CallNotification callNotification = new CallNotification();
		//Create the notification callback method for gateway statuses.
		GatewayStatusNotification statusNotification = new GatewayStatusNotification();
		OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
		try
		{
			//System.out.println("Example: Read messages from a serial gsm modem.");
			System.out.println(Library.getLibraryDescription());
			System.out.println("Version: " + Library.getLibraryVersion());
                        getProperties();
			// Create the Gateway representing the serial GSM modem.
			gateway = new SerialModemGateway("modem."+usbcom, usbcom, 115200, usbmark, usbmodel);
			// Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
			gateway.setProtocol(Protocols.PDU);
			// Do we want the Gateway to be used for Inbound messages?
			gateway.setInbound(true);
			// Do we want the Gateway to be used for Outbound messages?
			gateway.setOutbound(true);
			// Let SMSLib know which is the SIM PIN.
			gateway.setSimPin("0000");
			// Set up the notification methods.
			Service.getInstance().setInboundMessageNotification(inboundNotification);
			Service.getInstance().setCallNotification(callNotification);
			Service.getInstance().setGatewayStatusNotification(statusNotification);
			Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
			// Add the Gateway to the Service object.
			Service.getInstance().addGateway(gateway);
			// Similarly, you may define as many Gateway objects, representing
			// various GSM modems, add them in the Service object and control all of them.
			// Start! (i.e. connect to all defined Gateways)
			Service.getInstance().startService();
			// Printout some general information about the modem.
			System.out.println();
			System.out.println("Modem Information:");
			System.out.println("  Manufacturer: " + gateway.getManufacturer());
			System.out.println("  Model: " + gateway.getModel());
			System.out.println("  Serial No: " + gateway.getSerialNo());
			System.out.println("  SIM IMSI: " + gateway.getImsi());
			System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
			System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
                        //System.out.println("  MSG en Cola: " + gateway.get + "%");
			System.out.println();
			// In case you work with encrypted messages, its a good time to declare your keys.
			// Create a new AES Key with a known key value. 
			// Register it in KeyManager in order to keep it active. SMSLib will then automatically
			// encrypt / decrypt all messages send to / received from this number.
			//Service.getInstance().getKeyManager().registerKey("+306948494037", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
			// Read Messages. The reading is done via the Service object and
			// affects all Gateway objects defined. This can also be more directed to a specific
			// Gateway - look the JavaDocs for information on the Service method calls.
			msgList = new ArrayList<InboundMessage>();
			Service.getInstance().readMessages(msgList, MessageClasses.UNREAD);
			for (InboundMessage msg : msgList){
                            //System.out.println(msg.getText());
                            //IndexCounter = IndexCounter + 1;
                        }
			// Sleep now. Emulate real world situation and give a chance to the notifications
			// methods to be called in the event of message or voice call reception.
			System.out.println("Now Sleeping - Hit <enter> to stop service.");
			System.in.read();
			System.in.read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Service.getInstance().stopService();
		}
	}

	public class InboundNotification implements IInboundMessageNotification 
	{
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) 
		{
                    try {
                        if (msgType == MessageTypes.INBOUND) System.out.println(">>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
                        else if (msgType == MessageTypes.STATUSREPORT) System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
                        
                        String[] stemp;
                        String delimiter = " ";
                        
                        Date date = new Date(); 
                        Calendar calendar = GregorianCalendar.getInstance();
                        calendar.setTime(date);
                        
                        DBMain result = new DBMain();
                        
                        stemp = msg.getText().split(delimiter);                        
                        
                        ResultSet rs = result.DBQuery("SELECT * FROM ALUMNOS WHERE CODIGO = '"+stemp[1]+"'");
                        
                        if(rs.isBeforeFirst()){
                        
                              
                            ResultSet rsv = result.DBQuery("SELECT * FROM VOTES WHERE CODIGO = '"+stemp[1]+"'");
                            
                            if(rsv.isBeforeFirst()){

                                VotaSMS.activitiesFeed.setText("Intento de voto duplicado para el codigo "+stemp[1]+"");

                            }else{
                                   result.DBUpdate("INSERT INTO VOTES (LISTA, CODIGO, CAMPANIA, HORA) VALUES ('"+stemp[0]+"', '"+stemp[1]+"', "+VotaSMS.Campaing+",'"+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":00')");
                            }
                                
                         }else{
                             VotaSMS.activitiesFeed.setText("Codigo no existe: "+stemp[1]+"");
                             System.out.println("Codigo no existe: "+stemp[1]+"");
                         }
                        //System.out.println(msg.getText());
                        gateway.deleteMessage(msg);
                        VotaSMS.pastel();
                    } catch (SQLException ex) {
                        Logger.getLogger(SMSListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TimeoutException ex) {
                        Logger.getLogger(SMSListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (GatewayException ex) {
                        Logger.getLogger(SMSListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SMSListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SMSListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}

	public class CallNotification implements ICallNotification
	{
		public void process(AGateway gateway, String callerId)
		{
			System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

	public class GatewayStatusNotification implements IGatewayStatusNotification
	{
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus)
		{
			System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}

	public class OrphanedMessageNotification implements IOrphanedMessageNotification
	{
		public boolean process(AGateway gateway, InboundMessage msg)
		{
			System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
			System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned message part.
			return false;
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

	/*public static void main(String args[])
	{
                
		SMSListener app = new SMSListener();
		try
		{
			app.doIt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
}

