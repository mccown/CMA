package RMIEngine;

import java.rmi.RemoteException;

//***********************************************************************
/** Class: ServerGraphicIcon
  * Purpose:  Used to maintain a list of graphic icons independent of 
  *	the client GUI applications.
  * Methods:
  * 	ServerGraphicIcon()
  * 	CreateOnClient()
  * Author:  Steven H. McCown
  * Last Modified:  June 4, 1997
  */
class ServerGraphicIcon extends ServerObject
{
	private String iconFile;
	private int iff;

	//---------------------------------------------------------------
	/** Method:  ServerGraphicIcon()
	  * Params:  
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String iconFile - icon file path string
	  *	int iff - Identification Friend or Foe
	  * Return:  void
	  * Purpose:  set the x and y coordinates of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public ServerGraphicIcon(int x, int y, 
		String iconFile, int iff)
	{
		super(x, y);
		this.iconFile = iconFile;
		this.iff = iff;
	}

	//---------------------------------------------------------------
	/** Method:  CreateOnClient()
	  * Params:  
	  *	ClientInterface client - client object
	  * Return:  void
	  * Purpose:  call the client to create this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public boolean CreateOnClient(ClientInterface client)
	{
		boolean rValue = true;

		try
		{
			client.newGraphicIcon(objectID, x, y, iconFile, iff);
		}
		catch (java.rmi.RemoteException e)
		{
			rValue = false;
		}

		return rValue;
	}
}
