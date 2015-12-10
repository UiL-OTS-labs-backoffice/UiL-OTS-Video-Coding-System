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

import model.Look;
import model.Trial;
import view.player.IMediaPlayerListener;
import controller.Controller;
import controller.Globals;
import controller.IVideoControllerObserver;

public class TrialControls extends JPanel {
	
	private static final long serialVersionUID = -2015592156911727320L;
	private static ArrayList<ImageIcon> buttonIcons;
	private Controller c;
	private IVideoControllerObserver vidObserver;
	
	// Buttons
	private JButton newTrial, endTrial, newLook, endLook;
	
	private static final int BUTTON_HEIGHT = 30;
	private static final int BUTTON_TEXT_MARGIN = 20;

	public TrialControls(final Globals g)
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
				
				vidObserver = new IVideoControllerObserver(){
					@Override
					public void videoInstantiated() {
						registerTimeListener();
						g.getVideoController().deregister(vidObserver);
						updateButtons();
					}
				};
				
				g.getVideoController().register(vidObserver);
			}
		});
	}
	
	
	/**
	 * Private method to register an IMediaPlayer listener, to update buttons
	 * on changing of media time
	 */
	private void registerTimeListener(){
		Globals.getInstance().getVideoController().getPlayer().register(new IMediaPlayerListener(){

			@Override
			public void mediaStarted() { }

			@Override
			public void mediaPaused() { }

			@Override
			public void mediaTimeChanged() {
				updateButtons();
			}
		});
	}
	
	/**
	 * Method to update the trial manipulation buttons based on current time
	 */
	private void updateButtons(){
		new Thread(){
			public void run(){
				Globals g = Globals.getInstance();
				long time = g.getVideoController().getMediaTime();
				int tnr = g.getExperimentModel().getItemForTime(time);
				
				final boolean nt = g.getExperimentModel().canAddItem(time) >= 0 & tnr <= 0;
				boolean et = false, nl = false, el = false;
				
				int lnr = 0;
				if(tnr != 0)
				{
					Trial t = (Trial) g.getExperimentModel().getItem(Math.abs(tnr));
					lnr = t.getItemForTime(time);
					et = t.canEnd(time) && lnr <= 0;
					nl = t.canAddItem(time) >= 0 && lnr <= 0;
					
					if(lnr != 0)
					{
						Look l = (Look) t.getItem(Math.abs(lnr));
						el = tnr > 0 && l.canEnd(time);
					}
				}
				
				final boolean canNewLook = nl;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						newTrial.setEnabled(nt);
						newLook.setEnabled(canNewLook);
					}
				});
				
				setEndTrial(tnr, et);
				setEndLook(lnr, el);
			}
		}.start();
	}
	
	/**
	 * Method to update the end trial button based on current trial number and
	 * can end status
	 * @param tnr			Current trial number
	 * @param canEndTrial	Can end status of trial
	 */
	private void setEndTrial(final int tnr, final boolean canEndTrial){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				endTrial.setIcon(tnr >= 0 || !canEndTrial? buttonIcons.get(5) : buttonIcons
						.get(4));
				endTrial.setToolTipText(formatLookTrial("trial", tnr, canEndTrial));
				endTrial.setText(canEndTrial ? Integer.toString(Math.abs(tnr))
						: "");
				endTrial.setEnabled(canEndTrial);
			}
		});
	}
	
	/**
	 * Method to update the end look button based on current look number and
	 * can end status
	 * @param lnr			Current look number
	 * @param canEndLook	Can end status of look
	 */
	private void setEndLook(final int lnr, final boolean canEndLook){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				endLook.setIcon(buttonIcons.get(lnr >= 0 || !canEndLook ? 2 : 1));
				endLook.setToolTipText(formatLookTrial("look", lnr, canEndLook));
				endLook.setText(canEndLook ? Integer.toString(Math.abs(lnr))
						: "");
				endLook.setEnabled(canEndLook);
			}
		});
	}
	
	/**
	 * Formatter function for look and trial button hover text
	 * @param text		The base text of the button
	 * @param number	The number of the trial or look
	 * @return			Formatted text
	 */
	private static String formatLookTrial(String text, int number, boolean canEnd)
	{
		String endOrExtend = number >= 0 || !canEnd ? "End" : "Extend";
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
				updateButtons();
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
				updateButtons();
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
				updateButtons();
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
				updateButtons();
			}
		});
		
		add(endLook);
		endLook.setPreferredSize(getButtonDimension(endLook.getIcon()));
		endLook.setMaximumSize(getButtonDimension(endLook.getIcon()));
		endLook.setFocusable(false);
		endLook.setBorder(BorderFactory.createEmptyBorder());
		endLook.setContentAreaFilled(false);
	}
	
	/**
	 * Calculates button dimensions based on button icon and some settings
	 * @param ico		The button icon
	 * @return			A dimension object for the buttons
	 */
	private static Dimension getButtonDimension(Icon ico)
	{
		return new Dimension(ico.getIconWidth() + BUTTON_TEXT_MARGIN, BUTTON_HEIGHT);
	}
	
	/**
	 * Method to read the icons for the buttons from the files
	 */
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
