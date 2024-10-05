import java.awt.*;
import java.awt.image.*;
import java.awt.Cursor.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.util.*;
import java.lang.*;
import java.lang.Math.*;
import java.io.*;
import java.io.RandomAccessFile.*;
import java.awt.MediaTracker.*;
import GenericLinkedList.*;
import RMIEngine.*;
import MapItems.*;
import MapItems.ScreenObjectTypes.*;

//***********************************************************************
/** Class:  MapPanel
  * Purpose:  Creates a panel that will display a map and its' 
  *	associated icons.
  * Methods:
  *	MapPanel() 
  *	setBackground()
  *	newIcon() 
  *	newIcon() 
  *	drawLine() 
  *	moveObject()
  *	deleteSelectedObject()
  *	deleteObject()
  *	clearAllObjects()
  *	saveAllObjects()
  *	readAllObjects()
  *	paintNode() 
  *	update() 
  *	paint() 
  *	class MAdapt
  *	class MMAdapt
  * Author:  Steven H. McCown
  * Last Modified:  May 20, 1997
  */
class MapPanel extends Panel 
{
	CMA newMap;
	GenericLinkedList.LinkedList iconList = new GenericLinkedList.LinkedList();

	Image mapImage = null;
	MediaTracker tracker;

	Node selectedNode = null;
	int firstX = 0;
	int firstY = 0;
	int currentX = 0;
	int currentY = 0;
	int count = 0;

	// Line drawing variables.
	boolean startLineDrawing = false;	
	boolean lineDrawingMode = false;

	// Double-buffering objects.
	Dimension offscreensize;
	Image offScreenImage;
	Graphics offScreenGraphics;

	Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	
	//---------------------------------------------------------------
	/** Method:  MapPanel()
	  * Params:  None  
	  * Return: instantiated object.
	  * Purpose:  Create MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	MapPanel(CMA newMap) 
	{
		this.newMap = newMap;

		// Setup the Mediatracker
		// MediaTracker will allow me to check
		// to see when images are done loading. 
		tracker = new MediaTracker(this);

		// Setup the listeners to trap
		// mouse events.
		addMouseListener(new Madapt(this));
		addMouseMotionListener(new MMAdapt(this));
	}

	//---------------------------------------------------------------
	/** Method:  setBackground()
	  * Params:  None  
	  * Return:  void
	  * Purpose:  Load a new image as the background of
	  *	the MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 23, 1997
	  */
	public void setBackground(Image newImage)
	{
		mapImage = newImage;

		// Show the hour glass cursor.
		newMap.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		// Add the image to media tracker and wait for it to load.
		int mapID = mapImage.hashCode();
		tracker.addImage(mapImage, mapID);
		try
		{
			tracker.waitForID(mapID);
		}
		catch (InterruptedException e)
		{
			// Just return if the image has a 
			// problem loading.
			return;
		}

		// Get the size of the loaded image.
		int imageWidth = mapImage.getWidth(this);
		int imageHeight = mapImage.getHeight(this);

		// Call to newMap (the parent window) to 
		// reset its' size to encompass the new background. 
		// NOTE:  setBounds() seems to set the location of the
		// client area of the window at the specified
		// X/Y coords and then draws the border and title 
		// around it.  Therefore, an arbitrary X/Y of 20, 30
		// is used to allow the title to draw on the screen.
		// If this is not done and the title draws off the 
		// screen, you cannot grab or move the window.
		newMap.setBounds(20, 30, imageWidth+10, imageHeight+50);

		// Reset the default mouse pointer.
		newMap.setCursor(Cursor.getDefaultCursor());
	}

