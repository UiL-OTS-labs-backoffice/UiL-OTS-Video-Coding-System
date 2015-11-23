package view.navbar;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.Globals;
import net.miginfocom.swing.MigLayout;

public class InformationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private OverviewBar overview;
	private DetailBar detail;
	
	public static final int TYPE_TRIAL = 0;
	public static final int TYPE_LOOK = 1;
	
	protected InformationPanel(Navbar navbar, Globals g)
	{
		overview = new OverviewBar(navbar, g);
		detail = new DetailBar(navbar, g);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new MigLayout("", "[grow,fill]", "[20px:40px:40px,grow,fill][150px:n,grow,fill]"));
				add(overview, "cell 0 0,grow");
				add(detail, "cell 0 1,grow");
			}
		});
	}
	
	public void videoInstantiated()
	{
		overview.videoInstantiated();
		detail.videoInstantiated();
		componentResized();
	}
	
	/**
	 * If the component is resized, everything (all panels, overview box and indicators)
	 * has to be updated 
	 */
	public void componentResized()
	{
		overview.paintTimeFrames();
		overview.paintBox();
		detail.paintTimeFrames();
		mediaTimeChanged();
	}
	
	/**
	 * If the visible area changed (resized, start time changed, etc), all VISIBLE
	 * panels in the detail view have to be redrawn. The overview box in the overview 
	 * has to be redrawn. That's it 
	 */
	public void visibleAreaChanged()
	{
		overview.paintBox();
		detail.paintTimeFrames();
		detail.repaint();
	}
	
	/**
	 * Update indicators only
	 */
	public void mediaTimeChanged()
	{
		overview.mediaTimeChanged();
		detail.mediaTimeChanged();
	}
	
//	public void addTimeFrame(model.AbstractTimeFrame tf)
//	{
//		overview.addTimeFrame(tf);
//		detail.addTimeFrame(tf);
//	}
//	
//	public void removeTimeFrame(model.AbstractTimeFrame tf)
//	{
//		overview.removeTimeFrame(tf);
//		detail.removeTimeFrame(tf);
//	}
}
