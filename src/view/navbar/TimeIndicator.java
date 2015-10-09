package view.navbar;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingConstants;

import view.player.IMediaPlayer;
import controller.Globals;
import controller.IVideoControls;

public class TimeIndicator extends JPanel {
	private JSeparator topDivider, indicator, bottomDivider;
	private int width, margin;
	private ABar parent;
	private Navbar navbar;
	private IMediaPlayer player;
	private IVideoControls vc;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for time indicator
	 */
	protected TimeIndicator(ABar parent, Globals g)
	{
		this.vc = g.getVideoController();
		this.player = g.getVideoController().getPlayer();
		this.parent = parent;
		navbar = parent.navbar;
		setLayout(null);
		this.setBackground(new Color(0,0,0,0));
		createSeparators();
		calculateBounds();
		addListeners();
	}
	
	protected void setMargins(int margin)
	{
		this.margin = margin;
	}
	
	protected void setPreferredWidth(int width)
	{
		this.width = width;
	}
	
	private void addListeners()
	{
		addComponentListener(new ComponentListener(){
			public void componentResized(ComponentEvent e) {
				calculateBounds();
			}
			public void componentMoved(ComponentEvent e) { }
			public void componentShown(ComponentEvent e) { }
			public void componentHidden(ComponentEvent e) { }
			
		});
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				navbar.setIsDragging(true);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				navbar.setIsDragging(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
		
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				if(navbar.isDragging())
				{
					int centreX = e.getX() - (int) Math.round((float) width / 2.0);
					long nt = player.getMediaTime() + parent.timeByX(centreX);
					System.out.format("x: %d\t\t diff t: %d\t\t\n", e.getX(), parent.timeByX(centreX));
					if(nt < 0) nt = 0;
					if(nt >= player.getMediaDuration()) nt = player.getMediaDuration()-1;
					vc.setMediaTime(nt);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if(draggableArea(e))
					setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
				else
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
	
	private boolean draggableArea(MouseEvent e)
	{
		return (float) e.getX() > (float) getWidth() * 0.3F && (float) e.getX() < (float) getWidth() * 0.6F;
	}
	
	/**
	 * Get the corrected x value for the size of the indicator
	 * to put the indicator at the correct position
	 * @param x		X-position
	 * @return		Corrected X-position
	 */
	protected int getXforX(int x)
	{
		return x - (getWidth() / 2);
	}
	
	protected void createSeparators()
	{
		topDivider = new JSeparator();
		topDivider.setForeground(Color.RED);
		add(topDivider);
		
		indicator = new JSeparator();
		indicator.setForeground(Color.RED);
		indicator.setOrientation(SwingConstants.VERTICAL);
		add(indicator);
		
		bottomDivider = new JSeparator();
		bottomDivider.setForeground(Color.RED);
		bottomDivider.setBounds(10, 287, 430, 1);
		add(bottomDivider);
	}
	
	protected Rectangle calculateBounds(long time)
	{
		int x = parent.xByTime(time);
		return new Rectangle(getXforX(x), margin, width, parent.getHeight() - 2*margin);
	}
	
	
	protected void calculateBounds()
	{
		topDivider.setBounds(0, 0, getWidth(), 1);
		bottomDivider.setBounds(0, getHeight()-1, getWidth(), 1);
		indicator.setBounds(getWidth() / 2, 0, 1, getHeight());
	}
}