	//---------------------------------------------------------------
	/** Method:  newTextIcon()
	  * Params:  
	  *	int objectID - object id number (unique number
	  *		assigned by the RMI server)
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String label - text label displayed on the icon
	  *	int iff - Identification Friend or Foe code
	  * Return: void.
	  * Purpose:  Create a new icon and add it to the MapPanel.
	  *	This function is called by CMA.newTextIcon() in
	  *	response to an RMI Server call.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void newTextIcon(int objectID, int x, int y, 
		String label, int iff)
	{
		ScreenObject iconData;

		iconData = new IconData(objectID, x, y, label, iff);
		iconList.addData(objectID, iconData);

		repaint();
	}

	//---------------------------------------------------------------
	/** Method:  newGraphicIcon()
	  * Params:  
	  *	Toolkit toolKit - instance of the Toolkit class
	  *	int objectID - object id number (assigned by the server)
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String iconFile - path and filename of the icon image
	  *	int iff - Identification Friend or Foe
	  *		0 - friendly (draws in blue)
	  *		1 - unfriendly (draws in red)
	  *		2 - neutral (draws in yellow)
	  *		3 - unknown (draws in green)
	  * Return: void
	  * Purpose:  Create a new icon and add it to the MapPanel.
	  *	This function is called by CMA.newGraphicsIcon() in
	  *	response to an RMI Server call.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void newGraphicIcon(Toolkit toolKit, int objectID, 
		int x, int y, String iconFile, int iff)
	{
		ScreenObject iconData = new IconData(tracker, toolKit, this,
			objectID, x, y, iconFile, iff);
		iconList.addData(objectID, iconData);
		
		repaint();
	}

	//---------------------------------------------------------------
	/** Method:  drawLine()
	  * Params:  
	  *	int objectID - object id number (assigned by the server)
	  *	int x0 - x0 coordinate
	  *	int y0 - y0 coordinate
	  *	int x1 - x1 coordinate
	  *	int y1 - y1 coordinate
	  * Return: void
	  * Purpose:  Create a new line object and add it to the MapPanel.
	  *	This function is called by CMA.drawLine() in
	  *	response to an RMI Server call.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	void drawLine(int objectID, int x0, int y0, int x1, int y1)
	{
		ScreenObject line = new LineData(objectID, 
			x0, y0, x1, y1);

		iconList.addData(objectID, line);
		
		repaint();
	}

	//---------------------------------------------------------------
	/** Method:  moveObject()
	  * Params:  None  
	  *	int objectID - ID of the object
	  *	int xOffset - xOffset of the object
	  *	int yOffset - yOffset of the object
	  * Return: void
	  * Purpose:  Process request to move an object. 
	  *	This function is called by CMA.moveObject() in
	  *	response to an RMI Server call.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 4, 1997
	  */
	public void moveObject(int objectID, int xOffset, int yOffset)
	{
		// Find the object.
		Object obj = iconList.findData(objectID);

		// If found, translate the object
		if (obj != null)
		{
			((ScreenObject)obj).translate(xOffset, yOffset);
			repaint();
		}
		else
		{
			System.out.println("CMA:  RMIServer requested " +
				"to move an object that was not found.");
		}
	}

	//---------------------------------------------------------------
	/** Method:  deleteSelectedObject()
	  * Params:  None  
	  * Return: void
	  * Purpose:  Check to see if an object is selected and the
	  *	issue a delete request for that object. 
	  * Author:  Steven H. McCown
	  * Last Modified:  June 22, 1997
	  */
	public void deleteSelectedObject()
	{
		if (selectedNode != null)
		{
			int objectID = ((ScreenObject)selectedNode.getData()).getObjectID();
			newMap.initiateDeleteObject(objectID);
		}
	}

	//---------------------------------------------------------------
	/** Method:  deleteObject()
	  * Params:    
	  *	int objectID - ID of the object
	  * Return: void
	  * Purpose:  Process request to delete an object. 
	  *	This function is called by CMA.deleteObject() in
	  *	response to an RMI Server call.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 21, 1997
	  */
	public void deleteObject(int objectID)
	{
		// Find the object.
		boolean deleted = iconList.removeData(objectID);

		// If found, delete the object
		if (deleted != true)
		{
			System.out.println("CMA:  request to delete " +
				"an object that was not found.");
		}
		else
		{
			repaint(); 
		}
	}

	//---------------------------------------------------------------
	/** Method:  startLineDrawingMode()
	  * Params:  None  
	  * Return: void
	  * Purpose:  begin line drawing mode (i.e., rubber-banding).
	  * Author:  Steven H. McCown
	  * Last Modified:  April 30, 1997
	  */
	void startLineDrawingMode() 
	{
		startLineDrawing = true;
	}

