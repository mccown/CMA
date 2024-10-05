/**
//////////////////////////////////////////////////////////////////////////
	java.VoiceInput.ServerRegionCommand

	ServerRegionCommand.java - 
			This is a lightweight object for use in the VoiceInput package.
		It calls on other helper objects to evaluate the values  received 
		from the ServerThread socket passed to it(ServerEval) then sets the 
		Region, Command  and CommandStr values.  This object can then  be 
		passed around without much overhead for use in the package.  It is
		anticipate that in future releases this object, created for use on
		the component,(which was passed to the ServerThread).  It almost 
		acts as a facade to the component callback because the ServerEval 
		class, which it calls to create the command string, interprets many 
		types of input command sources even though they all come over the 
		the socket.(they are content based)

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

ServerRegionCommand Class Methods:
==================================
	protected ServerRegionCommand(byte eval_stream[]):
			Constructor for the object which takes as an argument a byte
		array.  When this constructor is invoked an evaluation of the byte
		array is performed to establish the values for REGION (R) and 
		COMMAND (C).  R and C are the key private elements of this object.
	
	protected ServerRegionCommand(int R, int C):
			Constructor for the object which takes as arguments an integer
		representing the REGION (R) and an integer representing the 
		COMMAND (C) portions of the object.  It then simulates the byte
		array and calls the evaluator as in the above constructor.
	
	protected int getRegion():
			Simple getter method which returns the value for REGION

	protected int getCommand():
			Simple getter method which returns the value for COMMAND.

	protected String getCommandStr()
		Simple getter method which return the string value of the 
		command

	protected void resetRegion(int R):
			Simple setter method to reset the REGION of an object.

	protected void resetCommand(int C);
			Simple setter method to reset the REGION of an object.

	protected void resetRandC( int R, int C):
			Simple setter method to reset the REGION and COMMAND of an 
		object

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
	
	  This object was designed to be simple in and of itself.  This is so
	that the processing element which constructs it (at this point the 
	ServerThread object) can simple access its values and pass around 
	a very lightweight object.  This is evident in the constructor with
	the byte array.  When that object is constructed another very temporary
	object is constructed (ServerEval object) which has the dubious task of
	evaluating the byte array to come up with the proper Region and Command
	and Command_Str  values.  After this occurs the object is destroyed and 
	a very lightweight ServerRegionCommand object remains with the pertinent
	information.


**/
///////////////////////////////////////////////////////////////
//                                                           //
//  declarations                                             //
//                                                           //
///////////////////////////////////////////////////////////////

package VoiceInput;

public class ServerRegionCommand {
	private int Region=0;
	private int Command=0;
	private String Command_Str=null;

	//default constructor which satisfies the ServerEval Subclass
	protected ServerRegionCommand(){
		Region=0;
		Command=0;
		Command_Str=null;
	}

	//evaluate a the stream to set the region and command
	protected ServerRegionCommand(byte eval_stream[]){
		//Create a very short lived ServerEval object to perform
		//evaluation of the byte array
		ServerEval ep = new ServerEval(eval_stream);//ep - eval packet
		Region = ep.region();
		Command = ep.command();
		Command_Str=ep.commandstr();
	}

	//Directly set the region and comand from integer input verses
	//a byte array...
	protected ServerRegionCommand(int R, int C){
		byte tempBarray[] = new byte[20];

		tempBarray[0]= (byte) R;
		tempBarray[1]= (byte) C;
		
		//Create a very short lived ServerEval object to perform
		//evaluation of the byte array
		ServerEval ep = new ServerEval(tempBarray);//ep - eval packet
		Region = ep.region();
		Command = ep.command();
		Command_Str=ep.commandstr();
	}

	//returns the region for the object
	protected int getRegion(){
		return Region;
	}

	//returns the command for the object
	protected int getCommand(){
		return Command;
	}
	
	//returns the command string for the object
	protected String getCommandStr(){
		return Command_Str;
	}


	//The following reset methods can be dangerous because they do not 
	//check the validity of the input data. So when the callback to 
	//the component is executed eroneus results may occur.  However,
	//since they are protected commands they are only relevent to the 
	//developer with access to the source code.  The standard user of
	//this package would only interface to this stuff through the Server
	//class/object which can only create the objects and pass a callback
	//in to be operated on... 
	
	//resets region for the object
	protected void resetRegion(int R){
		this.Region=R;
	}

	//resets command for the object
	protected void resetCommand(int C){
		this.Command=C;
	}

	//resets region and command for the object
	protected void resetCommand(int R, int C){
		this.Region=R;
		this.Command=C;
	}

};
