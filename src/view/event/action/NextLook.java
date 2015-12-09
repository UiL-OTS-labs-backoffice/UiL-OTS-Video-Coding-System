package view.event.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.Globals;

/**
 * Action that will move the cursor to the
 * first next look
 */
public class NextLook extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		Globals.getInstance().getVideoController().nextLook();
	}

}
