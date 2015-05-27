package view.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.*;

/**
 * Key listeners
 */
public class CodingKeyListener implements KeyListener {
	
	private IVideoControls vc;
	private Controller qk;
	
	public CodingKeyListener(Globals g)
	{
		vc = g.getVideoController();
		qk = g.getController();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(vc.IsLoaded())
    	{
    		int keycode = e.getKeyCode();
    		executeCodeForKey(keycode);
    	}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
