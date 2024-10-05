package MapItems;

import java.awt.*;
import java.awt.image.*;
import java.awt.Cursor.*;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.MediaTracker.*;
import java.util.*;
import java.lang.*;
import java.lang.Math.*;
import java.io.*;

//***********************************************************************
/** Class:  IconData 
  * Purpose:  Create and manipulate an IconData object 
  *	(text or graphic) and control its' screen interaction.
  * Methods: 
  *	IconData()
  *	IconData()
  *	IconData()
  *	IconData()
  *	loadGraphic()
  *	paint() 
  *	isHit()
  *	write()
  *	read()
  * Author:  Steven H. McCown
  * Last Modified:  June 24, 1997
  */
public class IconData extends ScreenObject
{
	private String lbl = "";
	private int iff = 0;
	private Image iconImage = null;
	private String iconPath = null;

	final Color fixedColor = Color.red;
	final Color nodeColor = new Color(250, 220, 100);

        //---------------------------------------------------------------
	/** Method:  IconData()
	  * Params:
	  *	int objectID - unique ID of the object. 
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String text - text string displayed as 
	  *		part of the icon
	  *	int iff - identification friend or foe code
	  * Return:  instantiated object.  
	  * Purpose:  Create a text icon.
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public IconData(int objectID, int x, int y, String text, int iff)
	{
		super(objectID, x, y);

		objectType = ScreenObjectTypes.TEXT_ICON_OBJECT_TYPE;
		lbl = new String(text);
		this.iff = iff;
	}

        //---------------------------------------------------------------
	/** Method:  IconData()
	  * Params:
	  *	MediaTracker tracker - used to check when the 
	  *		image is done loading 
	  *	Toolkit toolKit - used in loading the image
	  *	ImageObserver observer - used in loading the image 
	  *	int objectID - unique ID of the object. 
	  *	int x - x coordinate
	  *	int y - y coordinate
	  *	String iconPath - path to the icon image file
	  *	int iff - identification friend or foe code
	  * Return:  instantiated object.  
	  * Purpose:  Create a graphic icon.
	  * Author:  Steven H. McCown                              
	  * last Modified:  June 24, 1997                         
	  */                              
	public IconData(MediaTracker tracker, Toolkit toolKit, 
		ImageObserver observer, int objectID, 
		int x, int y, String iconPath, int iff)
   	{
		super(objectID, x, y);

		objectType = ScreenObjectTypes.GRAPHIC_ICON_OBJECT_TYPE;
		this.iconPath = new String(iconPath);
		Image tempImage = toolKit.getImage(iconPath);
		this.iff = iff;

		loadGraphic(tracker, iconPath, toolKit, observer);
	}		

	// NOTE:  Loading icons from a file has been 
	// 	temporarily disabled.
	// For loading a new text icon.
	//public IconData(RandomAccessFile file, int objectType)
	//{
	//	this.objectType = objectType;
 	//	read(file);
	//}

	// NOTE:  Loading icons from a file has been 
	// 	temporarily disabled.
	// For loading a new graphic icon.
	//public IconData(RandomAccessFile file, int objectType, 
	//	Toolkit toolKit, ImageObserver observer)
	//{
	//	this.objectType = objectType;
 	//	read(file);
	//
	//	loadGraphic(iconPath, toolKit, observer);
	//}
	

