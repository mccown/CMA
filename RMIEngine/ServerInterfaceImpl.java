package RMIEngine;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;
import GenericLinkedList.*;

//***********************************************************************
/** Class: ServerInterfaceImpl
  * Purpose:  Handle the RMI between clients of the NewMap application.
  * Methods:
  *     ServerInterfaceImpl() 
  * 	newGraphicIcon()
  * 	newTextIcon()
  * 	newLine()
  *	CreateOnAllClients()
  *	moveObject()
  *	deleteObject()
  *	LoadMap()
  *	LoadMapOnAClient()
  *	register()
  *	unRegister()
  *	main()
  * Author:  Steven H. McCown
  * Last Modified:  June 26, 1997
  */
class ServerInterfaceImpl extends UnicastRemoteObject
	implements ServerInterface 
{
	private LinkedList clientList = new LinkedList();
	private LinkedList objectList = new LinkedList();

	// This path is relative to the CMA client and not 
	// the RMIEngine!
	private String mapName = "default.gif";

	public final static int portID = 8444;

	//---------------------------------------------------------------
	/** Method:  ServerInterfaceImpl()
	  * Params:  none
	  * Return: an instantiated object 
	  * Purpose:  Create the server interface implementation. 
	  * Author:  Steven H. McCown
	  * Last Modified:  May 30, 1997
	  */
	public ServerInterfaceImpl() throws java.rmi.RemoteException 
	{
		super();
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.newGraphicIcon()
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
	    	throws java.rmi.RemoteException
	{
		// Create the server object.
		ServerObject sObj = new ServerGraphicIcon(x, y, 
			iconFile, iff);

		// Set a unique objectID.
		int objectID = sObj.hashCode();
		sObj.setObjectID(objectID);

		// Add the new object to the objectList.
		objectList.addData(objectID, sObj);

		// Send the new object to all clients.
		CreateOnAllClients(sObj);
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.newTextIcon()
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
	    	throws java.rmi.RemoteException
	{
		// Create the server object.
		ServerObject sObj = new ServerTextIcon(x, y, 
			label, iff);

		// Set a unique objectID.
		int objectID = sObj.hashCode();
		sObj.setObjectID(objectID);

		// Add the new object to the objectList.
		objectList.addData(objectID, sObj);

		// Send the new object to all clients.
		CreateOnAllClients(sObj);
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.newLine()
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
	    	throws java.rmi.RemoteException
	{
		// Create the server object.
		ServerObject sObj = new ServerLineObject(x0, y0, 
			x1, y1);

		// Set a unique objectID.
		int objectID = sObj.hashCode();
		sObj.setObjectID(objectID);

		// Add the new object to the objectList.
		objectList.addData(objectID, sObj);

		// Send the new object to all clients.
		CreateOnAllClients(sObj);
	}

	//---------------------------------------------------------------
	/** Method:  CreateOnAllClients()
	  * Params:  ServerObject sObj - object to be created on all clients.
	  * Return:  void
	  * Purpose:  to send an object to be created on all clients.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 26, 1997
	  */
	private void CreateOnAllClients(ServerObject sObj)
	{
		// Send the new object to all clients.
		Node n = clientList.getFirstItem();
		ClientInterface client = null;
		while (n != null)
		{
			client = (ClientInterface) n.getData();
			if (sObj.CreateOnClient(client) == false)
			{
				// If the client does not respond,
				// then delete it from the client list.
				clientList.remove(n);
				n = clientList.getCurrent();	
				System.out.println("Client did not respond to " +
					"object create request and was eliminated.");
			}
			else
			{
				n = clientList.next();
			}
		}
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.moveObject()
	* Params:  the command string to be broadcast
	* Return:
	*     int objectID - object's ID
	*	    int xOffset - X offset of the object
	*	    int yOffset - Y offset of the object
	* Purpose:  receive the command from a user and broadcast it.
	* Author:  Steven H. McCown
	* Last Modified:  June 3, 1997
	*/
	public void moveObject(int objectID, int xOffset, int yOffset)
		throws java.rmi.RemoteException
	{
		ServerObject obj = (ServerObject)objectList.findData(objectID);

		if (obj == null)
		{
			System.out.println("Object not found during " +
				"remote move request");
			return;
		}

		obj.translate(xOffset, yOffset);

		// Move the new object on all clients.
		Node n = clientList.getFirstItem();
		ClientInterface client = null;
		while (n != null)
		{
			client = (ClientInterface) n.getData();
			try
			{
				client.moveObject(objectID, xOffset, yOffset);
				n = clientList.next();
			}
			catch (java.rmi.RemoteException e)
			{
				// If the client does not respond,
				// then delete it from the client list.
				clientList.remove(n);
				n = clientList.getCurrent();	
				System.out.println("Client did not respond to " +
					"object move request and was eliminated.");
			}
		}
	}

	//---------------------------------------------------------------
	/** Method:  deleteObject()
	  * Params:
	  *     int objectID - object's ID
	  * Return:  void
	  * Purpose:  receive a deleteObject request from the user
	  *             and broadcast it.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 21, 1997
	  */
	public void deleteObject(int objectID)
		throws java.rmi.RemoteException
	{
		// Remove the object from the server's list.
		objectList.removeData(objectID);

		// Send the delete request to all clients.
		Node n = clientList.getFirstItem();
		ClientInterface client = null;
		while (n != null)
		{
			client = (ClientInterface) n.getData();
			client.deleteObject(objectID);
			n = clientList.next();
		}
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.LoadMap()
	  * Params:  String mapName - map file name
	  * Return:  void
	  * Purpose:  Register client processes with the server. 
	  * Author:  Steven H. McCown
	  * Last Modified:  June 26, 1997
	  */
	public void LoadMap(String mapName)
 		throws java.rmi.RemoteException
	{
		if (mapName == null)
		{
			return;
		}

		// Save the map name.
		this.mapName = mapName;

		Node n = clientList.getFirstItem();
		ClientInterface client = null;
		while (n != null)
		{
			client = (ClientInterface) n.getData();
			boolean loaded = LoadMapOnAClient(client);
			if (loaded == true)	
			{
				n = clientList.next();
			}
			else
			{
				clientList.remove(n);
				n = clientList.getCurrent();	
				System.out.println("Client did not respond to " +
					"load map request and was eliminated.");
			}
		}
	}

	//---------------------------------------------------------------
	/** Method:  LoadMapOnAClient()
	  * Params:  RMIEngine.ClientInterface obj - a handle to the 
	  *		client process that will receive the map request. 
	  * Return:  true if the load operation was successful, otherwise false 
	  * Purpose:  Load a map on a specific client. 
	  * Author:  Steven H. McCown
	  * Last Modified:  June 26, 1997
	  */
	private boolean LoadMapOnAClient(ClientInterface client)
 		throws java.rmi.RemoteException
	{
		boolean rValue = true;

		// Load the map on the new client.
		try
		{
			client.LoadMap(mapName);
		}
		catch (java.rmi.RemoteException e)
		{
			// The client did not respond.
			rValue = false;
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.register()
	  * Params:  RMIEngine.ClientInterface obj - a handle to the 
	  *		client process requesting registration. 
	  * Return:  true if the registration was successful, otherwise false 
	  * Purpose:  Register client processes with the server. 
	  * Author:  Steven H. McCown
	  * Last Modified:  June 26, 1997
	  */
	public int register(ClientInterface newClient)
 		throws java.rmi.RemoteException
	{
		int clientID = newClient.hashCode();

		clientList.addData(clientID, newClient);

		// Load the map on the new client.
		LoadMapOnAClient(newClient);

		// Load the current objects in the session.
		Node n = objectList.getFirstItem();
		ServerObject sObj = null;
		while (n != null)
		{
			sObj = (ServerObject) n.getData();
			sObj.CreateOnClient(newClient);
			n = objectList.next();
		}

		return clientID;
	}

	//---------------------------------------------------------------
	/** Method:  ServerInterface.unRegister()
	  * Params:  int clientID - id of the client requesting to 
	  *	unregister.
	  * Return:  true if the un-registration was successful, 
	  *		otherwise false
	  * Purpose:  un-register client processes with the server.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 18, 1997
	*/
	public boolean unRegister(int clientID)
		throws java.rmi.RemoteException
	{
		// I should do some real error checking to ensure
		// that clients can only request to un register themselves.
		clientList.removeData(clientID);

		return true;
	}

	//---------------------------------------------------------------
	/** Method:  main()
	  * Params:  String args[] - command line arguments (currently unused). 
	  * Return:  void
	  * Purpose:  Startup the ServerInterfaceImpl application. 
	  * Author:  Steven H. McCown
	  * Last Modified:  May 30, 1997
	*/
	public static void main(String args[])
	{
		// Create and install the security manager
		System.setSecurityManager(new RMISecurityManager());

		try 
		{
			String myName = new String((InetAddress.getLocalHost()).getHostName());
			System.out.println("RMIEngine starting on: " + myName);

			LocateRegistry.createRegistry(portID);
	
			ServerInterfaceImpl obj = new ServerInterfaceImpl();
			String bindName = new String("//" + myName + ":" + portID + "/CMAServer");
			System.out.println("bindName = " + bindName);
			Naming.rebind(bindName, obj);
		} 
/*		catch (javax.naming.directory.InvalidAttributesException iae)
		{
			// if object did not supply all mandatory attributes 
			System.out.println("ServerInterfaceImpl.main exception");
			System.out.println(iae.toString());
			iae.printStackTrace();
		}
		catch (javax.naming.NamingException ne)
		{
			// if a naming exception is encountered
			System.out.println("ServerInterfaceImpl.main exception");
			System.out.println(ne.toString());
			ne.printStackTrace();
		}
*/		catch (Exception e) 
		{
			System.out.println("ServerInterfaceImpl.main exception");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
