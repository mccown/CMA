package RMIEngine;

import MapItems.ScreenObject.*;

//***********************************************************************
/** Class: ServerInterface 
  * Purpose:  Handle the RMI between clients of the NewMap application.
  * Methods:
  * 	newGraphicIcon()
  * 	newTextIcon()
  * 	newLine()
  *	moveObject()
  *	register()
  *	unRegister()
  * Author:  Steven H. McCown
  * Last Modified:  June 26, 1997
  */
public interface ServerInterface extends java.rmi.Remote 
{
	//---------------------------------------------------------------
	/** Method:  newGraphicIcon()
	  * Params:  
	  * 	int x - x coordinate
	  *	int y - y coordinate
	  *	String iconFile - relative path of the icon file
	  *	int iff - Identification Friend or Foe
	  * Return:  void
	  * Purpose:  receive the new icon command from a client, set the 
	  * 	objectID, and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newGraphicIcon(int x, int y, String iconFile, int iff)
	    	throws java.rmi.RemoteException;

	//---------------------------------------------------------------
	/** Method:  newTextIcon()
	  * Params:  
	  * 	int x - x coordinate
	  *	int y - y coordinate
	  *	String label - icon label
	  *	int iff - Identification Friend or Foe
	  * Return:  void
	  * Purpose:  receive the new icon command from a client, set the 
	  * 	objectID, and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newTextIcon(int x, int y, String label, int iff)
	    	throws java.rmi.RemoteException;

	//---------------------------------------------------------------
	/** Method:  newLine()
	  * Params:  
	  * 	int x0 - x0 coordinate
	  *	int y0 - y0 coordinate
	  * 	int x1 - x1 coordinate
	  *	int y1 - y1 coordinate
	  * Return:  void
	  * Purpose:  receive the new line command from a client, set the 
	  * 	objectID, and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void newLine(int x0, int y0, int x1, int y1)
	    	throws java.rmi.RemoteException;

	//---------------------------------------------------------------
	/** Method:  moveObject()
	  * Params:  the command string to be broadcast
	  * Return:  
	  *	int objectID - object's ID
	  *	int xOffset - X offset of the object
	  *	int yOffset - Y offset of the object
	  * Purpose:  receive the command from a user and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 2, 1997
	  */
	public void moveObject(int objectID, int xOffset, int yOffset)
	    	throws java.rmi.RemoteException;

	//---------------------------------------------------------------
	/** Method:  deleteObject()
	  * Params:
	  *     int objectID - object's ID
	  * Return:  void
	  * Purpose:  receive a deleteObject request from the user
	  *		and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 21, 1997
	  */
	public void deleteObject(int objectID)
	        throws java.rmi.RemoteException;

        //---------------------------------------------------------------
        /** Method:  LoadMap()
          * Params:  String mapName - map file name
          * Return:  void
          * Purpose:  Handle load map request.
          * Author:  Steven H. McCown
          * Last Modified:  June 26, 1997
          */
        public void LoadMap(String mapName)
                throws java.rmi.RemoteException;
     
	//---------------------------------------------------------------
	/** Method:  register()
	  * Params:  RMIEngine.ClientInterface obj - a handle to the
	  *     client process requesting registration.
	  * Return:  id of the new client.
	  * Purpose:  Register client processes with the server.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 30, 1997
	  */
	public int register(RMIEngine.ClientInterface obj)
		throws java.rmi.RemoteException;

	//---------------------------------------------------------------
	/** Method:  unRegister()
	  * Params:  int clientID - id of the client requesting to
	  *	unregister. 
	  * Return:  true if the un-registration was successful, otherwise false
	  * Purpose:  un-register client processes with the server.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 18, 1997
	  */
	public boolean unRegister(int clientID)
		throws java.rmi.RemoteException;
}
