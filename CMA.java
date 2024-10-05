import java.awt.*;
import java.awt.image.*;
import java.awt.Cursor.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.rmi.*;
import java.rmi.server.*;
import java.net.InetAddress;
import RMIEngine.*;
import MapItems.*;
import MapItems.ScreenObjectTypes.*;
import VoiceInput.*;

//***********************************************************************
/** Class:  CMA 
  * Purpose:  Creates a map interaction applet allowing the creation 
  *	and placement of icons.
  * Methods:
  *	main()
  *	init() 
  *	setHostName()	
  *	createLine()
  *	initiateMoveObject()
  *	initiateDeleteObject()
  *	ClientInterface.newGraphicIcon()
  *	ClientInterface.newTextIcon()
  *	ClientInterface.newLine()
  *	ClientInterface.moveObject()
  *	ClientInterface.LoadMap()
  *	selectBackgroundMap()
  *	ConnectToRMIServer()
  *	DisconnectFromRMIServer()
  *	EnableVoiceConnection()
  *	CreateMenu()
  *	AddMenuItem()
  *	AddMenuItem()
  *	createCascadedMenu()
  *	actionPerformed(ActionEvent evt)
  *	executeAction()
  *	checkToLoadIcon()
  *	setCoordString()
  *	getAppletInfo()
  * Author:  Steven H. McCown
  * Last Modified:  June 24, 1997
  */
