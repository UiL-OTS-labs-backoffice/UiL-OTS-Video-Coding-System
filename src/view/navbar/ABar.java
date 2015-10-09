package view.navbar;

import java.awt.Rectangle;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.Globals;
import view.navbar.PanelTimeframe;
import view.player.IMediaPlayer;
import model.AbstractTimeFrame;

public abstract class ABar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected Globals g;
	protected IMediaPlayer player;
	
	protected Navbar navbar;
	protected TimeIndicator indicator;
	private LinkedList<PanelTimeframe> timeFrames = new LinkedList<PanelTimeframe>();
	
	/**
	 * Constructor method creates a new ABar object.
	 * @param duration	Total video duration
	 * @param navbar	Reference to parent Navbar object
	 */
	protected ABar(Navbar navbar, Globals g)
	{
		this.g = g;
		this.navbar = navbar;
		indicator = new TimeIndicator(this, g);
		add(indicator);
		setIndicatorSettings();
	}
	
	public void videoInstantiated()
	{
		this.player = g.getVideoController().getPlayer();
	}
	
	/**
	 * adds a time frame to the list of time frames.
	 * TODO find a way to make reference to model in final product
	 * @param tf 	Time frame to be added
	 */
	public void addTimeFrame(AbstractTimeFrame tf, int type) {
		
		// TODO delete and reference model
		timeFrames.add(new PanelTimeframe(tf, type, this));
		paintTimeFrames();
	}
	

	/**
	 * Method to invoke when the window has changed by zooming,
	 * scrolling or resizing. Repaints all time frames
	 */
	protected void paintTimeFrames()
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				runnableUpdater();
			}
		});
	}
	
	/**
	 * Call these actions if the containing JFrame is resized
	 */
	public void componentResized()
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				paintTimeFrames();
				indicator.setBounds(getIndicatorRectangle());
			}
		});
		
	}
	
	/**
	 * Use the Indicator.calculateBounds function with the component
	 * specific parameters
	 * @return	Rectangle containing indicator bounds
	 */
	protected Rectangle getIndicatorRectangle()
	{
		return indicator.calculateBounds(player.getMediaTime());
	}
	
	/**
	 * Code to execute when repainting
	 */
	protected void runnableUpdater()
	{
		paintTimeFrames();
	}
	
	/**
	 * Resizes a single time frame based on current visible area and 
	 * width of the canvas
	 * @param tf	time frame to be resized
	 */
	protected abstract void paintTimeFrame(PanelTimeframe tf);
	
	/**
	 * Calculate the x coordinate for a certain video time
	 * @param time 	the video time
	 * @return	int giving the x coordinate that corresponds 
	 * 				with the time
	 */
	protected abstract int xByTime(long time);
	
	/**
	 * Get the video time that corresponds with a certain x coordinate
	 * in the current view
	 * @param xCoord	The x coordinate in the current view
	 * @return			Time corresponding to x coordinate
	 */
	protected abstract long timeByX(int xCoord);
	
	/**
	 * Set the settings of the indicator, giving it the
	 * margin and the preferred width
	 */
	protected abstract void setIndicatorSettings();
	
}
