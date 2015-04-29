package view.events;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import controller.Globals;
import controller.*;

/**
 * Key listeners
 */
public class KeyDispatch implements KeyEventDispatcher {
	
	private IVideoControls vc;
	private Controller qk;
	
	public KeyDispatch(Globals g)
	{
		vc = g.getVideoController();
		qk = g.getController();
	}
	
	/** 
	 * Key Events
	 * @param e		Key event
	 * @return		Boolean
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() == KeyEvent.KEY_PRESSED) {
	    	if(vc.IsLoaded())
	    	{
	    		int keycode = e.getKeyCode();
	    		executeCodeForKey(keycode);
	    	}
		} else if (e.getID() == KeyEvent.KEY_RELEASED){
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
		}
		return false;
	}
	
	/**
	 * Finds the function to execute for the key that
	 * is pressed
	 * @param keycode	The keycode of the key that was pressed
	 */
	private void executeCodeForKey(int keycode)
	{
		if(keycode == qk.getKey("play"))
			vc.play();
		else if (keycode == qk.getKey("prevFrame"))
			vc.prevFrame();
		else if (keycode == qk.getKey("nextFrame"))
			vc.nextFrame();
		else if (keycode == qk.getKey("nextTrial"))
		{ 
			vc.nextTrial();
		}
		else if (keycode == qk.getKey("prevTrial"))
		{
			vc.prevTrial();
		}
		else if (keycode == qk.getKey("prevLook"))
		{ 
			vc.prevLook();
		}
		else if (keycode == qk.getKey("nextLook"))
		{ 
			vc.nextLook();
		}
		else if (keycode == qk.getKey("newTrial"))
		{ 
			qk.newTrial();
		} 
		else if (keycode == qk.getKey("newLook"))
		{ 
			qk.newLook();
		} 
		else if (keycode == qk.getKey("endTrial"))
		{ 
			qk.setEndTrial();
		} 
		else if (keycode == qk.getKey("endLook"))
		{ 
			qk.setEndLook();
		} 
	}

}
