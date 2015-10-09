package view.navbar;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.SystemColor;

import javax.swing.JPanel;

import view.navbar.ABar;
import model.AbstractTimeFrame;

/**
 *	Each timeframe has its own panel. Cewl. 
 */
public class PanelTimeframe
{
	private static final Color LOOK_COLOR = new Color(0, 102, 255);
	private static final Color TRIAL_COLOR = new Color(77, 148, 255);
	private AbstractTimeFrame tf;
	private JPanel panel, smallPanel;
	private int type;
	
	public PanelTimeframe(AbstractTimeFrame tf, int type, ABar pane)
	{
		this.tf = tf;
		this.type = type;
		
		this.panel = new JPanel();
		
		panel.setBackground((type == view.navbar.InformationPanel.TYPE_LOOK) ? SystemColor.inactiveCaption : SystemColor.activeCaption);
		
		this.smallPanel = new JPanel();
		smallPanel.setBackground((type == view.navbar.InformationPanel.TYPE_LOOK) ? LOOK_COLOR : TRIAL_COLOR);
		
		pane.add(panel);
	}
	
	public void setSize(Rectangle size)
	{
		panel.setBounds(size);
	}
	
	public long getStart()
	{
		return tf.getBegin();
	}
	
	public long getEnd()
	{
		return tf.getEnd();
	}
	
	public int getType()
	{
		return type;
	}
	
}
