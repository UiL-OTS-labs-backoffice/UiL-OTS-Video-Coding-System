package view.event;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.Controller;
import controller.Globals;
import view.panels.QuickKeysPanel;

/**
 * Allows clicking of a hotkey field to assign a new key
 */
public class ChangeKeyListener implements FocusListener {
	
	/**
	 * Field that is clicked
	 */
	JTextField field;
	
	/**
	 * Current action
	 */
	private String action;
	
	/**
	 * QuickKeys instance
	 */
	QuickKeysPanel quickKeys;
	
	/**
	 * Controller instance
	 */
	Controller c;
	
	/**
	 * Keychanger
	 */
	KeyChanger keyChanger;
	
	/**
	 * Constructor
	 * @param field		The field where the keyListener listens to
	 * @param action	The action of the field
	 */
	public ChangeKeyListener(JTextField field, String action, QuickKeysPanel quickKeys)
	{
		c = Globals.getInstance().getController();
		this.field = field;
		this.action = action;
		this.quickKeys = quickKeys;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				field.setText("");
			}
		});
		
		keyChanger = new KeyChanger(action, field, quickKeys);
		field.addKeyListener(keyChanger);
		field.setBackground(Color.WHITE);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		final int key = c.getKey(action);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				field.setText((key > 0) ? KeyEvent.getKeyText(key) : "");
			}
		});
		field.removeKeyListener(keyChanger);
	}

}
