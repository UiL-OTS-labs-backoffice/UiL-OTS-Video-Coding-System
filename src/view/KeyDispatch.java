package view;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import controller.Globals;
import controller.IVideoControls;

/**
 * Key listeners
 */
public class KeyDispatch implements KeyEventDispatcher {
	
	private static IVideoControls c = Globals.getVideoController();
	
	/** 
	 * Key Events
	 * @param e		Key event
	 * @return		Boolean
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() == KeyEvent.KEY_PRESSED) {
	    	if(c.IsLoaded())
	    	{
				switch (e.getKeyCode())
		    	{
			    	case 32: // Space button
			    		c.play();
			    		break;
			    	case 37: // Left button
			    		c.prevFrame();
			    		break;
			    	case 39: // Right button
			    		c.nextFrame();
			    		break;
			    	case 78: // N key
			    		break;
			    	default: // Not registered
			    		 break;
		    	}
	    	}
		} else if (e.getID() == KeyEvent.KEY_RELEASED){
		
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
		
		}
		return false;
	}

}
