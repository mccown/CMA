package RMIEngine;

//***********************************************************************
/** Class: ClientInterfaceImpl
  * Purpose:  THIS CLASS IS ONLY USED AS A STUB SO THAT THE
  *	PACKAGE RMIEngine WILL COMPILE.  A REAL CLASS THAT
  *	IMPLEMENTS THE ClientInterface MUST BE CREATED 
  *	(SEE CMA.JAVA).
  * Methods:
  *	register()
  *	newGraphicIcon()
  *	newTextIcon()
  *	newLine()
  *	moveObject()
  *	deleteObject()
  *	LoadMap()
  * Author:  Steven H. McCown
  * Last Modified:  June 4, 1997
  */
public class ClientInterfaceImpl 
	implements ClientInterface
{
	//---------------------------------------------------------------
	/** Method:  newGraphicIcon()
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
	}

	//---------------------------------------------------------------
	/** Method:  newGraphicIcon()
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
	}

	//---------------------------------------------------------------
	/** Method:  newLine()
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
	}

	//---------------------------------------------------------------
	/** Method:  moveObject()
	  * Params:
	  *     int objectID - object's ID
	  *     int newX - new X location of the object
	  *     int newY - new Y location of the object
	  * Return:  void
	  * Purpose:  receive the command from a user and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 2, 1997
	  */
	public void moveObject(int objectID, int newX, int newY)
		throws java.rmi.RemoteException
	{
	}

	//---------------------------------------------------------------
	/** Method:  deleteObject()
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
	}

	 //---------------------------------------------------------------
        /** Method:  LoadMap()
          * Params:  String mapName - map file name
          * Return:  void
          * Purpose:  Process a load map request from the server.
          * Author:  Steven H. McCown
          * Last Modified:  June 26, 1997
          */
        public void LoadMap(String mapName)
                throws java.rmi.RemoteException
	{
	}
}
