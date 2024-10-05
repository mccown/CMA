package RMIEngine;

//***********************************************************************
/** Class: ServerObject
  * Purpose:  Used to maintain a list of screen objects independent of 
  *	the client GUI applications.
  * Methods:
  * 	ServerObject()
  * 	translate()
  * 	CreateOnClient()
  *	getObjectID()
  *	SetObjectID()
  *	getX()
  *	getY()
  *	setXY()
  *	register()
  * Author:  Steven H. McCown
  * Last Modified:  June 4, 1997
  */
public abstract class ServerObject
{
	protected int objectID;
	protected int x;
	protected int y;

	//---------------------------------------------------------------
	/** Method:  ServerObject()
	  * Params:  
	  *	int x - x coordinate
	  *	int y - y coordinate
	  * Return:  void
	  * Purpose:  create the ServerObject
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public ServerObject(int x, int y)
	{
		this.objectID = objectID;
		this.x = x;
		this.y = y;
	}

	//---------------------------------------------------------------
	/** Method:  translate()
	  * Params:  
	  *	int xOffset - new xOffset coordinate
	  *	int yOffset - new yOffset coordinate
	  * Return:  void
	  * Purpose:  translate the ServerObject
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void translate(int xOffset, int yOffset)
	{
		this.x += xOffset;
		this.y += yOffset;
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
	public abstract boolean 
		CreateOnClient(ClientInterface client);

	//---------------------------------------------------------------
	/** Method:  getObjectID()
	  * Params:  void
	  * Return:  
	  *	int objectID - objectID
	  * Purpose:  get the objectID of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public int getObjectID()
	{
		return objectID;		
	}

	//---------------------------------------------------------------
	/** Method:  setObjectID()
	  * Params:  
	  *	int objectID - objectID
	  * Return:  void 
	  * Purpose:  set the objectID of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void setObjectID(int objectID)
	{
		this.objectID = objectID;
	}

	//---------------------------------------------------------------
	/** Method:  getX()
	  * Params:  void
	  * Return:   
	  *	int x - x coordinate
	  * Purpose:  get the x-coordinate of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public int getX()
	{
		return x;
	}

	//---------------------------------------------------------------
	/** Method:  getY()
	  * Params:  void
	  * Return:   
	  *	int y - y coordinate
	  * Purpose:  get the y-coordinate of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public int getY()
	{
		return y;
	}

	//---------------------------------------------------------------
	/** Method:  setXY()
	  * Params:  
	  *	int x - x coordinate
	  *	int y - y coordinate
	  * Return:  void
	  * Purpose:  set the x and y coordinates of this object
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
