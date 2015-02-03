package view.events;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import controller.Globals;

/**
 * Key listeners
 */
public class KeyDispatch implements KeyEventDispatcher {
	
	private static controller.IVideoControls vc = 
			Globals.getVideoController();
	
	private static controller.Controller qk = 
			Globals.getController();
	
	/** 
	 * Key Events
	 * @param e		Key event
	 * @return		Boolean
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() == KeyEvent.KEY_PRESSED) {
	    	if(Globals.getVideoController().IsLoaded())
	    	{
				int kc = e.getKeyCode();
	    		if(kc == qk.getKey("play"))
	    			vc.play();
	    		else if (kc == qk.getKey("prevFrame"))
	    			vc.prevFrame();
	    		else if (kc == qk.getKey("nextFrame"))
	    			vc.nextFrame();
	    		else if (kc == qk.getKey("nextTrial"))
	    		{ } // TODO: implement next trial
	    		else if (kc == qk.getKey("prevTrial"))
	    		{ } // TODO: implement previous trial
	    		else if (kc == qk.getKey("prevLook"))
	    		{ } // TODO: implement previous look
	    		else if (kc == qk.getKey("nextLook"))
	    		{ } // TODO: implement next look
	    		else if (kc == qk.getKey("newTrial"))
	    		{ } // TODO: implement new trial
	    		else if (kc == qk.getKey("newLook"))
	    		{ } // TODO: implement new look
	    		else if (kc == qk.getKey("endTrial"))
	    		{ } // TODO: implement end trial
	    		else if (kc == qk.getKey("endLook"))
	    		{ } // TODO: implement end look
	    	}
		} else if (e.getID() == KeyEvent.KEY_RELEASED){
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
		}
		return false;
	}

}
