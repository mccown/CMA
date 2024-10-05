package GenericLinkedList;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.applet.*;
import java.util.*;
import java.lang.*;

//***********************************************************************
/** Class:  LinkedList
  * Purpose:  To maintain a doubly-linked list of 
  *	generic objects (i.e., Object).
  * Methods:
  *	add()
  *	removeData()
  *	remove()
  *	removeAllNodes()
  *	toFront()
  *	find()
  *	findData()
  *	getFirstItem()
  *	getLastItem()
  *	insert()
  *	previous()
  *	next()
  *	setCurrent();
  * Author:  Steven H. McCown
  * Last Modified:  April 24, 1997
  */
public class LinkedList
{
	private Node root;
	private Node current;

	//---------------------------------------------------------------
	/** Method:  add()
	  * Params:  
	  *	int nodeID - nodeID to be used to search/find nodes.
	  *	Object obj - the object to be added to the list.
	  * Return: void
	  * Purpose:  To add a node to the front of the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public void addData(int nodeID, Object obj)
	{
		Node n = new Node(obj);
		n.setNodeID(nodeID);
		add(n);
	}

	//---------------------------------------------------------------
	/** Method:  add()
	  * Params:  
	  *	Node n - the node to be added to the list.
	  * Return: void
	  * Purpose:  To add a node to the front of the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void add(Node n)
	{
		// New nodes are added to the front of the list.
		n.next = root;
		if (root != null)
		{
			root.prev = n;
		}
		root = n;
		current = root;
	}

	//---------------------------------------------------------------
	/** Method: removeData()
	  * Params:  
	  *	int objectID - objectID of a node in the list.
	  * Return: true if found (and removed), otherwise false.
	  * Purpose: Takes a Node out of the linked list.
	  * 	In Java, memory is freed when it is de-referenced.
	  *	Therefore, the node cannot be explicitly freed,
	  *	but it is gone as far as the LinkedList is concerned.
	  *	It is the calling procedures responsibility to determine
	  *	what to do with the Node.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 21, 1997
	  */
	public boolean removeData(int objectID)
	{
		boolean rValue = false;
		Node temp = root;

		while ((temp != null) && (temp.getNodeID() != objectID))
		{
			temp = temp.next;
		}

		if (temp != null)
		{
			rValue = remove(temp);
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method: remove()
	  * Params:  
	  *	Node n - a node in the list.
	  * Return: true if found (and removed), otherwise false.
	  * Purpose: Takes a Node out of the linked list.
	  * 	In Java, memory is freed when it is de-referenced.
	  *	Therefore, the node cannot be explicitly freed,
	  *	but it is gone as far as the LinkedList is concerned.
	  *	It is the calling procedures responsibility to determine
	  *	what to do with the Node.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public boolean remove(Node n)
	{
		boolean rValue = false;

		// Make sure the Node is in the list.
		if (find(n))
		{
			// Is the node 'current'?
			if (n == current)
			{
				current = n.next;
			}

			if (n.prev != null)
			{
				n.prev.next = n.next;
			}

			if (n.next != null)
			{
				n.next.prev = n.prev;
			}

			// Is the node 'root'?
			if (n == root)
			{
				root = n.next;
			}

			// Set the nodes prev and next pointers.
			n.prev = null;
			n.next = null;
			
			rValue = true;
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method: removeAllNodes()
	  * Params:  None
	  * Return: void
	  * Purpose: remove and delete all nodes (if the are 
	  *	not otherwise referenced) all nodes from the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void removeAllNodes()
	{
		while (root != null)
		{
			current = root.next;

			// set all members to null.
			root.next = null;
			root.prev = null;
			root.data = null;

			root = current;
		}
	}

	//---------------------------------------------------------------
	/** Method:  toFront()
	  * Params:  
	  *	Node n - a node in the list.
	  * Return: true if found and moved to the front, otherwise false.
	  * Purpose:  To remove a node from the list and add it 
	  * 	to the front (i.e., it becomes the root node).
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public boolean toFront(Node n)
	{
		boolean rValue = false;

		if (n != null)
		{
	 		rValue = remove(n);
			n.next = root;
			if (root != null)
			{
				root.prev = n;
			}
			root = n;
			current = root;
			rValue = true;
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method:  find()
	  * Params:  
	  *	Node n - a node in the list.
	  * Return: true if found, otherwise false.
	  * Purpose:  To determine whether the Node n is in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public boolean find(Node n)
	{
		boolean rValue = false;
		Node temp = root;

		while ((temp != null) && (temp != n))
		{
			temp = temp.next;
		}

		if (temp != null)
		{
			rValue = true;
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method:  findData()
	  * Params:  
	  *		int nodeID - a nodeID.
	  * Return: the data associated with a node (if the node was
	  *		found), otherwise null.
	  * Purpose:  To get a node's data by its' nodeID.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 3, 1997
	  */
	public Object findData(int nodeID)
	{
		Object rValue = null;
		Node temp = root;

		while ((temp != null) && (temp.getNodeID() != nodeID))
		{
			temp = temp.next;
		}

		if (temp != null)
		{
			rValue = temp.getData();
		}

		return rValue;
	}

	//---------------------------------------------------------------
	/** Method:  getFirstItem()
	  * Params:  None
	  * Return: the first node in the list or null.
	  * Purpose:  To return the first node in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node getFirstItem()
	{
		current = root;

		return root;
	}

	//---------------------------------------------------------------
	/** Method:  getLastItem()
	  * Params:  None
	  * Return: the last node in the list or null.
	  * Purpose:  To return the last node in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node getLastItem()
	{
		current = root;

		if (current != null)
		{
			while (current.next != null)
			{
				current = current.next;
			}
		}

		return current;
	}

	//---------------------------------------------------------------
	/** Method:  insert(Node newNode, boolean after)
	  * Params:  
	  *	Node n - a Node in the list
	  *	boolean after - true if the Node is to be 
	  *		inserted after 'current', otherwise it 
	  *		will be inserted before the 'current'.
	  * Return: true if found, otherwise false.
	  * Purpose:  To determine whether the Node n is in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void insert(Node n, boolean after)
	{
		// If current is null set it equal to root.
		current = (current == null) ? root : current;

		// If current is *still* null, then make 
		// root and current equal to n.
		if (current == null)
		{
			current = root = n;
			return;
		}

		// Insert the new node.
		if (after)
		{
			n.next = current.next;
			if (n.next != null)
			{
				n.next.prev = n;
			}
			n.prev = current;
			current.next = n;
		}
		else
		{
			n.prev = current.prev;
			if (n.prev != null)
			{
				n.prev.next = n;
			}
			else
			{
				root = n;
			}
			n.next = current;
			current.prev = n;
		}
	}

	//---------------------------------------------------------------
	/** Method:  previous()
	  * Params:  None
	  * Return: a pointer to the Node if found, otherwise null.
	  * Purpose:  To return 'current.next'.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node previous()
	{
		if (current != null)
		{
			current = current.prev;
		}

		return current;
	}

	//---------------------------------------------------------------
	/** Method:  next()
	  * Params:  None
	  * Return: a pointer to the Node if found, otherwise null.
	  * Purpose:  To return 'current.next'.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node next()
	{
		Node temp = null;

		if (current != null)
		{
			temp = current.next;
			current = temp;
		}

		return temp;
	}

	//---------------------------------------------------------------
	/** Method:  getCurrent()
	  * Params:  
	  * Return: current Node
	  * Purpose:  To get the current pointer in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public Node getCurrent()
	{
		return current;
	}

	//---------------------------------------------------------------
	/** Method:  setCurrent(Node newNode)
	  * Params:  
	  *	Node n - a Node in the list
	  * Return: true if found in the list and set as 
	  *	current, otherwise false.
	  * Purpose:  To set the current pointer in the list.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public boolean setCurrent(Node n)
	{
		boolean rValue = false;

		if (find(n))
		{
			current = n;
			rValue = true;
		}

		return rValue;
	}
}
 
