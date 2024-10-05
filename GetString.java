import java.awt.*;
import java.awt.event.*;
import java.awt.TextArea.*;
import java.lang.*;
import java.awt.AWTEvent.*;

//***********************************************************************
/** Class:  GetString 
  * Purpose:  Creates a dialog box with an editable text string, an
  *	ok button, and a cancel button.
  * Methods:
  *	GetString()
  *	actionPerformed() 
  * Author:  Steven H. McCown
  * Last Modified:  July 10, 1997
  */
public class  GetString extends Dialog implements ActionListener
{
	protected Button okButton;
	protected Button cancelButton;
	protected TextField text;

	private Frame parent;

	//---------------------------------------------------------------
	/** Method:   GetString()
	  * Params:  none
	  * Return: created object
	  * Purpose:   GetString initialization.
	  * Author:  Steven H. McCown
	  * Last Modified:  May 8, 1997
	  */
	public  GetString(Frame parent, String title, String message) 
	{
		super(parent, title, false);

		this.setLayout(new BorderLayout(15, 15));   

		// Create text string.
		text = new TextField(message, 40);
		this.add("Center", text);

		// Create and add and OK button.
		okButton = new Button("OK");
		okButton.addActionListener(this);

		// Create and add and Cancel button.
		cancelButton = new Button("Cancel");
		cancelButton.addActionListener(this);

		Panel p = new Panel();
		p.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		p.add(okButton);
		p.add(cancelButton);
		this.add("South", p);

		// Resize the window to the preferred size of its components.
		this.pack();

		// Save the parent reference
		this.parent = parent;
	}

	//---------------------------------------------------------------
	/** Method:  actionPerformed()
	  * Params:  
	  *	ActionEvent evt - action event
	  * Return:  void.
	  * Purpose:  Handle dialog events.
	  * Author:  Steven H. McCown
	  * Last Modified:  July 10, 1997
	  */
	public void actionPerformed(ActionEvent evt)
	{
		String str = evt.getActionCommand();

		// Handle the menu events.
		if(str.equals("OK"))
		{
			String message = text.getText();

			// Generate the CONNECT_TO_HOST event.
			ActionEvent awtEvent = new ActionEvent(this, DialogEvents.CONNECT_TO_HOST, message);
			parent.dispatchEvent(awtEvent);

			this.setVisible(false);
			this.dispose();
		}
		else
		{
			this.setVisible(false);
			this.dispose();
		}
	}
}

