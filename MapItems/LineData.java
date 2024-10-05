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
/** Class:  LineData 
  * Purpose:  create and manipulate an LineData object and control
  *     and its' screen interaction.
  * Methods:  None
  *	LineData()
  *	LineData()
  *	paint()
  *	isHit()
  *	drawXORFrame() 
  *	read()
  *	write()
  * Author:  Steven H. McCown
  * Last Modified:  June 24, 1997
  */
public class LineData extends ScreenObject
{
	//---------------------------------------------------------------
	/** Method:  LineData()
	  * Params:
	  *     int objectID - unique ID of the object.
	  *     int x0 - x0 coordinate
	  *     int y0 - y0 coordinate
	  *     int x1 - x1 coordinate
	  *     int y1 - y1 coordinate
	  * Return:  instantiated object.
	  * Purpose:  Create a LineData object.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public LineData(int objectID, int x0, int y0, int x1, int y1)
	{
		super(objectID, x0, y0, x1, y1);

		objectType = ScreenObjectTypes.LINE_OBJECT_TYPE;
	}

	// NOTE:  File access is temporarily disabled.
	//public LineData(RandomAccessFile file)
	//{
	//	objectType = ScreenObjectTypes.LINE_OBJECT_TYPE;
	//	read(file);
	//}

	//---------------------------------------------------------------
	/** Method:  paint()
	  * Params:
	  *     Graphics g - the graphics context used for drawing
	  *     FontMetrics fm - font settings used to display text
	  *     ImageObserver observer - used in loading the image
	  *     boolean isCurrent - tells whether to draw the icon
	  *             in its' current state.
	  * Return:  void
	  * Purpose:  draw the icon
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public void paint(Graphics g, FontMetrics fm, 
		ImageObserver observer, boolean isCurrent) 
	{
		if (isCurrent == true)
		{
			g.setColor(Color.red);
		}
		else
		{
			g.setColor(Color.black);
		}

		g.drawLine(x0, y0, x1, y1);
	}

	//---------------------------------------------------------------
	/** Method:  isHit()
	  * Params:
	  *     int x - the x coordinate of the mouse click
	  *     int y - the y coordinate of the mouse click
	  * Return:  true if the mouse click was 'on the line';
	  *     otherwise false.
	  * Purpose:  determine if a mouse click was within
	  *     10 pixels of the line.
	  * Author:  Steven H. McCown
	  * Last Modified:  June 24, 1997
	  */
	public boolean isHit(int x, int y)
	{
		boolean rValue = false;
		double slope = (double)(y1 - y0)/(x1 - x0);

		// y = mx + b
		int hitY = (int)(y0 + (slope * (x-x0)));
		int hitRegion = 10;
		if (((hitY - hitRegion) <= y) && (y <= (hitY + hitRegion)) &&
			(((x0 <= x) && (x <= x1)) || ((x1 <= x) && (x <= x0))))
		{
			rValue = true;
		}

		return rValue;
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
		// Compute the bounding box.
		// Find the upper left corner of the box.  
		// Since the line may be drawn from any 
		// orientation, we must check for this location.
   		int left = (x0 < x1) ? x0 : x1;
		int top = (y0 < y1) ? y0 : y1;

		// Adjust the upper left location for the 
		// current mouse position and the initial
		// mouse offset in the object.
		left = currentX - (firstX - left);
		top = currentY - (firstY - top);

		// Compute the height and width.
		int width = Math.abs(x1 - x0);
		int height = Math.abs(y1 - y0);

		// Set the XOR drawing color and draw the box.
		g.setXORMode(new Color(255, 255, 255));
		g.drawRect(left, top, width, height);
	}

        // NOTE:  Loading icons from a file has been
        //      temporarily disabled.
	public boolean write(RandomAccessFile file)
	{
		// Save the base class data first.
		return super.write(file);
	}
 
        // NOTE:  Loading icons from a file has been
        //      temporarily disabled.
	public boolean read(RandomAccessFile file)
	{
		return super.read(file);
	}
}
