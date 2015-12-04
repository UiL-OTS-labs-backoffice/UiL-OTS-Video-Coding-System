package view.navbar;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.Globals;
import net.miginfocom.swing.MigLayout;

public class InformationPanel extends JPanel{
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
				setLayout(new MigLayout("insets 2 10 10 10", "[grow,fill]", "[20px:40px:40px,grow,fill][150px:n,grow,fill]"));
				add(overview, "cell 0 0,grow");
				add(detail, "cell 0 1,grow");
			}
		});
	}
}
