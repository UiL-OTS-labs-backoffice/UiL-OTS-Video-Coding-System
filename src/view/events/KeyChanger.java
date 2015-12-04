package view.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
				quickKeys.update();
			}
		});
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.print(e.getKeyChar());
		System.out.print(" ");
		System.out.print(e.getKeyCode());
		System.out.print(" ");
		System.out.println(e.getKeyLocation());
		System.out.println(e);
		System.out.println();
	}

}
