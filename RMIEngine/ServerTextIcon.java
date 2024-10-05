package RMIEngine;

import java.rmi.RemoteException;

//***********************************************************************
/** Class: ServerTextIcon
  * Purpose:  Used to maintain a list of text icons independent of 
  *	the client GUI applications.
  * Methods:
  * 	ServerTextIcon()
  * 	CreateOnClient()
  * Author:  Steven H. McCown
  * Last Modified:  June 4, 1997
  */
class ServerTextIcon extends ServerObject
{
	private String label;
	private int iff;

	//---------------------------------------------------------------
	/** Method:  ServerTextIcon()
	  * Params:  
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String label - icon label string
	  *	int iff - Identification Friend or Foe
	  * Return:  
	  * Purpose:  set the x and y coordinates of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public ServerTextIcon(int x, int y, String label, int iff)
	{
		super(x, y);
		this.label = label;
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
 			client.newTextIcon(objectID, x, y, label, iff);
		}
		catch (RemoteException e)
		{
			rValue = false;
		}

		return rValue;
	}
}
