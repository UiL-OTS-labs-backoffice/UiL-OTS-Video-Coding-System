package view.event.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.Globals;

/**
 * Action that will set the end time of the current
 * (or last, if available) trial item to the current time
 */
public class EndTrial extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			Globals.getInstance().getController().setEndTrial();
		} catch(Exception ex){
			
		}
	}

}
