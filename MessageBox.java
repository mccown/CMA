import java.awt.*;
import java.awt.event.*;
import java.awt.TextArea.*;
import java.lang.*;

//***********************************************************************
/** Class:  MessageBox
  * Purpose:  Creates a dialog box with a text string and an ok button.
  * Methods:
  *	MessageBox()
  *	actionPerformed() 
  * Author:  Steven H. McCown
  * Last Modified:  May 20, 1997
  */
public class MessageBox extends Dialog implements ActionListener
{
	protected Button button;
	protected TextArea text;

	//---------------------------------------------------------------
	/** Method:  MessageBox()
	  * Params:  none
	  * Return: created object
	  * Purpose:  MessageBox initialization.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 8, 1997
	  */
	public MessageBox(Frame parent, String title, String message) 
	{
		super(parent, title, false);

		this.setLayout(new BorderLayout(15, 15));   

		text = new TextArea(message, 10, 50);
		this.add("Center", text);

		// Create and add and OK button.
		button = new Button("OK");
		button.addActionListener(this);
		Panel p = new Panel();
		p.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		p.add(button);
		this.add("South", p);

		// Resize the window to the preferred size of its components.
		this.pack();
	}

	//---------------------------------------------------------------
	/** Method:  actionPerformed()
	  * Params:  
	  *	ActionEvent evt - action event
	  * Return:  void.
	  * Purpose:  Handle dialog events.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 20, 1997
	  */
	public void actionPerformed(ActionEvent evt)
	{
		String str = evt.getActionCommand();

		// ------------------------------------
		// Handle the menu events.
		if(str.equals("OK"))
		{
			this.setVisible(false);
			this.dispose();
		}
	}
}

