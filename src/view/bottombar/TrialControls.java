package view.bottombar;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.Controller;
import controller.Globals;

public class TrialControls extends JPanel {
	
	private static final long serialVersionUID = -2015592156911727320L;
	private static ArrayList<ImageIcon> buttonIcons;
	private Controller c;
	
	// Buttons
	private JButton newTrial, endTrial, newLook, endLook;
	
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_TEXT_MARGIN = 20;

	public TrialControls(Globals g)
	{
		readButtonIcons();
		
		this.c = g.getController();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BoxLayout(TrialControls.this, BoxLayout.LINE_AXIS));
				addNewTrialButton();
				addEndTrialButton();
				addNewLookButton();
				addEndLookButton();
			}
		});
	}
	
	/**
	 * Method to update the text and state of the experiment buttons
	 * @param newTrial		new trial button text
	 * @param endTrial		end trial button text
	 * @param newLook		new look button text
	 * @param endLook		end look button text
	 * @param nt			new trial enabled state
	 * @param et			end trial enabled state
	 * @param nl			new look enabled sate 
	 * @param el			end look enabled state
	 * @param lookComment 
	 * @param trialComment 
	 */
	public void update(
			final int trial, final int look,
			final boolean nt, final boolean et, final boolean nl, final boolean el
		)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TrialControls.this.newTrial.setEnabled(nt);
				
				TrialControls.this.endTrial.setIcon(trial >= 0 ? buttonIcons.get(5) : buttonIcons.get(4));
				TrialControls.this.endTrial.setToolTipText(formatLookTrial("trial", trial));
				TrialControls.this.endTrial.setText(et ? Integer.toString(Math.abs(trial)) : "");
				TrialControls.this.endTrial.setEnabled(et);
				
				TrialControls.this.newLook.setEnabled(nl);
				
				TrialControls.this.endLook.setIcon(buttonIcons.get(look >= 0 ? 2 : 1));
				TrialControls.this.endLook.setToolTipText(formatLookTrial("look", look));
				TrialControls.this.endLook.setText(el ? Integer.toString(Math.abs(look)) : "");
				TrialControls.this.endLook.setEnabled(el);
			}
		});
	}
	
	private static String formatLookTrial(String text, int number)
	{
		String endOrExtend = number >= 0 ? "End" : "Extend";
		return String.format("%s %s %d", endOrExtend, text, Math.abs(number));
	}

	private void addNewTrialButton()
	{
		newTrial = new JButton(buttonIcons.get(3));
		newTrial.setToolTipText("Star a new Trial");
		
		newTrial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread ntT = new Thread(){
					public void run(){
						c.newTrial();
					}
				};
				ntT.start();
			}
		});
		
		add(newTrial);
		newTrial.setPreferredSize(getButtonDimension(newTrial.getIcon()));
		newTrial.setMaximumSize(getButtonDimension(newTrial.getIcon()));
		newTrial.setFocusable(false);
		newTrial.setBorder(BorderFactory.createEmptyBorder());
		newTrial.setContentAreaFilled(false);
	}
	
	private void addEndTrialButton()
	{
		endTrial = new JButton(buttonIcons.get(5));
		endTrial.setToolTipText("End trial");
		endTrial.setHorizontalAlignment(SwingConstants.CENTER);
		endTrial.setHorizontalTextPosition(SwingConstants.LEADING);
		
		endTrial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread etT = new Thread(){
					public void run(){
						c.setEndTrial();
					}
				};
				etT.start();
			}
			
		});
		
		add(endTrial);
		endTrial.setPreferredSize(getButtonDimension(endTrial.getIcon()));
		endTrial.setMaximumSize(getButtonDimension(endTrial.getIcon()));
		endTrial.setFocusable(false);
		endTrial.setBorder(BorderFactory.createEmptyBorder());
		endTrial.setContentAreaFilled(false);
	}
	
	private void addNewLookButton()
	{
		newLook = new JButton(buttonIcons.get(0));
		newLook.setToolTipText("New look");
		
		newLook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread nlT = new Thread(){
					public void run(){
						c.newLook();
					}
				};
				nlT.start();
			}
		});
		
		add(newLook);
		newLook.setPreferredSize(getButtonDimension(newLook.getIcon()));
		newLook.setMaximumSize(getButtonDimension(newLook.getIcon()));
		newLook.setFocusable(false);
		newLook.setBorder(BorderFactory.createEmptyBorder());
		newLook.setContentAreaFilled(false);
	}
	
	private void addEndLookButton()
	{
		endLook = new JButton(buttonIcons.get(2));
		endLook.setToolTipText("End look");
		endLook.setHorizontalAlignment(SwingConstants.CENTER);
		endLook.setHorizontalTextPosition(SwingConstants.LEADING);
		endLook.setContentAreaFilled(false);
		endLook.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread elT = new Thread(){
					public void run(){
						c.setEndLook();
					}
				};
				elT.start();
			}
		});
		
		add(endLook);
		endLook.setPreferredSize(getButtonDimension(endLook.getIcon()));
		endLook.setMaximumSize(getButtonDimension(endLook.getIcon()));
		endLook.setFocusable(false);
		endLook.setBorder(BorderFactory.createEmptyBorder());
		endLook.setContentAreaFilled(false);
	}
	
	private static Dimension getButtonDimension(Icon ico)
	{
		return new Dimension(ico.getIconWidth() + BUTTON_TEXT_MARGIN, BUTTON_HEIGHT);
	}
	
	private static void readButtonIcons()
	{
		buttonIcons = new ArrayList<ImageIcon>();
		for(int i = 1; i <= 6; i++)
		{
			String filename = String.format("../../img/TrialButtonIcons/TrialButtons-%02d.png", i);
			try{
				BufferedImage buttonIcon = ImageIO.read(TrialControls.class.getResource(filename));
				ImageIcon icon = new ImageIcon(buttonIcon);
				Image img = icon.getImage();
				int width = Math.round(25f / (float) buttonIcon.getHeight() * (float) buttonIcon.getWidth()); 
				img = img.getScaledInstance(width, 25, Image.SCALE_SMOOTH);
				buttonIcons.add(new ImageIcon(img));
			} catch (IOException e) {
				System.out.format("Couldn't read file '%s'\n", filename);
			}
		}
		
		
	}
	
	
}