public class CMA extends Frame 
	implements ActionListener, RMIEngine.ClientInterface, java.io.Serializable
{
	MapPanel panel;
	Label coordinateLabel;
        private RMIEngine.ServerInterface RMIServer;

	// clientID is assigned by the server and should
	// *ONLY* be changed (indirectly by the server)
	// during register() and unRegister() requests.
	private int clientID = -1;

	private VoiceInput.Server voiceServer = null;

	private int iff = ScreenObjectTypes.UNKNOWN;
	private MenuItem friendlyMenuItem = null;
	private MenuItem hostileMenuItem = null;
	private MenuItem unknownMenuItem = null;

	private String FriendlyString = "Friendly";
	private String HostileString = "Hostile";
	private String NeutralString = "Neutral";
       	private String UnknownString = "Unknown";

	// Maintain a reference to the local host, since it is 
	// used as the RMIServer when running in 'local' mode.
	private String localHostName = "";

	// The target host name when connected to a remote host.
	private String targetHostName = "";

	//***************************************************************
	/** Class:  FileMenuItem
	  * Purpose:  a basic structure to hold a menuString and a 
	  *	filePath for graphic icons (*.gif images).
	  * Methods:
	  *	FileMenuItem()
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	private class FileMenuItem
	{
		public String menuString;
		public String filepath;

		public FileMenuItem(String menuString, 
			String filepath)
		{
			this.menuString = menuString;
			this.filepath = filepath;
		}
	}

	String sep = (new File(".")).separator;

	private String FSFMenuLabel = "FSF";
	private FileMenuItem[] FSFMenuItems = 
	{
		new FileMenuItem("Air Defense", "icons"+sep+"fsc"+sep+"air_defense.gif"),
		new FileMenuItem("Anti Tank", "icons"+sep+"fsc"+sep+"anti_tank.gif"),
		new FileMenuItem("Armor", "icons"+sep+"fsc"+sep+"armor.gif"),
		new FileMenuItem("Armored Cavalry", "icons"+sep+"fsc"+sep+"armored_cavalry.gif"), 
		new FileMenuItem("Chemical", "icons"+sep+"fsc"+sep+"chemical.gif"),
		new FileMenuItem("Infantry", "icons"+sep+"fsc"+sep+"infantry.gif"),
		new FileMenuItem("Maintenance", "icons"+sep+"fsc"+sep+"maintenance.gif"), 
		new FileMenuItem("Mechanized Infantry", "icons"+sep+"fsc"+sep+"mechanized_infantry.gif"),
		new FileMenuItem("Medical", "icons"+sep+"fsc"+sep+"medical.gif"),
		new FileMenuItem("Signal Comm", "icons"+sep+"fsc"+sep+"signal_comm.gif")
	};

	private String targetMenuLabel = "Targets";
	private FileMenuItem[] targetMenuItems = 
	{
		new FileMenuItem("AAA", "icons"+sep+"targets"+sep+"aaa.gif"),
		new FileMenuItem("Aircraft Storage Hanger", "icons"+sep+"targets"+sep+"aircraft_storage_hanger.gif"),
		new FileMenuItem("Airport1", "icons"+sep+"targets"+sep+"airport1.gif"),
		new FileMenuItem("Airport2", "icons"+sep+"targets"+sep+"airport2.gif"),
		new FileMenuItem("Airport3", "icons"+sep+"targets"+sep+"airport3.gif"),
		new FileMenuItem("Airport4", "icons"+sep+"targets"+sep+"airport4.gif"),
		new FileMenuItem("Airport5", "icons"+sep+"targets"+sep+"airport5.gif"),
		new FileMenuItem("Airport6", "icons"+sep+"targets"+sep+"airport6.gif"),
		new FileMenuItem("Airport7", "icons"+sep+"targets"+sep+"airport7.gif"),
		new FileMenuItem("Airport8", "icons"+sep+"targets"+sep+"airport8.gif"),
		new FileMenuItem("Airport9", "icons"+sep+"targets"+sep+"airport9.gif"),
		new FileMenuItem("Factory Indus Complex", "icons"+sep+"targets"+sep+"factory_indus_complex.gif"),
		new FileMenuItem("Highway Bridge", "icons"+sep+"targets"+sep+"highway_bridge.gif"), 
		new FileMenuItem("Hydroelectric Dam", "icons"+sep+"targets"+sep+"hydroelectric_dam.gif"), 
		new FileMenuItem("Marker Reference Pt", "icons"+sep+"targets"+sep+"marker_reference_pt.gif"),
		new FileMenuItem("Mobile Missle Launcher", "icons"+sep+"targets"+sep+"mobile_missle_launcher.gif"),
		new FileMenuItem("Radar Site", "icons"+sep+"targets"+sep+"radar_site.gif"),
		new FileMenuItem("Railroad Bridge", "icons"+sep+"targets"+sep+"railroad_bridge.gif"),
		new FileMenuItem("SAM", "icons"+sep+"targets"+sep+"sam.gif"), 
		new FileMenuItem("Tank Armored Vehicle", "icons"+sep+"targets"+sep+"tank_armored_vehicle.gif"),
		new FileMenuItem("Truck Convoy", "icons"+sep+"targets"+sep+"truck_convoy.gif"),
		new FileMenuItem("Unspecified Target", "icons"+sep+"targets"+sep+"unspecified_target.gif")
	};

	//---------------------------------------------------------------
	/** Method:  main()
	  * Params:  String[] args - command line arguments.
	  * Return: void.
	  * Purpose:  Starts CMA as an application.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 7, 1997
	  */
	public static void main(String[] args)
	{
		CMA collaborativeMapAnnotator = new CMA();
		collaborativeMapAnnotator.setBackground(Color.white);
		collaborativeMapAnnotator.show();
		collaborativeMapAnnotator.init();
	}

	//---------------------------------------------------------------
	/** Method:  init()
	  * Params:  none
	  * Return: void.
	  * Purpose:  CMA initialization.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void init() 
	{
		addWindowListener(new Wadapt());

		CreateMenu();

		setLayout(new BorderLayout());

		panel = new MapPanel(this);
		add("Center", panel);
		Panel p = new Panel();
		add("South", p);

		// Version 1.1.1 of the JDK seemed to have a problem
		// drawing any subsequent text longer than the 
		// initial string.  So, the dashes were added
		// as mere placeholders.
		coordinateLabel = new Label("--------------------");
		p.add(coordinateLabel);

		Toolkit t = getToolkit();

		// Use a default minimize icon.  Seems to 
		// be used only in Windows 95/NT versions.
		setIconImage(t.getImage("icon.gif"));

		setTitle("Collaborative Map Annotator (not connected)");
		panel.setBackground(t.getImage("./maps/default.gif"));

		// Set the local host name.
		try
		{
			localHostName = new String((InetAddress.getLocalHost()).getHostName());
		}
		catch (Exception e)
		{
			System.out.println("CMA.init(): error loading localHostname.");
			e.printStackTrace();
		}

		// Set the current host name.
		setHostName(localHostName);
	}

	//---------------------------------------------------------------
	/** Method:  setHostName()
	  * Params:  
	  * 	String hostName - host address name string	
	  * Return:  void
	  * Purpose:  to set the host name of the RMI server.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 7, 1997
	  */
	public void setHostName(String hostName)
	{
		this.targetHostName = new String(hostName);
	}

	//---------------------------------------------------------------
	/** Method:  createLine()
	  * Params:  
	  * 	int x0 - x0 coordinate
	  *	int y0 - y0 coordinate
	  * 	int x1 - x1 coordinate
	  *	int y1 - y1 coordinate
	  * Return:  void
	  * Purpose:  call the server to create a line.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void createLine(int x0, int y0, int x1, int y1)
	{
		try
		{
			RMIServer.newLine(x0, y0, x1, y1);
		}  
		catch (java.rmi.RemoteException e)
		{
			System.out.println("Error calling RMIServer.newLine()");
			e.printStackTrace();
		}
		catch (java.lang.NullPointerException e)
		{
			MessageBox mbox = new MessageBox(this, "Error",
				"You must first connect to a server before " +
				"using this option.");
			mbox.show();
		}
	}

	//---------------------------------------------------------------
	/** Method:  initiateMoveObject()
	  * Params:  
	  *	int objectID - objects' ID
	  * 	int xOffset - x coordinate offset
	  *	int yOffset - y coordinate offset
	  * Return:  void
	  * Purpose:  calls to the server to request the move of an object.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void initiateMoveObject(int objectID, int xOffset, int yOffset)
	{
		try
		{
			RMIServer.moveObject(objectID, xOffset, yOffset);
		}  
		catch (java.rmi.RemoteException e)
		{
			System.out.println("Error calling RMIServer.moveObject()");
			e.printStackTrace();
		}
		catch (java.lang.NullPointerException e)
		{
			MessageBox mbox = new MessageBox(this, "Error",
				"You must first connect to a server before " +
				"using this option.");
			mbox.show();
		}
	}

	//---------------------------------------------------------------
	/** Method:  initiateDeleteObject()
	  * Params:  
	  *	int objectID - objects' ID
	  * Return:  void
	  * Purpose:  calls to the server to request that an object be deleted.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 22, 1997
	  */
	public void initiateDeleteObject(int objectID)
	{
		try
		{
			RMIServer.deleteObject(objectID);
		}  
		catch (java.rmi.RemoteException e)
		{
			System.out.println("Error calling RMIServer.deleteObject()");
			e.printStackTrace();
		}
		catch (java.lang.NullPointerException e)
		{
			MessageBox mbox = new MessageBox(this, "Error",
				"You must first connect to a server before " +
				"using this option.");
			mbox.show();
		}
	}

	//---------------------------------------------------------------
	/** Method:  ClientInterface.newGraphicIcon()
	  * Params:  
	  *	int objectID - icons' objectID
	  * 	int x - x coordinate
	  *	int y - y coordinate
	  *	String iconFile - relative path of the icon file
	  *	int iff - Identification Friend or Foe
	  * Return:  void
	  * Purpose:  receive the newGraphicIcon command from the server
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newGraphicIcon(int objectID, int x, int y, 
		String iconFile, int iff)
	    	throws java.rmi.RemoteException
	{
  		panel.newGraphicIcon(getToolkit(), objectID, 
			x, y, iconFile, iff);
	}

	//---------------------------------------------------------------
	/** Method:  ClientInterface.newTextIcon()
	  * Params:  
	  *	int objectID - icons' objectID
	  * 	int x - x coordinate
	  *	int y - y coordinate
	  *	String label - icons' text label
	  *	int iff - Identification Friend or Foe
	  * Return:  void
	  * Purpose:  receive the newTextIcon command from the server
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newTextIcon(int objectID, int x, int y, 
		String label, int iff)
	    	throws java.rmi.RemoteException
	{
		panel.newTextIcon(objectID, x, y, label, iff);
	}

	//---------------------------------------------------------------
	/** Method:  ClientInterface.newLine()
	  * Params:  
	  *	int objectID - icons' objectID
	  * 	int x0 - x0 coordinate
	  *	int y0 - y0 coordinate
	  * 	int x1 - x1 coordinate
	  *	int y1 - y1 coordinate
	  * Return:  void
	  * Purpose:  receive the newLine command from the server
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newLine(int objectID, int x0, int y0, 
		int x1, int y1)
	    	throws java.rmi.RemoteException
	{
		panel.drawLine(objectID, x0, y0, x1, y1);
	}

	//---------------------------------------------------------------
	/** Method:  ClientInterface.moveObject()
	  * Params:
	  *     int objectID - object's ID
	  *     int xOffset - xOffset of the object
	  *     int xOffset - yOffset of the object
	  * Return:  void
	  * Purpose:  receive a moveObject request from the RMI server.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 2, 1997
	  */
	public void moveObject(int objectID, int xOffset, int yOffset)
		throws java.rmi.RemoteException
	{
		panel.moveObject(objectID, xOffset, yOffset);
	}

	//---------------------------------------------------------------
	/** Method:  ClientInterface.deleteObject()
	  * Params:
	  *     int objectID - object's ID
	  * Return:  void
	  * Purpose:  receive a deleteObject request from the RMI server.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 21, 1997
	  */
	public void deleteObject(int objectID)
		throws java.rmi.RemoteException
	{
		panel.deleteObject(objectID);
	}

        //---------------------------------------------------------------
        /** Method:  ClientInterface.LoadMap()
          * Params:  String mapName - map file name
          * Return:  void
          * Purpose:  Process a load map request from the server.
	  *	Maps will ALWAYS be loaded from the ./maps/ directory.
	  *	This will eliminate any confusion as to where the maps
	  *	are located on each of the clients and not require the
	  *	overhead of downloading.
          * Author:  Steven H. McCown
          * Last Modified:  June 26, 1997
          */
        public void LoadMap(String mapName)
                throws java.rmi.RemoteException
	{
		Toolkit t = getToolkit();
		panel.setBackground(t.getImage("./maps/" + mapName));
		panel.repaint();
	}

	//---------------------------------------------------------------
	/** Method:  selectBackgroundMap()
	  * Params:  Toolkit t
	  * Return: void.
	  * Purpose:  allows the user to select and then load a new map.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 14, 1997
	  */
	private void selectBackgroundMap(Toolkit t)
	{
		FileDialog fd = new FileDialog(this, "Map Selection");
		fd.setDirectory("./maps");
		fd.setFile("*.gif");
		fd.show();

		String filename = fd.getFile();
		String directory = fd.getDirectory();

		if ((filename != null) && (directory != null))
		{
			if (filename.equals("") != true)
			{
				String filepath = new String(directory + filename);

				// Make the load map request by calling
				// the RMIServer.  This will allow the
				// map to be loaded on all clients.
				try
				{
					RMIServer.LoadMap(filename);
				}
				catch (java.rmi.RemoteException e)
				{
					System.out.println("Error calling RMIServer.LoadMap()");
					e.printStackTrace();
				}
				catch (java.lang.NullPointerException e)
				{
					MessageBox mbox = new MessageBox(this, "Error",
						"You must first connect to a server before " +
						"using this option.");
					mbox.show();
				}
			}
		}
		panel.repaint();
	}

	//---------------------------------------------------------------
	/** Method:  ConnectToRMIServer()
	  * Params:  none
	  * Return: void.
	  * Purpose:  Connect to a running RMIServer.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 9, 1997
	  */
	private void ConnectToRMIServer()
	{
		if (clientID != -1)
		{
			MessageBox mbox = new MessageBox(this, "Connect",
				"CMA is already connected to an RMIServer.  " +
				"Please use Disconnect first.");
			mbox.show();
			
			return;
		}

		// Setup the RMIEngine communications stuff.
	        try
        	{
			UnicastRemoteObject.exportObject(this);

			// The RMI server is setup to listen to 
			// port 8444 with the name 'CMAServer'.
			RMIServer = (RMIEngine.ServerInterface)Naming.lookup(
				"//" + targetHostName + ":8444/CMAServer");
			clientID = RMIServer.register(this);

			setTitle("Collaborative Map Annotator (connected to:  " + targetHostName + ")");
        	}
	        catch (Exception e)
        	{
			System.out.println("CMA: an RMI " + 
				"initialization exception occurred:");
			e.printStackTrace();
	        }
	}

	//---------------------------------------------------------------
	/** Method:  DisconnectFromRMIServer()
	  * Params:  none
	  * Return: void.
	  * Purpose:  Disconnect from an RMIServer.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 9, 1997
	  */
	private void DisconnectFromRMIServer()
	{
		if (clientID == -1)
		{
			MessageBox mbox = new MessageBox(this, "Disconnect",
				"CMA is not connected to an RMIServer.  " + 
				"Please use Connect first.");
			mbox.show();
			
			return;
		}

		try
		{
			boolean unRegistered = RMIServer.unRegister(clientID);
			if (unRegistered == true)
			{
				panel.clearAllObjects();
				RMIServer = (RMIEngine.ServerInterface)null;
			}
		}
		catch (Exception e)
		{
			System.out.println("CMA: an RMI " + 
				"disconnect request exception occurred:");
			e.printStackTrace();
		}

		clientID = -1;
	}

	//---------------------------------------------------------------
	/** Method:  EnableVoiceConnection()
	  * Params:  none
	  * Return: void.
	  * Purpose:  Open a listening socket for voice commands.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 9, 1997
	  */
	private void EnableVoiceConnection()
	{
		// Start the voice server thread.
		voiceServer = new VoiceInput.Server((Component)this);
		voiceServer.start();
	}

	//---------------------------------------------------------------
	/** Method:  CreateMenu()
	  * Params:  none
	  * Return: void.
	  * Purpose:  creates the pulldown menu structure.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	public void CreateMenu()
	{
		MenuBar mbar = new MenuBar();
		Menu m = new Menu("File");
		AddMenuItem(m, "Open...");
		AddMenuItem(m, "Load Map...");
		m.addSeparator();
		AddMenuItem(m, "Connect...");
		AddMenuItem(m, "Disconnect...");
		AddMenuItem(m, "Enable Voice Connection");
		m.addSeparator();
		AddMenuItem(m, "Save");
		AddMenuItem(m, "Save As...");
		m.addSeparator();
		AddMenuItem(m, "Exit..."); 
		mbar.add(m);

		m = new Menu("Edit");
		AddMenuItem(m, "Cut");
		AddMenuItem(m, "Copy");
		AddMenuItem(m, "Delete");
		AddMenuItem(m, "Paste");
		mbar.add(m);

		m = new Menu("Notations");
		AddMenuItem(m, "Line", new MenuShortcut('L'));
		AddMenuItem(m, "Text Icon", new MenuShortcut('T'));

		Menu icons = new Menu(FriendlyString);
		icons.add(createCascadedMenu(FSFMenuItems, FSFMenuLabel, 
			FriendlyString));
		icons.add(createCascadedMenu(targetMenuItems, targetMenuLabel,
			FriendlyString));
		m.add(icons);

		icons = new Menu(HostileString);
		icons.add(createCascadedMenu(FSFMenuItems, FSFMenuLabel,
			HostileString));
		icons.add(createCascadedMenu(targetMenuItems, targetMenuLabel,
			HostileString));
		m.add(icons);

		icons = new Menu(UnknownString);
		icons.add(createCascadedMenu(FSFMenuItems, FSFMenuLabel,
			UnknownString));
		icons.add(createCascadedMenu(targetMenuItems, targetMenuLabel,
			UnknownString));
		m.add(icons);

		icons = new Menu(NeutralString);
		icons.add(createCascadedMenu(FSFMenuItems, FSFMenuLabel,
			NeutralString));
		icons.add(createCascadedMenu(targetMenuItems, targetMenuLabel,
			NeutralString));
		m.add(icons);
		mbar.add(m);

		m = new Menu("Help");
		AddMenuItem(m, "Index");
		AddMenuItem(m, "About");
		mbar.add(m);

		setMenuBar(mbar);
	}

	//---------------------------------------------------------------
	/** Method:  AddMenuItem()
	  * Params:  
	  *	Menu m - pulldown menu
	  *	String str - text to go on the new MenuItem
	  * Return: void.
	  * Purpose:  creates the pulldown menu item.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	private void AddMenuItem(Menu m, String str)
	{
		AddMenuItem(m, str, null);
	}

	//---------------------------------------------------------------
	/** Method:  AddMenuItem()
	  * Params:  
	  *	Menu m - pulldown menu
	  *	String str - text to go on the new MenuItem
	  *	MenuShortcut msc - shortcut key character
	  * Return: void.
	  * Purpose:  creates the pulldown menu item.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	private void AddMenuItem(Menu m, String str, MenuShortcut msc)
	{
		MenuItem mi = new MenuItem(str, msc);
		mi.addActionListener(this);
		m.add(mi);
	}

	//---------------------------------------------------------------
	/** Method:  createCascadedMenu()
	  * Params:  
	  *	FileMenuItem[] items - an array of FileMenuItem 
	  *		structures to be included in the menu
	  *	String prefix - menu prefix to be prepended to the
	  *		action command string (e.g., 'FSF' or 'target').
	  *	String iffString - (e.g., friendly, hostile, 
	  *		unknown, neutral)
	  * Return: void.
	  * Purpose:  creates the pulldown menu item.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	private Menu createCascadedMenu(FileMenuItem[] items, String prefix,
		String iffString)
	{
		Menu cascadedMenu = new Menu(prefix);
		for (int i = 0; i < items.length; i++)
		{
			MenuItem m = new MenuItem(items[i].menuString);
			m.addActionListener(this);
			m.setActionCommand(iffString + " " + 
				prefix + " " + items[i].menuString);
			cascadedMenu.add(m);
		}

		return cascadedMenu;
	}

	//---------------------------------------------------------------
	/** Method:  actionPerformed()
	  * Params:  
	  *	ActionEvent evt - event that caused this action.
	  * Return: void.
	  * Purpose:  handles action events
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	public void actionPerformed(ActionEvent evt)
	{
		executeAction(evt.getActionCommand());
	}

	//---------------------------------------------------------------
	/** Method:  processEvent()
	  * Params:  
	  *	AWTEvent evt - event that caused this action.
	  * Return: void.
	  * Purpose:  this function was overridden to allow
	  *	VOICE_EVENT_ID type events to be handled by
	  *	CMA.   This is useful since VoiceInput
	  *	is passed a reference to CMA (the reference is 
	  *	to an object of type component).  This is nice 
	  *	because it totally encapsulates VoiceInput from 
	  *	CMA or any other package that may choose to 
	  *	implement it.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	protected void processEvent(AWTEvent e) 
	{
		// Only handle VOICE_EVENT_ID events and let the
		// parent class handle the rest normally.
		if (e.getID() == VoiceInput.Server.VOICE_EVENT_ID)
		{
			executeAction(((ActionEvent)e).getActionCommand());
		}
		else if (e.getID() == DialogEvents.CONNECT_TO_HOST)
		{
			String hostName = ((ActionEvent)e).getActionCommand();

			// Set the user-specified host name 
			setHostName(hostName);

			// Connect to the current hosts' RMI server.
			ConnectToRMIServer();
		}
		else
		{
			super.processEvent(e);
		}
	}

	//---------------------------------------------------------------
	/** Method:  executeAction()
	  * Params:  
	  *	String the action command string.
	  * Return: void.
	  * Purpose:  handles action events.  This function is used
	  *	instead of just having actionPerformed() handle
	  *	these requests so that it can also be used by
	  *	the collaboration routines.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	public void executeAction(String str)
	{
		// ------------------------------------
		// Handle the menu events.
		if(str.equals("Open..."))
 		{
			//panel.readAllObjects();			
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Load Map..."))
 		{
			selectBackgroundMap(getToolkit());
		}
		else if(str.equals("Connect..."))
 		{
			String t = new String("abu");
			GetString getString = new GetString(this, "Connect", t);
			getString.show();

			// Clear the current objects when 
			// connecting to an existing session.
			panel.clearAllObjects();
		}
		else if(str.equals("Disconnect..."))
 		{
			DisconnectFromRMIServer();
		}
		else if(str.equals("Enable Voice Connection"))
 		{
			EnableVoiceConnection();
		}
		else if(str.equals("Save"))
 		{
			//panel.saveAllObjects();
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Save As..."))
 		{
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Exit...")) 
 		{
			System.exit(0);
		}
		else if(str.equals("Cut"))
 		{
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Copy"))
 		{
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Delete"))
 		{
			panel.deleteSelectedObject();
		}
		else if(str.equals("Paste"))
		{
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("Line"))
		{
			panel.startLineDrawingMode();
		}
		else if(str.equals("Text Icon"))
		{
			try
			{
				RMIServer.newTextIcon(0, 0, "Text Icon", iff);
			}
			catch (java.rmi.RemoteException e)
			{
				System.out.println("Error calling RMIServer.newTextIcon()");
				e.printStackTrace();
			}
			catch (java.lang.NullPointerException e)
			{
				MessageBox mbox = new MessageBox(this, "Error",
					"You must first connect to a server before " +
					"using this option.");
				mbox.show();
			}
		}
		else if(str.equals("Index"))
		{
			MessageBox mbox = new MessageBox(this, "Feature",
				"This feature has not been implemented.");
			mbox.show();
		}
		else if(str.equals("About"))
		{
			MessageBox mbox = new MessageBox(this, "About",
				"Collaborative Map " + 
				"Annotator Version 1.0 Release 2\n\n" + 
				"Written By:\n\n" + 
				"Steven H. McCown\n\n\n" +
				"Special thanks go to Francis A. DiLego, Jr.\n\n" +
				"for creating the VoiceInput class.");
			mbox.show();
		}
		else
		{
			checkToLoadIcon(str);
		}
	}

	//---------------------------------------------------------------
	/** Method:  checkToLoadIcon()
	  * Params:  
	  *	String str - command string of the icon to be load.
	  * Return: void.
	  * Purpose:  decode the icon string to determine which 
	  *	icon to load
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	private void checkToLoadIcon(String str)
	{
		int iffStringLength = 0;

		if (str.startsWith(FriendlyString))
		{
 			iff = ScreenObjectTypes.FRIENDLY;
			iffStringLength = FriendlyString.length();
		}
		else if (str.startsWith(HostileString))
		{
			iff = ScreenObjectTypes.HOSTILE;
			iffStringLength = HostileString.length();
		}
		else if (str.startsWith(NeutralString))
		{
			iff = ScreenObjectTypes.NEUTRAL;
			iffStringLength = NeutralString.length();
		}
		else if (str.startsWith(UnknownString))
		{
			iff = ScreenObjectTypes.UNKNOWN;
			iffStringLength = UnknownString.length();
		}
		else
		{
			System.out.println("Bad iff string >" + str + "<");
		}

		// Add 1 for the space between words.
 		String tempStr = str.substring(iffStringLength + 1, str.length());
		if (tempStr.startsWith(FSFMenuLabel))
		{
			// Add 1 for the space between words.
			String temp = 
				tempStr.substring(FSFMenuLabel.length() + 1, 
				tempStr.length());

			boolean itemFound = false; 
			int i = 0;  
			int nItems = FSFMenuItems.length;
			for (; (itemFound == false) && (i < nItems); i++)
			{
				if (temp.equals(FSFMenuItems[i].menuString))
				{
					itemFound = true;
				}
			}

			// Decrement to fix the index position 
			// after the 'for' exits.
			i--;

			if (itemFound == true)
			{
				try
				{
					// RMI command to the server.
					RMIServer.newGraphicIcon(0, 0, 
						FSFMenuItems[i].filepath, iff);
				}
				catch (java.rmi.RemoteException e)
				{
					System.out.println("Error calling RMIServer.newGraphicIcon()");
	                        	e.printStackTrace();
				}
				catch (java.lang.NullPointerException e)
				{
					MessageBox mbox = new MessageBox(this, "Error",
						"You must first connect to a server before " +
						"using this option.");
					mbox.show();
				}
			}
		}
		else if (tempStr.startsWith(targetMenuLabel))
		{
			// Add 1 for the space between words.
			String temp = 
				tempStr.substring(targetMenuLabel.length() + 1, 
				tempStr.length());

			boolean itemFound = false;
			int i = 0;  
			int nItems = targetMenuItems.length;
			for (; (itemFound == false) && (i < nItems); i++)
			{
				if (temp.equals(targetMenuItems[i].menuString))
				{
					itemFound = true;
				}
			}

			// Decrement to fix the index position 
			// after the 'for' exits.
			i--;

			if (itemFound == true)
			{
				try
				{
					// RMI command to the server.
					RMIServer.newGraphicIcon(0, 0, 
						targetMenuItems[i].filepath, iff);
				}
				catch (java.rmi.RemoteException e)
				{
					System.out.println("Error calling RMIServer.newGraphicIcon()");
					e.printStackTrace();
				}
				catch (java.lang.NullPointerException e)
				{
					MessageBox mbox = new MessageBox(this, "Error",
						"You must first connect to a server before " +
						"using this option.");
					mbox.show();
				}
			}
		}
	}

	//---------------------------------------------------------------
	/** Method:  setCoordString()
	  * Params:  
	  *	String coordString - coordinates.
	  * Return: void
	  * Purpose:  Set and display the coordinate string.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void setCoordString(String coordString)
	{
		coordinateLabel.setText(coordString);
	}

	//---------------------------------------------------------------
	/** Method:  getAppletInfo()
	  * Params:  none
	  * Return: String - applet information.
	  * Purpose:  return the applet information.  Currently,
	  *	the CMA cannot be run as an applet, but that 
	  *	may change.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public String getAppletInfo() 
	{
		return "Collaborative Map Annotator by Steven H. McCown";
	}
}

//***********************************************************************
/** Class:  Wadapt
  * Purpose:  Creates a WindowAdapter to capture and handle 
  *	window events.
  * Methods:
  *	windowClosing() 
  * Author:  Steven H. McCown
  * Last Modified:  May 20, 1997
  */
class Wadapt extends WindowAdapter
{
	public void windowClosing(WindowEvent evt)
	{
		// Clean up when the window closes.
		Frame frm = (Frame)evt.getSource();
		frm.setVisible(false);
		frm.dispose();
		System.exit(0);
	}
}
