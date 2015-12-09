package view.event.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.Globals;

/**
 * Action that will play or pause the video, depending
 * on the active playing state at the time of the action
 */
public class PlayPause extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		Globals.getInstance().getVideoController().play();		
	}

}
