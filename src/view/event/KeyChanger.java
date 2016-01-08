package view.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import view.panels.QuickKeysPanel;
import controller.*;

public class KeyChanger implements KeyListener {

	Controller c;
	
	String action;
	JTextField field;
	QuickKeysPanel quickKeysPanel;
	
	public KeyChanger(String action, JTextField field, QuickKeysPanel quickKeys){
		this.action = action;
		this.field = field;
		this.quickKeysPanel = quickKeys;
		c = Globals.getInstance().getController();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		final int code = e.getKeyCode();
		
		
		Thread setActionCodeThread = new Thread(){
			public void run(){
				c.setKey(action, code);
			}
		};
		setActionCodeThread.start();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				field.setText(KeyEvent.getKeyText(code));
				field.setFocusable(false);
				field.setBackground(null);
				quickKeysPanel.update();
			}
		});
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

}
