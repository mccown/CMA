package RMIEngine;

import java.rmi.RemoteException;

//***********************************************************************
/** Class: ServerLineObject
  * Purpose:  Used to maintain a list of line objects independent of 
  *	the client GUI applications.
  * Methods:
  * 	ServerLineObject()
  *	translate()
  * 	CreateOnClient()
  * Author:  Steven H. McCown
  * Last Modified:  June 4, 1997
  */
class ServerLineObject extends ServerObject
{
	private int x1;
	private int y1;

	//---------------------------------------------------------------
	/** Method:  ServerLine()
	  * Params:  
	  *	int x0 - x0 coordinate
	  *	int y0 - y0 coordinate
	  *	int x1 - x1 coordinate
	  *	int y1 - y1 coordinate
	  * Return:  
	  * Purpose:  set the x0, y0 and x1, y1 coordinates of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public ServerLineObject(int x0, int y0, int x1, int y1)
	{
		super(x0, y0);
		this.x1 = x1;
		this.y1 = y1;
	}

	public void translate(int newX, int newY)
	{
		int difX = x1 - x;
		int difY = y1 - y;

		super.translate(newX, newY);

		// Adjust the endpoint for the new location.
		this.x1 = x + difX;
		this.y1 = y + difY;
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
			client.newLine(objectID, x, y, x1, y1);
		}
		catch (RemoteException e)
		{
			rValue = false;
		}

		return rValue;
	}
}
