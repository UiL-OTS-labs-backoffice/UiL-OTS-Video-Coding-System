package view.navbar.listeners;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import view.navbar.ABar;
import view.navbar.Navbar;
import controller.IVideoControls;

public class TimeMouseMotionListener implements MouseMotionListener{
	private ABar comp; private Navbar navbar; private IVideoControls vc;
	public TimeMouseMotionListener(ABar comp, Navbar navbar, IVideoControls vc)
	{
		this.comp = comp;
		this.navbar = navbar;
		this.vc = vc;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		final int centreX = e.getX() - comp.xByTime(vc.getMediaTime());
		if(navbar.isDragging()) {
			new Thread(){
				public void run()
				{
					long nt = vc.getMediaTime() + comp.timeByX(centreX);
					if(nt < 0) nt = 0;
					if(nt > vc.getMediaDuration()) nt = vc.getMediaDuration() - 1;
					vc.setMediaTime(nt);
				}
			}.start();
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(comp.draggableArea(e)) comp.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		else comp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	

}
