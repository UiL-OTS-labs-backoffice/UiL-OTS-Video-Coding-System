package view.event.binding;

import java.util.Collection;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import controller.Globals;
import model.QuickKeys;
import view.event.IQuickKeyListener;
import view.event.action.*;

public class KeyBindingHandler {
	
	JComponent component;
	QuickKeys keyModel;
	InputMap input;
	
	/**
	 * Constructor method creates the inputMap and actionMap for the
	 * component
	 * @param component		The component for the keyBindings
	 */
	public KeyBindingHandler(JComponent component){
		this.component = component;
		keyModel = Globals.getInstance().getKeyCodeModel();
		input = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		defineActions(component);
		defineKeys();
		
		keyModel.addQuickKeyListener(new IQuickKeyListener(){

			@Override
			public void actionUpdated(String action, int oldKey) {
				input.remove(KeyStroke.getKeyStroke(oldKey, 0));
				input.put(KeyStroke.getKeyStroke(keyModel.getKey(action), 0), action);
			}
			
		});
	}
	
	/**
	 * Private method that registers the initial action keys
	 */
	private void defineKeys(){
		Collection<String> actions = Globals.getInstance().getKeyCodeModel().getActions();
		
		for(String s : actions){
			int key = keyModel.getKey(s);
			if(key > 0){
//				String actionName = keyModel.getName(s);
//				String keyName = KeyStroke.getKeyStroke(key, 0).toString();
//				System.out.println(keyName);
//				System.out.format("Action: '%s'\t name: '%s'\t Key: %d\n\n\n", s, actionName, key);
				KeyStroke stroke = KeyStroke.getKeyStroke(key, 0);
				input.put(stroke, s);
			}
		}
	}
	
	/**
	 * Private method that assigns actions to action names
	 */
	private static void defineActions(JComponent component){
		ActionMap map = component.getActionMap();
		map.put("play", new PlayPause());
		map.put("nextFrame", new NextFrame());
		map.put("prevFrame", new PrevFrame());
		map.put("nextLook", new NextLook());
		map.put("prevLook", new PrevLook());
		map.put("nextTrial", new NextTrial());
		map.put("prevTrial", new PrevTrial());
		map.put("newLook", new NewLook());
		map.put("endLook", new EndLook());
		map.put("newTrial", new NewTrial());
		map.put("endTrial", new EndTrial());
	}
}
