/**
//////////////////////////////////////////////////////////////////////////
  java.VoiceInput.ServerEval

  ServerEval.java - 
		This class is instantiated by a  ServerRegionCommand class object
	of  this package.  It implements subsystem functionality assigned to 
	it by the ServerRegionCommand class/object. It evaluates the input 
	stream of the socket or integer input (defined by the constructor 
	called, in this version only the byte stream constructor is used ). 
	It is very short lived and decouples the input from the use of the 
	input. Current evaluation supports the Collaborative Map Annotator.  
	However, the design "hooks" for other systems is in place  (future 
	releases).  Future releases may require a data file to support in a 
	more dynamic fashion existing and future systems request.  Its 
	evolution appears to be that of an interpreter design but it is not 
	there yet.

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

ServerEval Class Methods:
=========================  

	protected ServerEval( byte IN_stream[]):
			Constructor, which takes a byte stream as, input for evaluation by
		the eval_packet() method.

	protected int region():
			Returns the value of region for the short lived object that was
		evaluated from the byte stream.

	protected int command():
			Returns the value of command for the short lived object that was
		evaluated from the byte stream.

	public void mesg(String str):
			A simple print routine to system out.  Written for convenience
		(I.E. mesg is shorter than System.out.Println)

	private void eval_packet( byte hark_stream[]):
			This method is currently (ver1.0) where most of the work is 
		performed.  Its evaluation is only relevant to the Collaborative
		Map Annotator however design/"hooks" for future systems are in 
		place.

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
	
		This class subclasses the ServerRegionCommand class.  I guess the 
  biggest design note would be the way ServerEval instantiate the values 
  for the VOICE_COMMAND, VOICE_REGION, and COMMAND_STR.  The evaluation 
  values to establish these are public final class instances so they
  may be referenced by name outside of this package.  The reason for this
  is to allow the client access to the knowledge this class has to what 
  it can evaluate from an external source.  However, all the methods and
  constructors etc. of this class       are protected so that they are only
  relevant to the classes in this package.  
	The only place this class is ever called or referenced
  through a method is in the constructor of a ServerRegionCommand object. 
  A future implementation may remove the specific instance values from
  within the class and store them in a data file which is read and then
  initializes and array or two.  This has advantages and disadvantages.  
  The advantages are that the values can become more dynamic, in that the 
  class would not need recompilation after addition of a new region or
  command element.  I would simply require editing of the data file to 
  contain the new values.  It would also reduce the size of the class
  file and alter the algorithm for evaluation to a more elegant verses
  brute force approach.  The disadvantage are that with the way the other
  classes in this package use this class there would be a performance 
  penalty of reading in the data file every time an object of this type
  was created.  This would open the issues of data file integrity, 
  consistency and simple existence.  Also if this package were to be added
  to an applet that applet would have to be able to carry the data file 
  around with it which becomes a problem!!!


**/
///////////////////////////////////////////////////////////////
//                                                           //
//  declarations                                             //
//                                                           //
///////////////////////////////////////////////////////////////

package VoiceInput;

public class ServerEval extends ServerRegionCommand{ 

	//The two cornerstone instance values of this class.
	//They represent the evaluated region to which the evaluated
	//command represents.
	private int VOICE_COMMAND = 0;
	private int VOICE_REGION = 0;
	private String COMMAND_STR;

	//All the following public class variable are as thy are because
	//we want to make available to the package etc. all the correct 
	//values coresponging to the voice input.  This is in case in the
	//we want to simulate the calls from out side of the class; we will
	//be consistant accross the board.  If we cange the implementation to 
	//be one of reading the values form a data input file and evaluating
	//them iterivly the this would change...
	//activation region not used by this map server at this point
	public static final int VOICE_ACTIVATION_REGION = 1;
	public static final String _voiceActivationRegion = "Activation";
	public static final int VOICE_ACTIVATION_COMMAND_DEFAULT = 0;//
		//public static final int COMPUTER = 1;
		//public static final int DRAWING_CONTROL = 2;
		//public static final int FLIGHT_CONTROL = 3; 
		//public static final int MAP_CONTROL = 4;

