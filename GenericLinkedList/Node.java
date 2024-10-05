package GenericLinkedList;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.applet.*;
import java.util.*;
import java.lang.*;

//***********************************************************************
/** Class:  Node
  * Purpose:  The generic node structure for a doubly-linked list.
  * Methods:
  *	Node()
  *	getData()
  *	setData()
  * Author:  Steven H. McCown
  * Last Modified:  June 3, 1997
  */
public class Node
{
	Node prev;
	Node next;
	Object data;

	// nodeID is to be used in searching a linked-list
	// or otherwise identifying nodes.  This would typically
	// be used in a list.find(nodeID) function.
	int nodeID;

	//---------------------------------------------------------------
	/** Method:  Node()
	  * Params:  
	  *	Object o - a Node in the list
	  * Return: instantiated object (this is the constructor).
	  * Purpose:  To create the node.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node (Object o)
	{
		data = o; 
		prev = null;
		next = null;
		nodeID = 0;
	}

	//---------------------------------------------------------------
	/** Method:  getData()
	  * Params:  None  
	  * Return: the data contained in this node.
	  * Purpose:  To get the data from this node.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Object getData()
	{
		return data;
	}

	//---------------------------------------------------------------
	/** Method:  setData()
	  * Params:  None  
	  * Return:  void
	  * Purpose:  To set the data for this node.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void setData(Object o)
	{
		data = o;
	}

	//---------------------------------------------------------------
	/** Method:  getNodeID()
	  * Params:  None  
	  * Return: the nodeID associated with this node.
	  * Purpose:  To get the nodeID associated with this node.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public int getNodeID()
	{
		return nodeID;
	}

	//---------------------------------------------------------------
	/** Method:  setNodeID()
	  * Params:  the nodeID associated with this node.
	  * Return: void
	  * Purpose:  To set the nodeID associated with this node.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void setNodeID(int nodeID)
	{
		this.nodeID = nodeID;
	}
}
