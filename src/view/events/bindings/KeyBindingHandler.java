package view.events.bindings;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import controller.Globals;
import view.bottombar.BottomBar;

public class KeyBindingHandler {
	
	BottomBar bar;
	
	public KeyBindingHandler(BottomBar bar){
		this.bar = bar;
		defineActions();
		defineKeys();
	}
	
	private void defineKeys(){
		InputMap input = bar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		input.put(KeyStroke.getKeyStroke(32, 0), "play");
		input.put(KeyStroke.getKeyStroke(39, 0), "nextFrame");
		input.put(KeyStroke.getKeyStroke(37, 0), "prevFrame");
		
		input.put(KeyStroke.getKeyStroke(93,0), "nextTrail");
		input.put(KeyStroke.getKeyStroke(91, 0), "prevTrial");
	}
	
	private void defineActions(){
		ActionMap map = bar.getActionMap();
		map.put("play", new PlayPause());
		map.put("nextFrame", new NextFrame());
		map.put("prevFrame", new PrevFrame());
		map.put("nextLook", new NextLook());
		map.put("prevLook", new PrevLook());
		map.put("nextTrail", new NextTrial());
		map.put("prevTrial", new PrevTrial());
		map.put("newLook", new NewLook());
		map.put("endLook", new EndLook());
		map.put("newTrial", new NewTrial());
		map.put("endTrial", new EndTrial());
	}
	
	private class PlayPause extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().play();			
		}
	}
	
	private class NextFrame extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().nextFrame();
		}
	}
	
	private class PrevFrame extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().prevFrame();
		}
	}
	
	private class NextLook extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().nextLook();
		}
	}
	
	private class PrevLook extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().prevLook();
		}
	}
	
	private class NextTrial extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().nextTrial();
		}
	}
	
	private class PrevTrial extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getVideoController().prevTrial();
		}
	}
	
	private class NewLook extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getController().newLook();
		}
	}
	private class EndLook extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getController().setEndLook();
		}
	}
	private class NewTrial extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getController().newTrial();
		}
	}
	private class EndTrial extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Globals.getInstance().getController().setEndTrial();
		}
	}
}
