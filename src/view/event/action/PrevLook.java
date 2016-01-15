package view.event.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.Globals;

/**
 * Action that will move the cursor to the start
 * of the current or the last look
 */
public class PrevLook extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		Globals.getInstance().getVideoController().prevLook();
	}

}
