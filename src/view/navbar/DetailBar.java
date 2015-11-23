package view.navbar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import model.AbstractTimeFrame;
import controller.Globals;
import view.formatter.Time;
import view.navbar.paneltimeframe.PanelTimeframe;



public class DetailBar extends ABar{
	
	private static final long serialVersionUID = 1L;
	private static final int TYPE = ABar.TYPE_DETAIL;
	
	/**
	 * Construct a new detail bar JPanel
	 * @param duration		Total video duration
	 * @param navbar		Reference to parent Navbar object
	 */
	protected DetailBar(Navbar navbar, Globals g)
	{
		super(navbar, g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setBorder(new MatteBorder(1, 1, 1, 1,
						(Color) new Color(0, 0, 0)));
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void paintTimeFrame(final PanelTimeframe tf)
	{
		int left = xByTime(tf.getStart());
		int right = xByTime((tf.getEnd() == -1L) ? Globals.getInstance().getVideoController().getMediaTime() : tf.getEnd());
		int y = (tf.getType() == AbstractTimeFrame.TYPE_LOOK) ? getHeight() / 2 + 20
				: 35;
		final Rectangle r = new Rectangle(left, y, right - left,
				(getHeight() / 2) - 30);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tf.setSize(r);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void repaintHallmarks(Graphics g)
	{
		g.setColor(Color.GRAY);
		int amount = Math.round(getWidth() / 10f);
		for(int i = 0; i < amount; i++)
		{
			int height; 
			final int x = (i) * 10;
			if(i % 10 == 0)
			{
				height = getHeight() - 21;
				StringPosition sp = new StringPosition(x);
				g.drawString(sp.getString(), sp.getX(), sp.getY());
			} else { 
				height = 10;
			}
			Line2D line = new Line2D.Float(x, 20, x, 20 + height);
			Graphics2D g2 = (Graphics2D) g;
			g2.draw(line);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int xByTime(long time)
	{
		float tNorm = ((float) time - (float) navbar.getCurrentStartVisibleTime()) / ((float) navbar.getCurrentEndVisibleTime() - (float) navbar.getCurrentStartVisibleTime());
		return (int) (tNorm * getWidth());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public long timeByX(int xCoord)
	{
		return (long) ((float) xCoord / (float) getWidth() * navbar.getVisibleTime());
	}
	
	public long timeByXinView(int xCoord)
	{
		long time = (long) Math.round( 
				((float) xCoord / (float) getWidth()) *
				((float) navbar.getVisibleTime())
				+ (float) navbar.getCurrentStartVisibleTime());
		return time;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getType()
	{
		return TYPE;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		repaintHallmarks(g);
	}
	
	private class StringPosition
	{
		private String s;
		private int x,y;
		
		public StringPosition(int x)
		{
			this.s = Time.format(timeByX(x) + navbar.getCurrentStartVisibleTime());
			this.x = x;
			this.y = 15;
		}
		
		public String getString()
		{
			return s;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
	}
	
}
