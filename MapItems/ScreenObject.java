package MapItems;

import java.awt.*;
import java.awt.image.*;
import java.awt.Cursor.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.util.*;
import java.lang.*;
import java.lang.Math.*;
import java.io.*;

//***********************************************************************
/** Class:  ScreenObject
  * Purpose:  Abstract class for defining movable objects
  *	on the screen. 
  * Methods: 
  *	ScreenObject() 
  *	ScreenObject() 
  *	getObjectID() 
  *	setObjectID() 
  *	paint() 
  *	isHit()
  *	translate()
  *	drawXORFrame() 
  *	write()
  * Author:  Steven H. McCown
  * Last Modified:  June 24, 1997
  */
public abstract class ScreenObject
{
	// objectID is an identification that is set by the RMIEngine 
	// (i.e., server).  Having the RMIEngine set the objectID will
	// ensure that the id is unique across all collaborative
	// clients.
	int objectID = 0;
	int x0 = 0;
	int y0 = 0;
	int x1 = 0;
	int y1 = 0;

	//---------------------------------------------------------------
	/** Method:  ScreenObject()
	  * Params:
	  *     int objectID - unique ID of the object.
	  *     int x0 - x0 coordinate
	  *     int y0 - y0 coordinate
	  *     int x1 - x1 coordinate
	  *     int y1 - y1 coordinate
	  * Return:  instantiated object.
	  * Purpose:  Create a ScreenObject.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public ScreenObject(int objectID, int x0, int y0, 
		int x1, int y1)
	{
		this.objectID = objectID;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	//---------------------------------------------------------------
	/** Method:  ScreenObject()
	  * Params:
	  *     int objectID - unique ID of the object.
	  *     int x0 - x0 coordinate
	  *     int y0 - y0 coordinate
	  * Return:  instantiated object.
	  * Purpose:  Create a ScreenObject.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public ScreenObject(int objectID, int x0, int y0)
	{
		this.objectID = objectID;
		this.x0 = x0;
		this.y0 = y0;
	}

	//---------------------------------------------------------------
	/** Method:  getObjectID()
	  * Params:  none
	  * Return:  int - objectID.
	  * Purpose:  return a ScreenObject's objectID.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public int getObjectID()
	{
		return objectID;
	}

	//---------------------------------------------------------------
	/** Method:  setObjectID()
	  * Params:  
	  *	int objectID - net objectID to be associated with
	  *		this object 
	  * Return:  void.
	  * Purpose:  set a ScreenObject's objectID.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public void setObjectID(int objectID)
	{
		this.objectID = objectID;
	}

	// This needs to be initialized in each constructor
	// for derived objects.  This is ONLY used for
	// identifing objects as they are read from a file.
	protected int objectType;

        //---------------------------------------------------------------
	/** Method:  paint()
	  * Params:
	  *	Graphics g - the graphics context used for drawing
	  *	FontMetrics fm - font settings used to display text
	  *	ImageObserver observer - used in loading the image 
	  *	boolean isCurrent - tells whether to draw the object 
	  *		in its' current state. 
	  * Return:  void
	  * Purpose:  draw the icon
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public abstract void paint(Graphics g, FontMetrics fm, 
		ImageObserver observer, boolean isCurrent);

        //---------------------------------------------------------------
	/** Method:  isHit()
	  * Params:
	  *	int x - the x coordinate of the mouse click
	  *	int y - the y coordinate of the mouse click
	  * Return:  true if the mouse click was within the object;
	  *	otherwise false. 
	  * Purpose:  determine if a mouse click was within
	  *	the objects' area. 
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public abstract boolean isHit(int x, int y);

        //---------------------------------------------------------------
	/** Method:  translate()
	  * Params:
	  *	int x - the x offset
	  *	int y - the y offset
	  * Return:  void 
	  * Purpose:  translate an object to a new location. 
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public void translate(int x, int y)
	{
		int endPointXOffset = x1 - x0;
		int endPointYOffset = y1 - y0;

		x0 += x;
		y0 += y;
		x1 = x0 + endPointXOffset;
		y1 = y0 + endPointYOffset;
	}

	//---------------------------------------------------------------
	/** Method:  drawXORFrame()
	  * Params:
	  *     Graphics g - the graphics context used for drawing
	  *     int firstX - the x coordinate of the first 
	  *		mouse click (in the move)
	  *     int firstY - the y coordinate of the first 
	  *		mouse click (in the move)
	  *     int currentX - the x coordinate of the current mouse click
	  *     int currentY - the y coordinate of the current mouse click
	  * Return:  void
	  * Purpose:  draw and XOR frame when moving the line. 
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public void drawXORFrame(Graphics g, int firstX, int firstY, 
		int currentX, int currentY)
	{
		// Setup offsets.
		int xOffset = firstX - x0;
		int yOffset = firstY - y0;

		// Draw new XOR lines.
		int left = currentX - xOffset;
		int top = currentY - yOffset;
		int width = x1 - x0;
		int height = y1 - y0;

		// Set the XOR drawing color.
		g.setXORMode(new Color(255, 255, 255));
		g.drawRect(left, top, width, height);
	}

        // NOTE:  Loading icons from a file has been
        //      temporarily disabled.
	public boolean write(RandomAccessFile file)
	{
		boolean rValue = false;
		try
		{
			// This needs to be the first item written as it 
			// is the item that will identify the following data.
			file.writeInt(objectType);

			file.writeInt(x0);
			file.writeInt(y0);
			file.writeInt(x1);
			file.writeInt(y1);
		
			rValue = true;
		} 
		catch (IOException ioe)
		{
			System.out.println("file write error in ScreenObject");
		}

		return rValue;
	}

        // NOTE:  Loading icons from a file has been
        //      temporarily disabled.
	public boolean read(RandomAccessFile file)
	{
		boolean rValue = false;
		// Since the objectType has already been read in, 
		// DO NOT read it in here.
		try
		{
			x0 = file.readInt();
			y0 = file.readInt();
			x1 = file.readInt();
			y1 = file.readInt();

			rValue = true;
		} 
		catch (IOException ioe)
		{
			System.out.println("file read error in ScreenObject");
		}

		return rValue;
	}
}
