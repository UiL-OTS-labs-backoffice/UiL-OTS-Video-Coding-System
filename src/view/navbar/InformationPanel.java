package view.navbar;

import javax.swing.JPanel;

import controller.Globals;
import model.AbstractTimeFrame;
import net.miginfocom.swing.MigLayout;

public class InformationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private OverviewBar overview;
	private DetailBar detail;
	
	public static final int TYPE_TRIAL = 0;
	public static final int TYPE_LOOK = 1;
	
	protected InformationPanel(Navbar navbar, Globals g)
	{
		setLayout(new MigLayout("", "[grow,fill]", "[20px:40px:40px,grow,fill][40px:50px:100px,fill][200px:n,grow,fill]"));
		
		overview = new OverviewBar(navbar, g);
		add(overview, "cell 0 0,grow");
		overview.repaint();
		
		
		detail = new DetailBar(navbar, g);
		add(detail, "cell 0 2,grow");
		detail.repaint();
	}
	
	public void videoInstantiated()
	{
		overview.videoInstantiated();
		detail.videoInstantiated();
	}
	
	public void addTimeFrame(AbstractTimeFrame tf, int type) {
		// TODO: remove. Draw from model
		overview.addTimeFrame(tf, type);
		detail.addTimeFrame(tf, type);
	}
	
	public void componentResized()
	{
		overview.componentResized();
		detail.paintTimeFrames();
	}

	public void repaintDetails() {
		detail.paintTimeFrames();
		overview.componentResized();
	}
}
