package view.navbar.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.navbar.ABar;
import view.navbar.Navbar;
import controller.IVideoControls;

public class TimeMouseListener implements MouseListener{
	
	private ABar comp; private Navbar navbar; private IVideoControls vc;
	
	public TimeMouseListener(ABar comp, Navbar navbar, IVideoControls vc)
	{
		this.comp = comp;
		this.navbar = navbar;
		this.vc = vc;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		final long newTime = comp.timeByXinView(x);
		new Thread(){
			public void run()
			{
				vc.setMediaTime(newTime);
			}
		}.start();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(comp.draggableArea(e)) navbar.setIsDragging(true);
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
	

}
