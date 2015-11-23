package view.navbar;

import java.awt.geom.Line2D;

import javax.swing.SwingUtilities;

import view.formatter.Time;
import view.player.IMediaPlayer;

public class TimeIndicator
{
	
	IMediaPlayer player;
	ABar parent;
	
	Line2D verticalLine;
	String currentPositionLabel;
	int curX;
	
	public TimeIndicator(ABar parent, IMediaPlayer player)
	{
		this.parent = parent;
		this.player = player;
	}
	
	public void repositionIndicator()
	{
		curX = parent.xByTime(player.getMediaTime());
		verticalLine = new Line2D.Float(curX, 0, curX, parent.getHeight());
		currentPositionLabel = Time.format(curX);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				parent.repaint();
			}
		});
	}
	
	public Line2D getLine()
	{
		return verticalLine;
	}
	
	public String getLabel()
	{
		return currentPositionLabel;
	}
	
	public int getPosition()
	{
		return curX;
	}
	
}