	//---------------------------------------------------------------
	/** Method:  clearAllObjects()
	  * Params:  None  
	  * Return: the number of icons in the MapPanel (i.e., will be 0).
	  * Purpose:  clear all of the icons from the MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	void clearAllObjects()
	{
		iconList.removeAllNodes();
		repaint();
	}

	//---------------------------------------------------------------
	/** Method:  saveAllObjects()
	  * Params:  None  
	  * Return: void.
	  * Purpose:  save all of the icons from the MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	void saveAllObjects()
	{
		try
		{
			// are loaded, they will be in the correct order.
			File f = new File("test.dat");
			if (f != null)
			{
				f.delete();
			}

			RandomAccessFile file = 
				new RandomAccessFile("test.dat", "rw");
			file.seek(0);

			// Save from the back of the list so that when the items 
			Node n = iconList.getLastItem();
			while(n != null) 
			{
				ScreenObject s = (ScreenObject)n.getData();
				boolean readOK = s.write(file);
			 	n = iconList.previous();
			}
			file.close();
		}
		catch (IOException ioe)
		{
			System.out.println("There is a problem " +
				"creating the storage file");
		}
	}

	//---------------------------------------------------------------
	/** Method:  readAllObjects()
	  * Params:  None  
	  * Return: void.
	  * Purpose:  read all of the icons from a file and load them
	  *	into the MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 8, 1997
	  */
	void readAllObjects()
	{
		try
		{
			// are loaded, they will be in the correct order.
			RandomAccessFile file = 
				new RandomAccessFile("test.dat", "r");

			boolean EOFfound = false;
			int objectType = -1;
			ScreenObject newObject = null;
			do
			{
				objectType = -1;
				// Read the object type.
				try
				{
					objectType = file.readInt();
				}
				catch (EOFException eofe)
				{
					EOFfound = true;
					file.close();
				}

				// Load the proper type.
				newObject = null;
				switch (objectType)
				{
				case ScreenObjectTypes.LINE_OBJECT_TYPE:
					//newObject = new LineData(file);
					break;

				case ScreenObjectTypes.TEXT_ICON_OBJECT_TYPE:
					//newObject = new IconData(file, objectType);
					break;

				case ScreenObjectTypes.GRAPHIC_ICON_OBJECT_TYPE:
					// newObject = new IconData(file, 
					//	objectType, getToolkit(), this);
					break;
				default:
					// This also serves to catch the EOFException.
				}
   
				// Save the new data in a Node and add it to 
				// the list of icons.
				if (newObject != null)
				{
			 		Node n = new Node(newObject);
					if (n != null)
					{
						iconList.add(n);
					}
				}
			} while (EOFfound != true);

			file.close();
		}
		catch (IOException ioe)
		{
			System.out.println("There is a problem " +
				"opening the storage file");
		}

		repaint();
	}

	//---------------------------------------------------------------
	/** Method:  update()
	  * Params:  
	  *	Graphics g - graphics context
	  * Return: void.
	  * Purpose:  handles the MapPanel drawing using double-buffering.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void update(Graphics g) 
	{
		paint(g);
	}
 
	//--------------------------------------------------------------- 
	/** Method:  paint()
	  * Params:  
	  *	Graphics g - graphics context
	  * Return: void
	  * Purpose:  handles the MapPanel drawing using double-buffering.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public void paint(Graphics g)
	{
		Dimension d = getSize();

		if ((offScreenImage == null) || 
			(d.width != offscreensize.width) || 
			(d.height != offscreensize.height)) 
		{
			offScreenImage = createImage(d.width, d.height);
			offscreensize = d;
			offScreenGraphics = offScreenImage.getGraphics();
			offScreenGraphics.setFont(getFont());
		}

		// Draw the map image.
		if (mapImage != null)
		{
			offScreenGraphics.drawImage(mapImage, 0, 0, this);
		}
		else
		{
			offScreenGraphics.setColor(getBackground());
			offScreenGraphics.fillRect(0, 0, d.width, d.height);
		}

		FontMetrics fm = offScreenGraphics.getFontMetrics();
		Node firstNode = iconList.getFirstItem();
		Node n = iconList.getLastItem();
		boolean firstObjectInList = false;
		while(n != null) 
		{
			ScreenObject s = (ScreenObject)n.getData();
			firstObjectInList = ((firstNode == n) ? true : false);
			s.paint(offScreenGraphics, fm, this, firstObjectInList);
			firstObjectInList = false;
		 	n = iconList.previous();
		}

		g.drawImage(offScreenImage, 0, 0, null);
	}

	//---------------------------------------------------------------
	/** Method:  selectObject()
	  * Params:  
	  *	int x - x position of hit
	  *	int y - y position of hit
	  * Return: void
	  * Purpose:  to determine if a screen object has been
	  *	clicked by the mouse.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
 	public boolean selectObject(int x, int y) 
	{
		Node n = iconList.getFirstItem();
		selectedNode = null;
		boolean found = false;
		while((n != null) && (found == false))
		{
			ScreenObject tempData = (ScreenObject) n.getData();

			if (tempData.isHit(x, y))
			{
				selectedNode = n;

				// Move the selectedNode item to the 
				// front of the list which allows it 
				// to be redrawn on top.
				iconList.toFront(selectedNode);

				repaint();

				newMap.setCoordString("X,Y = ("+x+","+y+")");
				newMap.setCursor(moveCursor);
				found = true;
			}
			else
			{
				n = iconList.next();
			}
 		}

		return found;
	}

	//***********************************************************************
	/** Class:  Madapt
	  * Purpose:  Creates a WindowAdapter to capture and handle 
	  *	window events.
	  * Methods:
	  *	windowClosing() 
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	class Madapt extends MouseAdapter
	{
		MapPanel nm;

		public Madapt(MapPanel nm)
		{
			this.nm = nm;
		}

		//---------------------------------------------------------------
		/** Method:  mousePressed()
		  * Params:  
		  * 	MouseEvent evt - the mouse down event
		  * Return: void.
		  * Purpose:  handle the mouse down events.
		  * Author:  Steven H. McCown
		  * Last Modified:  May 20, 1997
		  */
	 	public void mousePressed(MouseEvent evt)
		{
			int x = evt.getX();
			int y = evt.getY();
			currentX = firstX = x; 
			currentY = firstY = y;

			if (startLineDrawing == true)
			{
				startLineDrawing = false;
				getGraphics().setXORMode(new Color(255, 0, 255));
				lineDrawingMode = true;
			}
			else
			{
			 	selectObject(x, y);
			}
		}

