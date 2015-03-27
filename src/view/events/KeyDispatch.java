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
