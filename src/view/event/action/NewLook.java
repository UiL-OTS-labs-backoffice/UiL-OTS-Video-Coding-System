package view.event.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.Globals;

/**
 * Action that will create a new look item
 */
public class NewLook extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			Globals.getInstance().getController().newLook();
		} catch(Exception ex){
			
		}
		
	}

}
