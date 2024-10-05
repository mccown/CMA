package MapItems;

import java.awt.*;
import java.awt.image.*;
import java.lang.*;

//***********************************************************************
/** Class:  ColorImageFilter
  * Purpose:  Replaces a specified color in an image with 
  *	another specified color.  
  * Methods:
  *	RGBImageFilter() 
  *	filterRGB() 
  * Author:  Steven H. McCown
  * Last Modified:  May 15, 1997
  */
class ColorImageFilter extends RGBImageFilter
{
	int searchColor;
	int newColor;

	//---------------------------------------------------------------
	/** Method:  ColorImageFilter()
	  * Params:  
	  *	int searchColor - color to be replaced
	  *	int newColor - new color being added
	  * Return: the number of items in the list.
	  * Purpose:  Create a new icon and add it to the MapPanel.
	  * Author:  Steven H. McCown
	  * Last Modified:  April 24, 1997
	  */
	public ColorImageFilter(int searchColor, int newColor)
	{
		this.searchColor = searchColor;
		this.newColor = newColor;

		/**
		  * Setting canFilterIndexColorModel = true
		  * allows filterRGB() to run the filter on 
		  * the color map.  Setting 
		  * canFilterIndexColorModel = false allows 
		  * filterRGB() to be run on each pixel of the 
		  * image itself as noted by x and y.
		  */
		canFilterIndexColorModel = true;
	}

	//---------------------------------------------------------------
	/** Method:  RGBImageFilter()
	  * Params:  
	  *	int x - x location of the rgb pixel.
	  *	int y - y location of the rgb pixel.
	  *	int rgb - the current rgb value of the pixel.
	  * Return: the filtered RGB value;
	  * Purpose:  Replaces a pixel of rgb value searchColor
	  *	with newColor.  Setting 
	  *	canFilterIndexColorModel = true
	  * 	allows filterRGB() to run the filter on 
	  * 	the color map.  Setting 
	  * 	canFilterIndexColorModel = false allows 
	  * 	filterRGB() to be run on each pixel of the 
	  * 	image itself as noted by x and y.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 15, 1997
	  */
	public int filterRGB(int x, int y, int rgb)
	{
		int newRGB = rgb;

		if ((newRGB & 0xFFFFFF) == (searchColor & 0xFFFFFF))
		{
			// Get the old alpha setting.
			int a = rgb & 0xff000000;

			// Set the new color with the old alpha setting.
			newRGB = a | (newColor & 0xFFFFFF);
		}

		return newRGB;
	}
}
