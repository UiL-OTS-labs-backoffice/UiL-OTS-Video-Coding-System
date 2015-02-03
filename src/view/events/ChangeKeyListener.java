package view.events;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import view.panels.QuickKeys;

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
	QuickKeys quickKeys;
	
	/**
	 * Controller instance
	 */
	controller.Controller c = controller.Controller.getInstance();
	
	/**
	 * Keychanger
	 */
	KeyChanger keyChanger;
	
	/**
	 * Constructor
	 * @param field		The field where the keyListener listens to
	 * @param action	The action of the field
	 */
	public ChangeKeyListener(JTextField field, String action, QuickKeys quickKeys)
	{
		this.field = field;
		this.action = action;
		this.quickKeys = quickKeys;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		field.setText("");
		keyChanger = new KeyChanger(action, field, quickKeys);
		field.addKeyListener(keyChanger);
		field.setBackground(Color.WHITE);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		int key = c.getKey(action);
		field.setText((key > 0) ? KeyEvent.getKeyText(key) : "");
		field.removeKeyListener(keyChanger);
	}

}