	//manager region not used by this map server at this point
	public static final int VOICE_MANAGER_REGION = 2;
	public static final String _voiceManagerRegion = "Manager";
	public static final int VOICE_MANAGER_COMMAND_DEFAULT = 0;
		//public static final int USE_DRAWING_COMMANDS = 1
		//public static final int USE_FLIGHT_COMMANDS = 2
		//public static final int PUT_TERMINAL_HERE = 3
		//public static final int RUN_VIDEO_CLIP_ONE = 4
		//public static final int RUN_VIDEO_CLIP_TWO = 5
		//public static final int RUN_VIDEO_CLIP_THREE = 6
		//public static final int EXIT_PROGRAM = 7
		//public static final int QUIT_PROGRAM = 8
		//public static final int START_LASER_PEN = 9
		//public static final int STOP_LASER_PEN = 10
		//public static final int USE_MAP_COMMANDS = 11;
		//add new commands above this line and increment the max value
		//public static final int 

	//drawing region not used by this map server at this point
	public static final int VOICE_DRAWING_REGION = 3;
	public static final String _voiceDrawingRegion = "Drawing";
	public static final int VOICE_DRAWING_COMMAND_DEFAULT = 0;
		//public static final int CLEAR_SCREEN = 1
		//public static final int DONE_DRAWING = 2
		//public static final int SET_POINT = 3
		//public static final int DRAW_LINE = 4
		//public static final int DRAW_CIRCLE = 5
		//public static final int DRAW_SQUARE = 6
		//public static final int DRAW_BOX = 7
		//public static final int DRAW_IN_BLACK = 8
		//public static final int DRAW_IN_WHITE = 9
		//public static final int DRAW_IN_RED = 10
		//public static final int DRAW_IN_GREEN = 11
		//public static final int DRAW_IN_BLUE = 12
		//public static final int ERASE_BRUSH = 13
		//public static final int SMALL_PEN_SIZE = 14
		//public static final int MEDIUM_PEN_SIZE = 15
		//public static final int LARGE_PEN_SIZE = 16
		//public static final int RAISE_PEN = 17
		//public static final int LOWER_PEN = 18
		//public static final int EXIT_VOICE_DRAWING = 19
		//public static final int QUIT_VOICE_DRAWING = 20
		//add new commands above this line and increment the max value
		//public static final int MAX_VOICE_DRAWING = 20
	
	//Flight controls not used by this map server at this point
	public static final int VOICE_FLIGHT_REGION = 4;
	public static final String _voiceFlightRegion = "Flight";
	public static final int VOICE_FLIGHT_COMMAND_DEFAULT = 0;
		//public static final int DONE_FLIGHT = 1
		//public static final int TOGGLE_TM = 2
		//public static final int LEVEL = 3
		//public static final int STRAIGHT = 4
		//public static final int BANK_LEFT = 5 
		//public static final int BANK_RIGHT = 6
		//public static final int GO_LEFT = 7
		//public static final int GO_RIGHT = 8
		//public static final int GO_UP = 9
		//public static final int GO_DOWN = 10
		//public static final int GO_FASTER = 11
		//public static final int GO_FORWARD = 12
		//public static final int GO_SLOWER = 13
		//public static final int GO_BACK = 14
		//public static final int ACCELERATE = 15
		//public static final int DECELERATE = 16
		//public static final int ASCEND = 17
		//public static final int DESCEND = 18
		//public static final int HALT = 19
		//public static final int STOP = 20
		//public static final int HOVER = 21
		//public static final int EXIT_VOICE_FLIGHT = 22
		//public static final int QUIT_VOICE_FLIGHT = 23
		//add new commands above this line and increment the max value
		//public static final int MAX_VOICE_FLIGHT = 23;

	//Map region
	public static final int VOICE_MAP_REGION = 5;
	public static final String _voiceMapRegion = "Map";
	public static final int VOICE_MAP_COMMAND_DEFAULT = 0;