		//---------------------------------------------------------------
		/** Method:  mouseReleased()
		  * Params:  
		  * 	MouseEvent evt - the mouse up event
		  * Return: void.
		  * Purpose:  process mouse release events.
		  * Author:  Steven H. McCown
		  * Last Modified:  May 20, 1997
		  */
		public void mouseReleased(MouseEvent evt)
		{
			int x = evt.getX();
			int y = evt.getY();
			currentX = x;
			currentY = y;

			if (lineDrawingMode == true)
			{
				lineDrawingMode = false;
				newMap.createLine(firstX, firstY, 
					currentX, currentY);
			}  
			else if (selectedNode != null)
			{
 				int objectID = 
					((ScreenObject)selectedNode.getData()).getObjectID();
				newMap.initiateMoveObject(objectID, 
					currentX - firstX, currentY - firstY);
					
				//selectedNode = null;

				repaint();

				// Draw the coordinates on the bottom of
				// the window.
				newMap.setCoordString("X,Y = ("+x+","+y+")");
				newMap.setCursor(Cursor.getDefaultCursor());
			}
		}

	}  // End class Madapt

 	//***********************************************************************
	/** Class:  MMAdapt
	  * Purpose:  Creates a MouseMotionAdapter to capture and handle 
	  *	window events.
	  * Methods:
	  *	windowClosing() 
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	class MMAdapt extends MouseMotionAdapter
	{
		MapPanel nm;

		public MMAdapt(MapPanel nm)
		{
			this.nm = nm;
		}

		//---------------------------------------------------------------
		/** Method:  mouseDragged()
		  * Params:  
		  * 	MouseEvent evt - the mouse drag event
		  * Return: void.
		  * Purpose:  handle mouse drag events.
		  * Author:  Steven H. McCown
		  * Last Modified:  Map 20, 1997
		  */
		public void mouseDragged(MouseEvent evt) 
		{
			int x = evt.getX();
			int y = evt.getY();

			int previousX = currentX;
			int previousY = currentY;
			currentX = x;
			currentY = y;

			// Draw the coordinates on the bottom
			// of the window.
			newMap.setCoordString("X,Y = ("+x+","+y+")");

			if (lineDrawingMode == true)
			{
				Graphics g = getGraphics();
				g.setXORMode(new Color(255, 255, 255));
				g.drawLine(firstX, firstY, previousX, previousY);
				g.drawLine(firstX, firstY, currentX, currentY);
			}
			else if (selectedNode != null)
			{
				Graphics g = getGraphics();
				ScreenObject data = (ScreenObject) selectedNode.getData();
				data.drawXORFrame(g, firstX, firstY, previousX, previousY);
				data.drawXORFrame(g, firstX, firstY, currentX, currentY);
			}
		}

		//---------------------------------------------------------------
		/** Method:  mouseMoved()
		  * Params:  
		  * 	MouseEvent evt - the mouse move event
		  * Return: void.
		  * Purpose:  handle mouse move events.
		  * Author:  Steven H. McCown
		  * Last Modified:  Map 20, 1997
		  */
		public void mouseMoved(MouseEvent evt) 
		{
			int x = evt.getX();
			int y = evt.getY();

			// Draw the coordinates on the bottom 
			// of the window.
			newMap.setCoordString("X,Y = ("+x+","+y+")");
		}
	}  // End class MMAdapt
}
