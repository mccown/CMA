/**
//////////////////////////////////////////////////////////////////////////
	java.VoiceInput.Server
  
	Server.java - 
		A Server object/thread accepts connection from client applications
	on a socket.  This implementation specifically listening on socket 1201
	for Hark Speech system clients.  The Server thread spawns a ServerThread
	object/thread for every such connection.  Continuously  listening  for 
	more connections to spawn more independent threads. The Server class 
	itself is multi-threaded so as to be non-blocking.  The intent here is 
	to embed this package in some other client/server program.  Thus 
	allowing  it to run on its own.  A maximum of two clients can be active 
	at a given instant.  Processing of client requests is done by the 
	corresponding ServerThread. 

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

Server Class Methods:
=====================

	public Server(Component parentCallBack): 
			Constructor which takes as an argument a component object to 
		reference as a callback when the ServerThread has processed a 
		request
											
	public void run(): 
			Standard method to begin running a thread. In this also used to
		spawn multiple ServerThreads

	public static void ServerInfo(): 
			This is a class method that simply displays the hostname and ip 
		where the server was spawned


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
	
	The Server class subclasses the Thread class.  It is created by the 
  client which embeds this package for socket sever use.  This class is 
  setup to be the main socket server thread of the VoiceInput package.  
  Server itself is a thread, which spawns request processor threads.  The 
  impetus of this is to be able to embed, in essence, a non-blocking socket 
  server in a client program.  When a client program initializes and spawns 
  the main socket server thread (I.E. this one) it is then free to go on its 
  merry way doing whatever it does to keep happy.  This main thread then 
  services requests to spawn request processor threads (SEE: ServerThread 
  class).  Therefore allowing the client to accept input from multiple 
  external sources on the same socket without blocking the main client 
  portion of the application.  The main server thread blocks until a 
  request to connect to the client is initiated.  When this occurs a 
  separate thread is spawned which performs the usual block on read which 
  sockets do so well.  If a second or third etc. etc. request for 
  connection comes in the main socket server spawns a separate thread for 
  the main client to interface with.

**/
///////////////////////////////////////////////////////////////
//                                                           //
//  declarations                                             //
//                                                           //
///////////////////////////////////////////////////////////////


package VoiceInput;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.awt.*;
import java.io.*;

public class Server extends Thread {
	
	static Socket theSocket;
	static ServerSocket ServSock;
	static ServerThread client[] = new ServerThread[10];

	DataInputStream datain;
    DataOutputStream dataout;
	
	static int port = 1201;
	private Component parentCallBack;
	//component event ID valu for voice input 
	public static final int VOICE_EVENT_ID=2000;

	
	// If we wanted ti make this part of a standalone package we would need 
	// to implement a main.  The first pass at this package before it was
	// officialy versioned was standalone to facilitate testing while the
	// client software which it was destined for was being built.(I.E. CMA and
	// RMIServer)

	//public static void main(String args[]) {}

	
	public Server(Component parentCallBack){
		//not much to do here but initalize the parent component in the Server
		//thread object. This implementation allows us to send any type of component
		//to the server for a callback.  In this case (ver 1.0) callbacks will be to
		//the Collaborative Map Annotator client to the RMIServer
		this.parentCallBack = parentCallBack;
	}

    public void run(){
	  int indx=0;
	  ServerInfo();
	  try {
		ServSock = new ServerSocket(port);

		System.out.println("Main socket server thread started.");
		System.out.println("Awaiting connection....");

		while (true) {
		  if (indx <= 9){
			
		    theSocket = ServSock.accept();//this method blocks until a connection is made
		    System.out.println("Accepted a socket connection");
		 
	    client[indx] = new ServerThread(theSocket,indx, parentCallBack);  
		client[indx].start();

		    System.out.println("ServerThread # " + indx + " spawned");

		    indx++;
		  }
		  else {
			System.out.println("Out of server threads sorry");
		  }

		}
      } catch (IOException ioE){
		  System.out.println(" Server error ");
		}
    }

	public static void ServerInfo(){
		InetAddress s_inet;
		String s_name;

		try {
			//initalize InetAddress s_inet
			s_inet = InetAddress.getLocalHost();

			//obtain the string from of InetAddress
			s_name = s_inet.getHostName();

			//obtained by using getLocalHost() above
			String s_str = s_inet.toString();

			// get the index where '/' appears; before that
			// character is the IP address in the InetAddress String
			int index = s_str.indexOf('/');
			// obtain the ip address only
			String s_ipaddr = s_str.substring(index+1);
			int srcBegin=0,dstBegin=0;
			char s_ipname[]= new char [40];
			
			//load up name
			s_str.getChars(srcBegin,index,s_ipname,dstBegin);

			//print to system out
			System.out.println("IP Address : " + s_ipaddr);
			System.out.println("Hostname : " + s_ipname );

		} catch (IOException ioE){
			System.out.println(" ServerInfo() error: Possible Problem");
		    System.out.println(" cant get local host network information");
			};
	}

}