	public static final int LOAD_MAP = 1;
	public static final int LOAD_ICON = 2;
	public static final int CREATE_FRIENDLY_AIR_DEFENSE = 3;
	public static final int CREATE_FRIENDLY_ANTI_TANK = 4;
	public static final int CREATE_FRIENDLY_ARMOR = 5;
	public static final int CREATE_FRIENDLY_ARMORED_CAVALRY = 6;
	public static final int CREATE_FRIENDLY_CHEMICAL = 7;
	public static final int CREATE_FRIENDLY_INFANTRY = 8;
	public static final int CREATE_FRIENDLY_MAINTENANCE = 9;
	public static final int CREATE_FRIENDLY_MECHANIZED_INFANTRY = 10;
	public static final int CREATE_FRIENDLY_MEDICAL = 11;
	public static final int CREATE_FRIENDLY_SIGNAL_COM = 12;
	public static final int CREATE_HOSTILE_AIR_DEFENSE = 13;
	public static final int CREATE_HOSTILE_ANTI_TANK = 14;
	public static final int CREATE_HOSTILE_ARMOR = 15;
	public static final int CREATE_HOSTILE_ARMORED_CAVALRY = 16;
	public static final int CREATE_HOSTILE_CHEMICAL = 17;
	public static final int CREATE_HOSTILE_INFANTRY = 18;
	public static final int CREATE_HOSTILE_MAINTENANCE = 19;
	public static final int CREATE_HOSTILE_MECHANIZED_INFANTRY = 20;
	public static final int CREATE_HOSTILE_MEDICAL = 21;
	public static final int CREATE_HOSTILE_SIGNAL_COM = 22;
	public static final int CREATE_NEUTRAL_AIR_DEFENSE = 23;
	public static final int CREATE_NEUTRAL_ANTI_TANK = 24;
	public static final int CREATE_NEUTRAL_ARMOR = 25;
	public static final int CREATE_NEUTRAL_ARMORED_CAVALRY = 26;
	public static final int CREATE_NEUTRAL_CHEMICAL = 27;
	public static final int CREATE_NEUTRAL_INFANTRY = 28;
	public static final int CREATE_NEUTRAL_MAINTENANCE = 29;
	public static final int CREATE_NEUTRAL_MECHANIZED_INFANTRY = 30;
	public static final int CREATE_NEUTRAL_MEDICAL = 31;
	public static final int CREATE_NEUTRAL_SIGNAL_COM = 32;
	public static final int CREATE_UNKNOWN_AIR_DEFENSE = 33;
	public static final int CREATE_UNKNOWN_ANTI_TANK = 34;
	public static final int CREATE_UNKNOWN_ARMOR = 35;
	public static final int CREATE_UNKNOWN_ARMORED_CAVALRY = 36;
	public static final int CREATE_UNKNOWN_CHEMICAL = 37;
	public static final int CREATE_UNKNOWN_INFANTRY = 38;
	public static final int CREATE_UNKNOWN_MAINTENANCE = 39;
	public static final int CREATE_UNKNOWN_MECHANIZED_INFANTRY = 40;
	public static final int CREATE_UNKNOWN_MEDICAL = 41;
	public static final int CREATE_UNKNOWN_SIGNAL_COM = 42;
	public static final int CREATE_LINE = 43;
	public static final int EXIT_VOICE_MAP = 44;
	//add new commands above this line and increment the max value
	public static final int MAX_VOICE_MAP = 44;

	//the following values map to the CMA interface callback values
	//for performing map functions (I'am not sure if I like them here
	private String mapCommands[] =
	{
		"",
		"Load Map",
		"Load Icon",
		"Friendly FSF Air Defense",
		"Friendly FSF Anti Tank",
		"Friendly FSF Armor",
		"Friendly FSF Armored Cavalry",
		"Friendly FSF Chemical",
		"Friendly FSF Infantry",
		"Friendly FSF Maintenance",
		"Friendly FSF Mechanized Infantry",
		"Friendly FSF Medical",
		"Friendly FSF Signal Comm",
		"Hostile FSF Air Defense",
		"Hostile FSF Anti Tank",
		"Hostile FSF Armor",
		"Hostile FSF Armored Cavalry",
		"Hostile FSF Chemical",
		"Hostile FSF Infantry",
		"Hostile FSF Maintenance",
		"Hostile FSF Mechanized Infantry",
		"Hostile FSF Medical",
		"Hostile FSF Signal Comm",
		"Neutral FSF Air Defense",
		"Neutral FSF Anti Tank",
		"Neutral FSF Armor",
		"Neutral FSF Armored Cavalry",
		"Neutral FSF Chemical",
		"Neutral FSF Infantry",
		"Neutral FSF Maintenance",
		"Neutral FSF Mechanized Infantry",
		"Neutral FSF Medical",
		"Neutral FSF Signal Comm",
		"Unknown FSF Air Defense",
		"Unknown FSF Anti Tank",
		"Unknown FSF Armor",
		"Unknown FSF Armored Cavalry",
		"Unknown FSF Chemical",
		"Unknown FSF Infantry",
		"Unknown FSF Maintenance",
		"Unknown FSF Mechanized Infantry",
		"Unknown FSF Medical",
		"Unknown FSF Signal Comm",
		"Line",
		"Exit Voice Map"
	};



