package view.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import view.panels.QuickKeys;
import controller.*;

public class KeyChanger implements KeyListener {

	Controller c;
	
	String action;
	JTextField field;
	QuickKeys quickKeys;
	
	public KeyChanger(String action, JTextField field, QuickKeys quickKeys){
		this.action = action;
		this.field = field;
		this.quickKeys = quickKeys;
		c = Globals.getInstance().getController();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		field.setText(KeyEvent.getKeyText(code));
		c.setKey(action, code);
		quickKeys.update();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
