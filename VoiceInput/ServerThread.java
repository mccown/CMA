/**
//////////////////////////////////////////////////////////////////////////
  java.VoiceInput.ServerThread

  ServerThread.java - 
		This class is a object/thread spawned by the Server class of this 
	package.   It serves as an external input thread to the main client 
	program where it resides.  It processes requests (that it was designed 
	to handle) that comes in over the socket.  This first version handles
	requests for the BBN Hark speech recognition system on socket 1201.  
	The request which comes in over the socket is passed to the 
	ServerRegionCommand class/object for evaluation and creation of the
	command to be processed by the component object it is holding.

	ver 1.0

////////////////////////////////////////////////////////////////////////////
//
//      Manual page:
//
////////////////////////////////////////////////////////////////////////////
  
	Language:               SunSoft JAVA (JDK compiler), ver 1.1.2
	Platform:               Micron TransPort XPE, P5-166MMX
					Windows 95. [Version 4.00.1111]
	Application:    CIS 500/Masters Project
	Author:                 Francis A. DiLego, Jr.
	
////////////////////////////////////////////////////////////////////////////

ServerThread Class Methods:
===========================  

	public ServerThread(Socket m,int Id, Component incomingParentCallBack): 
			Constructor which takes the socket value to listen on (which is 
		assigned by the main Server) along with its Id and a generic 
		component object which it references as a callback.

	public void mesg(String str): 
			A simple print routine to system out.  Written for convenience
		(I.E. mesg is shorter than System.out.Println)

	public void run():
			The standard run method for a thread, obviously adapted for 
		ServerThread.

	private boolean processVoiceRequests():
			The method where most of the work gets done.  This method is 
		called by the run method repeatedly until and exit condition is 
		encountered to as to service an endless stream of requests.  This 
		method calls a blocking method on the data input stream.  When the 
		data buffer on the input stream is flushed the request is processed.
		Processing of the request is done with the aid of two (at this 
		point ver 1.0) other classed ServerEval and ServerRegionCommand.
	
	void  CleanUp()
			A simple method to cleanly close all open streams and exits 
		gracefully.

///////////////////////////////////////////////////////////////////////////
//                                                           
//  Maintenance page                                         
//                                                           
///////////////////////////////////////////////////////////////////////////
							  
  Build Process                                            
							   
  Files Required:                                          
    (standard java core and a main since this is not a standalone
		java package)
		VoiceInput Package: Server.java
							ServerEval.java
							ServerRegionCommand.java
							ServerThread.java
								   
  Command Line Compiler Build: 
	(NOTE: Be sure your CLASSPATH variable is set to point to the 
			VoiceInput package directory)
		   
    javac VoiceInput/*.java                       
						  
///////////////////////////////////////////////////////////////////////////
//
//  Maintenance History:
//
//////////////////////////////////////////////////////////////////////////

    ver 1.0 7/11/97 - initial release

//////////////////////////////////////////////////////////////////////////
//                                                           
//  Design Notes                                             
//                                                           
/////////////////////////////////////////////////////////////////////////
	
		The ServerThread class subclasses the Thread class. This thread is 
	spawned by the Server object, which was in turn spawned by the main 
	client. This thread/class accepts connection from external clients on a 
	socket in this case socket 1201.  Each ServerThread services requests 
	from one client assigned by the Server.  Multiple ServerThreads can be 
	spawned by the main socket server for processing from multiple external
	sources.  Thus allowing, as in this first release, multiple Hark speech
	systems to communicate to one client Collaborative Map Annotator CMA.

**/
///////////////////////////////////////////////////////////////
//                                                           //
//  declarations                                             //
//                                                           //
///////////////////////////////////////////////////////////////

package VoiceInput;

import java.net.Socket;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.AWTEvent.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionEvent.*;
import java.applet.*;
import java.util.*;

public class ServerThread extends Thread {

	private Socket mySocket;
	private DataInputStream datain;
	private DataOutputStream dataout;
	private int myId;
	
	private Component parentcallback;

		//ServerThread constructor
	public ServerThread(Socket m,int Id, Component incomingParentCallBack) throws IOException {
	
	   mySocket = m;
	   myId = Id;
	   
	   //initalize the callback
	   this.parentcallback = incomingParentCallBack;

	//initalize the input and otput streams
	   datain  = new DataInputStream(new 
				   BufferedInputStream(mySocket.getInputStream()));
	   dataout = new DataOutputStream(new 
				   BufferedOutputStream(mySocket.getOutputStream()));
	}

	// simple output statement 
	public void mesg(String str) {
		System.out.println(str);
	}

	//standard run method for threads allbe it specialized for the
	//VoiceInput package
	public void run() {
	  try {
	    while (processVoiceRequests()) 
	      yield(); // yield to other threads too!

	    CleanUp();
	  } catch (IOException E){
		System.out.println("Error in processing request");
	  }

	}       

	//This method processes client voice requests read from the 
	//socket stream by evaluating the content, establishing the 
	//Region and Command for the input voice packet and finaly
	//making the proper component callback.

    private boolean processVoiceRequests() throws IOException {
	   try {
		 byte  buff[] = new byte [20];//Incomming buffer for Hark Packet
		 byte  rbuff[]= new byte [20];//Return ACK for Hark Socket
		 rbuff[0]= (byte)'A'; rbuff[1]=(byte)'C'; rbuff[2]=(byte)'K';
		 
		 if ( datain.read(buff) == 20 ) {
		   mesg("Recieved legal byte array...");
		   mesg("Region & Command Components "+ buff[0] +" "+ buff[1]); 
		   mesg(" ");
			   
		   //validates packet contents and sets region and command values
		   ServerRegionCommand srg = new ServerRegionCommand(buff);

		   mesg(" ");
		   mesg("REGION : " + srg.getRegion());
		   mesg("COMMAND: " + srg.getCommand());
		   mesg(" ");

		   //Send ACK to Hark 
		   dataout.write(rbuff,0,rbuff.length);
		   dataout.flush();
		   
		   //Check for termination codes
		   if (srg.getRegion()==5 && srg.getCommand()==ServerEval.MAX_VOICE_MAP){
			   mesg("Terminating Hark Voice Input Thread...");
			   return false;
		   }
		   //need to add check for termination codes for other regions
		   //in future releases... currently the other regions 
		   //are not in use
		   if (srg.getRegion()!=0){//the only region supported is map (5)
		     ActionEvent awtEvent = new ActionEvent(this, Server.VOICE_EVENT_ID, srg.getCommandStr());
		     parentcallback.dispatchEvent(awtEvent); //executes various commands on what ever in 
		   }                                                                            //our case the CMA map server
		   else {
			   mesg("Unimplemented Region...!!!!");
		   }

//Masters project extentions (voice feedack component goes here
//AudioClip soundFile; Applet app = new Applet();
//URL url=new URL ("file://localhost/d:/Baby.au");
//soundFile = app.getAudioClip(url);
//soundFile.play();     

			return true;
		   
		 }
		else {
			System.out.println("Unknown service requested");
			return false;
		}
	  } catch(IOException ioe) {
		System.out.println("Error in input from Client");
		return false;
	  }

	}

	//clean up when done I. E. close datastreams and mysocket
	void  CleanUp() {
		
		try {
		datain.close();
		dataout.close();
		mySocket.close();
		} catch (IOException io) {
			mesg(" IOEXception!! in ServerThread.CleanUp()");
			mesg(io.getMessage());
		}
	}

}