        //---------------------------------------------------------------
	/** Method:  loadGraphic()
	  * Params:
	  *	MediaTracker tracker - used to check when the 
	  *		image is done loading 
	  *	Toolkit toolKit - used in loading the image
	  *	ImageObserver observer - used in loading the image 
	  *	int objectID - unique ID of the object. 
	  * Return:  void
	  * Purpose:  load a graphic image and filter it for the
	  *	proper iff code color
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	private void loadGraphic(MediaTracker tracker, String iconPath, 
		Toolkit toolKit, ImageObserver observer)
	{
		Color iffColor = Color.white;
		switch (iff)
		{
		case ScreenObjectTypes.FRIENDLY:		
			iffColor = Color.blue;
			break;
		case ScreenObjectTypes.HOSTILE: 
			iffColor = Color.red;
			break;
		case ScreenObjectTypes.UNKNOWN: 
			iffColor = Color.yellow;
			break;
		case ScreenObjectTypes.NEUTRAL: 
			iffColor = Color.green;
			break;
		}

	    	ImageFilter f = new 
			ColorImageFilter(Color.black.getRGB(), 
 			iffColor.getRGB());
	
		Image tempImage = toolKit.getImage(iconPath);
	    	ImageProducer producer =  
			new FilteredImageSource(tempImage.getSource(), f);
	    	iconImage = toolKit.createImage(producer);
		int iconID = iconImage.hashCode();
		tracker.addImage(iconImage, iconID);
		try
                {
                        tracker.waitForID(iconID);
                }
                catch (InterruptedException e)
                {
			System.out.println("InterruptedException");
                        return;
                }

		x1 = x0 + iconImage.getWidth(observer);
		y1 = y0 + iconImage.getHeight(observer);
	}

        //---------------------------------------------------------------
	/** Method:  paint()
	  * Params:
	  *	Graphics g - the graphics context used for drawing
	  *	FontMetrics fm - font settings used to display text
	  *	ImageObserver observer - used in loading the image 
	  *	boolean isCurrent - tells whether to draw the icon 
	  *		in its' current state. 
	  * Return:  void
	  * Purpose:  draw the icon
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public void paint(Graphics g, FontMetrics fm, 
		ImageObserver observer, boolean isCurrent) 
	{
		if (iconImage == null)
		{
			g.setColor(isCurrent ? fixedColor : nodeColor);

			int textOffset = 10;
			int width = fm.stringWidth(lbl) + textOffset;
			int height = fm.getHeight() + 4;
			x1 = x0 + width;
			y1 = y0 + height;
			g.fillRect(x0, y0, width, height);
			g.setColor(Color.black);
			g.drawRect(x0, y0, width, height);
			g.drawString(lbl, x0 + (textOffset/2), y0 + fm.getAscent());
		}
		else
		{
			g.drawImage(iconImage, x0, y0, observer);
		}
	}

        //---------------------------------------------------------------
	/** Method:  isHit()
	  * Params:
	  *	int x - the x coordinate of the mouse click
	  *	int y - the y coordinate of the mouse click
	  * Return:  true if the mouse click was within the icon;
	  *	otherwise false. 
	  * Purpose:  determine if a mouse click was within
	  *	the icons' area. 
	  * Author:  Steven H. McCown                              
	  * Last Modified:  June 24, 1997                         
	  */                              
	public boolean isHit(int x, int y)
	{
		boolean rValue = false;

		if ((x0 <= x) && (x <= x1) &&
			(y0 <= y) && (y <= y1))
		{
			rValue = true;
		}

		return rValue;
	}

	// NOTE:  Loading icons from a file has been 
	// 	temporarily disabled.
	public boolean write(RandomAccessFile file)
	{
		boolean rValue = false;

		// Save the base class data first.
		rValue = super.write(file);
		if (rValue == true)
		{
			switch (objectType)
			{
			case ScreenObjectTypes.TEXT_ICON_OBJECT_TYPE:
				try
				{
					file.writeUTF(lbl);
					rValue = true;
				} 
				catch (IOException ioe)
				{
					System.out.println("file write error in IconData");
				}
	 			break;

			case ScreenObjectTypes.GRAPHIC_ICON_OBJECT_TYPE:
				try
				{
					file.writeUTF(iconPath);
					file.writeInt(iff);
					rValue = true;
				} 
				catch (IOException ioe)
				{
					System.out.println("file write error in IconData");
				}
				break;
			}
		}

		return rValue;
	}

	// NOTE:  Loading icons from a file has been 
	// 	temporarily disabled.
	public boolean read(RandomAccessFile file)
	{
		boolean rValue = false;

		rValue = super.read(file);
		if(rValue == true)
		{
			switch (objectType)
			{
			case ScreenObjectTypes.TEXT_ICON_OBJECT_TYPE:
				try
				{
					lbl = file.readUTF();
					rValue = true;
				} 
				catch (IOException ioe)
				{
					System.out.println("file read error in IconData");
				}
	 			break;

			case ScreenObjectTypes.GRAPHIC_ICON_OBJECT_TYPE:
				try
				{
					iconPath = file.readUTF();
					iff = file.readInt();
					rValue = true;
				} 
				catch (IOException ioe)
				{
					System.out.println("file read error in IconData");
				}	
				break;
			}
		}

		return rValue;
	}
}