	//Constructor this constrctor is called by ServerRegionCommand class
	//this is so ServerThread only need deal with the command an region 

	protected ServerEval( byte IN_stream[]){
			//evaluate the data and set the region and command 
				eval_packet(IN_stream);
				//finally set the command string for the component
				//callback
				COMMAND_STR=mapCommands[VOICE_COMMAND];
	};

	//accessors to the region and command
	protected int region(){return VOICE_REGION;}
	protected int command(){return VOICE_COMMAND;}
	protected String commandstr(){return COMMAND_STR;}

	// simple output statement for debugging etc
	public void mesg(String str) {
		System.out.println(str);
	}


	//This is where all the wval work is done..
	//consume(byte[]) processes byte stream from hark voice system to 
	//set the voice region and command.
	private void eval_packet( byte hark_stream[])
	{
		// only eval first two bytes for region command; 18 remaining 
		//bytes are for future use

		int region  = (int) hark_stream[0];//ignore 8 bit integer comming in
		int command = (int) hark_stream[1];//ignore 8 bit integer comming in

		switch(region){
		  case VOICE_ACTIVATION_REGION :
			switch (command){
			//insert activation commands commands here
			  default:
			    mesg("VOICE_ACTIVATION_REGION : " + _voiceActivationRegion);
			    mesg("UNIMPLEMENTED REGION NO ACTION OCCURED!!");
			    VOICE_REGION= 0;
			    VOICE_COMMAND = VOICE_ACTIVATION_COMMAND_DEFAULT;
			}
			break;

		  case VOICE_MANAGER_REGION ://= 2;
			switch (command){
			//insert manager commands here
			  default:
			mesg("VOICE_MANAGER_REGION : " + _voiceManagerRegion );
			    mesg("UNIMPLEMENTED REGION NO ACTION OCCURED!!");
			    VOICE_REGION= 0;
			    VOICE_COMMAND= VOICE_MANAGER_COMMAND_DEFAULT;
			}
		    break;

		  case VOICE_DRAWING_REGION :// = 3;
			switch (command){
			//insert drawing commands here
			  default:
			    mesg ("VOICE_DRAWING_REGION : " +  _voiceDrawingRegion );
			    mesg("UNIMPLEMENTED REGION NO ACTION OCCURED!!");
			    VOICE_REGION= 0;
			    VOICE_COMMAND = VOICE_DRAWING_COMMAND_DEFAULT;
			}
			break;
		  case VOICE_FLIGHT_REGION ://= 4;
			switch (command){
			//insert flight commands here
			  default:
				mesg("VOICE_FLIGHT_REGION : " + _voiceFlightRegion);
			    mesg("UNIMPLEMENTED REGION NO ACTION OCCURED!!");
			    VOICE_REGION= 0;
			    VOICE_COMMAND= VOICE_FLIGHT_COMMAND_DEFAULT;
			  }
			break;
		  case VOICE_MAP_REGION ://=5;
			mesg("VOICE_MAP_REGION : " + _voiceMapRegion + " " + VOICE_MAP_REGION);
			VOICE_REGION= VOICE_MAP_REGION;
			//This switch statement captures and evaluated the command value sent via 
			//the socket from Hark voice interpreter for audio map commands
			switch(command){
			  case LOAD_MAP :
				mesg("VOICE_MAP_COMMANDS : LOAD_MAP " + LOAD_MAP);
				VOICE_COMMAND = LOAD_MAP;
				break;
			    
			  case LOAD_ICON:
				mesg("VOICE_MAP_COMMANDS : LOAD_ICON " + LOAD_ICON);
				VOICE_COMMAND =  LOAD_ICON;
				break;
			  case CREATE_FRIENDLY_AIR_DEFENSE:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_AIR_DEFENSE " + CREATE_FRIENDLY_AIR_DEFENSE);
				VOICE_COMMAND =  CREATE_FRIENDLY_AIR_DEFENSE;
				break;

			  case CREATE_FRIENDLY_ANTI_TANK:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_ANTI_TANK " + CREATE_FRIENDLY_ANTI_TANK);
				VOICE_COMMAND =  CREATE_FRIENDLY_ANTI_TANK;
				break;

			  case CREATE_FRIENDLY_ARMOR:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_ARMOR " + CREATE_FRIENDLY_ARMOR);
				VOICE_COMMAND =  CREATE_FRIENDLY_ARMOR;
				break;

			  case CREATE_FRIENDLY_ARMORED_CAVALRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_ARMORED_CAVALRY " + CREATE_FRIENDLY_ARMORED_CAVALRY);
				VOICE_COMMAND =  CREATE_FRIENDLY_ARMORED_CAVALRY;
				break;

			  case CREATE_FRIENDLY_CHEMICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_CHEMICAL " + CREATE_FRIENDLY_CHEMICAL);
				VOICE_COMMAND =  CREATE_FRIENDLY_CHEMICAL;
				break;

			  case CREATE_FRIENDLY_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_INFANTRY " + CREATE_FRIENDLY_INFANTRY);
				VOICE_COMMAND =  CREATE_FRIENDLY_INFANTRY;
				break;

			  case CREATE_FRIENDLY_MAINTENANCE:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_MAINTENANCE " + CREATE_FRIENDLY_MAINTENANCE);
				VOICE_COMMAND =  CREATE_FRIENDLY_MAINTENANCE;
				break;

			  case CREATE_FRIENDLY_MECHANIZED_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_MAINTENANCE " + CREATE_FRIENDLY_MECHANIZED_INFANTRY);
				VOICE_COMMAND =  CREATE_FRIENDLY_MAINTENANCE;
				break;

			  case CREATE_FRIENDLY_MEDICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_MEDICAL " + CREATE_FRIENDLY_MEDICAL);
				VOICE_COMMAND =  CREATE_FRIENDLY_MEDICAL;
				break;

			  case CREATE_FRIENDLY_SIGNAL_COM:
				mesg("VOICE_MAP_COMMANDS : CREATE_FRIENDLY_SIGNAL_COM " + CREATE_FRIENDLY_SIGNAL_COM);
				VOICE_COMMAND =  CREATE_FRIENDLY_SIGNAL_COM;
				break;

			  case CREATE_HOSTILE_AIR_DEFENSE:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_AIR_DEFENSE " + CREATE_HOSTILE_AIR_DEFENSE);
				VOICE_COMMAND =  CREATE_HOSTILE_AIR_DEFENSE;
				break;

			  case CREATE_HOSTILE_ANTI_TANK:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_ANTI_TANK " + CREATE_HOSTILE_ANTI_TANK);
				VOICE_COMMAND =  CREATE_HOSTILE_ANTI_TANK;
				break;

			  case CREATE_HOSTILE_ARMOR:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_ARMOR " + CREATE_HOSTILE_ARMOR);
				VOICE_COMMAND =  CREATE_HOSTILE_ARMOR;
				break;

			  case CREATE_HOSTILE_ARMORED_CAVALRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_ARMORED_CAVALRY " + CREATE_HOSTILE_ARMORED_CAVALRY);
				VOICE_COMMAND =  CREATE_HOSTILE_ARMORED_CAVALRY;
				break;

			  case CREATE_HOSTILE_CHEMICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_CHEMICAL " + CREATE_HOSTILE_CHEMICAL);
				VOICE_COMMAND =  CREATE_HOSTILE_CHEMICAL;
				break;

			  case CREATE_HOSTILE_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_INFANTRY " + CREATE_HOSTILE_INFANTRY);
				VOICE_COMMAND =  CREATE_HOSTILE_INFANTRY;
				break;

			  case CREATE_HOSTILE_MAINTENANCE:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_MAINTENANCE " + CREATE_HOSTILE_MAINTENANCE);
				VOICE_COMMAND =  CREATE_HOSTILE_MAINTENANCE;
				break;

			  case CREATE_HOSTILE_MECHANIZED_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_MECHANIZED_INFANTRY " + CREATE_HOSTILE_MECHANIZED_INFANTRY);
				VOICE_COMMAND =  CREATE_HOSTILE_MECHANIZED_INFANTRY;
				break;

			  case CREATE_HOSTILE_MEDICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_HOSTILE_MEDICAL " + CREATE_HOSTILE_MEDICAL);
				VOICE_COMMAND =  CREATE_HOSTILE_MEDICAL;
				break;

			  case CREATE_HOSTILE_SIGNAL_COM:
				mesg("VOICE_MAP_COMMANDS : " + CREATE_HOSTILE_SIGNAL_COM);
				VOICE_COMMAND =  CREATE_HOSTILE_SIGNAL_COM;
				break;

			  case CREATE_NEUTRAL_AIR_DEFENSE:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_AIR_DEFENSE " + CREATE_NEUTRAL_AIR_DEFENSE);
				VOICE_COMMAND =  CREATE_NEUTRAL_AIR_DEFENSE;
				break;

			  case CREATE_NEUTRAL_ANTI_TANK:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_ANTI_TANK " + CREATE_NEUTRAL_ANTI_TANK);
				VOICE_COMMAND =  CREATE_NEUTRAL_ANTI_TANK;
				break;

			  case CREATE_NEUTRAL_ARMOR:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_ARMOR " + CREATE_NEUTRAL_ARMOR);
				VOICE_COMMAND =  CREATE_NEUTRAL_ARMOR;
				break;

			  case CREATE_NEUTRAL_ARMORED_CAVALRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_ARMORED_CAVALRY " + CREATE_NEUTRAL_ARMORED_CAVALRY);
				VOICE_COMMAND =  CREATE_NEUTRAL_ARMORED_CAVALRY;
				break;

			  case CREATE_NEUTRAL_CHEMICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_CHEMICAL " + CREATE_NEUTRAL_CHEMICAL);
				VOICE_COMMAND =  CREATE_NEUTRAL_CHEMICAL;
				break;

			  case CREATE_NEUTRAL_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_INFANTRY " + CREATE_NEUTRAL_INFANTRY);
				VOICE_COMMAND =  CREATE_NEUTRAL_INFANTRY;
				break;

			  case CREATE_NEUTRAL_MAINTENANCE:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_MAINTENANCE " + CREATE_NEUTRAL_MAINTENANCE);
				VOICE_COMMAND =  CREATE_NEUTRAL_MAINTENANCE;
				break;

			  case CREATE_NEUTRAL_MECHANIZED_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_MECHANIZED_INFANTRY " + CREATE_NEUTRAL_MECHANIZED_INFANTRY);
				VOICE_COMMAND =  CREATE_NEUTRAL_MECHANIZED_INFANTRY;
				break;

			  case CREATE_NEUTRAL_MEDICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_MEDICAL " + CREATE_NEUTRAL_MEDICAL);
				VOICE_COMMAND = CREATE_NEUTRAL_MEDICAL ;
				break;

			  case CREATE_NEUTRAL_SIGNAL_COM:
				mesg("VOICE_MAP_COMMANDS : CREATE_NEUTRAL_SIGNAL_COM " + CREATE_NEUTRAL_SIGNAL_COM);
				VOICE_COMMAND =  CREATE_NEUTRAL_SIGNAL_COM;
				break;

			  case CREATE_UNKNOWN_AIR_DEFENSE:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_AIR_DEFENSE " + CREATE_UNKNOWN_AIR_DEFENSE);
				VOICE_COMMAND =  CREATE_UNKNOWN_AIR_DEFENSE;
				break;

			  case CREATE_UNKNOWN_ANTI_TANK:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_ANTI_TANK " + CREATE_UNKNOWN_ANTI_TANK);
				VOICE_COMMAND =  CREATE_UNKNOWN_ANTI_TANK;
				break;

			  case CREATE_UNKNOWN_ARMOR:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_ARMOR " + CREATE_UNKNOWN_ARMOR);
				VOICE_COMMAND =  CREATE_UNKNOWN_ARMOR;
				break;

			  case CREATE_UNKNOWN_ARMORED_CAVALRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_ARMORED_CAVALRY " + CREATE_UNKNOWN_ARMORED_CAVALRY);
				VOICE_COMMAND =  CREATE_UNKNOWN_ARMORED_CAVALRY;
				break;

			  case CREATE_UNKNOWN_CHEMICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_CHEMICAL " + CREATE_UNKNOWN_CHEMICAL);
				VOICE_COMMAND =  CREATE_UNKNOWN_CHEMICAL;
				break;

			  case CREATE_UNKNOWN_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_INFANTRY " + CREATE_UNKNOWN_INFANTRY);
				VOICE_COMMAND =  CREATE_UNKNOWN_INFANTRY;
				break;

			  case CREATE_UNKNOWN_MAINTENANCE:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_MAINTENANCE " + CREATE_UNKNOWN_MAINTENANCE);
				VOICE_COMMAND =  CREATE_UNKNOWN_MAINTENANCE;
				break;

			  case CREATE_UNKNOWN_MECHANIZED_INFANTRY:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_MECHANIZED_INFANTRY " + CREATE_UNKNOWN_MECHANIZED_INFANTRY);
				VOICE_COMMAND =  CREATE_UNKNOWN_MECHANIZED_INFANTRY;
				break;

			  case CREATE_UNKNOWN_MEDICAL:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_MEDICAL " + CREATE_UNKNOWN_MEDICAL);
				VOICE_COMMAND =  CREATE_UNKNOWN_MEDICAL;
				break;

			  case CREATE_UNKNOWN_SIGNAL_COM:
				mesg("VOICE_MAP_COMMANDS : CREATE_UNKNOWN_SIGNAL_COM " + CREATE_UNKNOWN_SIGNAL_COM);
				VOICE_COMMAND =  CREATE_UNKNOWN_SIGNAL_COM;
				break;

			  case CREATE_LINE:
				mesg("VOICE_MAP_COMMANDS : CREATE_LINE " + CREATE_LINE);
				VOICE_COMMAND =  CREATE_LINE;
				break;

			  case EXIT_VOICE_MAP:
				mesg("VOICE_MAP_COMMANDS : EXIT_VOICE_MAP " + EXIT_VOICE_MAP);
				VOICE_COMMAND =  EXIT_VOICE_MAP;
				break;


			  default:
				  mesg("UNKNOWN VOICE_MAP_COMMAND : reset to 0");
				  VOICE_REGION = 0;
				  VOICE_COMMAND = VOICE_MAP_COMMAND_DEFAULT;
			}

			// Send the command to the calling program.

			break;

		  default: 
			mesg("UNKNOWN VOICE_REGION : reset to 0");
			VOICE_REGION = 0;
			VOICE_COMMAND = 0;
		
		}

	};

};      
/*
	 Method that retrieves contents of a file, It uses a depricated API
	 readline() so i desided to go with the servereval class contining all
	 the required information internaly so ther is no need to cary arround
	 a data file to load up strings with... I may chang my mind in the 
	 and go back to a data file containing the command strings and values.
	 reading them in and looping through them for evaluation...

	  
	private  void GetFile(String file_name) {
	   //buffer to get all the lines in the file
	  StringBuffer buff = new StringBuffer();
		
	  File f = new File(file_name);
	  boolean b = (f.exists() || f.canRead());
	  if (!b)
		  mesg(" File either doesnt exists or is unreadable");
	  try {
		DataInputStream f_in = new DataInputStream(new 
					BufferedInputStream ( new 
					FileInputStream(file_name)));
	
		while(f_in.available() !=0) {
			String line = f_in.readLine();
			buff.append(line +"\n");
			dataout.writeUTF(line);
			dataout.flush();
		}
		dataout.writeUTF("EndOfFile");
		dataout.flush();
	  } catch (IOException ioE) {
		System.out.println("Error in handling file");
	  }
	
	}
*/
