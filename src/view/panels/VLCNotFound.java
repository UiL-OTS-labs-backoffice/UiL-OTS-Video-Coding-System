package view.panels;

import javax.swing.JFrame;

import java.awt.CardLayout;
import java.awt.Panel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;

import controller.Globals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VLCNotFound extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Panel panel1;
	private JPanel panel2;
	private model.ApplicationPreferences prefs;

	public VLCNotFound(model.ApplicationPreferences prefs) {
		this.prefs = prefs;
		
		setTitle("Error! VLC could not be found");
		setLocation(400, 150);
		setSize(520,450);
        setIconImages(Globals.getIcons());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new CardLayout(0, 0));
		
		addPanel1();
		addPanel2();
	}
	
	private void addPanel1()
	{
		panel1 = new Panel();
		getContentPane().add(panel1, "name_5828589986876");
		
		JLabel lblVlcNotFound = new JLabel("<html><h2><font color=\"red\">Error!</font><br/> No installation of VLC was found on your computer!</h2>\r\n\r\n<p>This application requires VLC to play video. However, no installation of VLC could be found on your computer.</p><br/><p>Theoretically all versions of VLC should be supported. Do make sure the <i>plugins</i> and <i>lua</i> directories are included in your VLC installation directory (this should be the case in any default VLC installation)</p><br/>\r\n\r\n<p>Please make sure you have VLC installed (you can get VLC <a href=\"http://www.videolan.org/vlc/\">here</a>) and restart the application</p>");
		
		JButton btnOkIllInstall = new JButton("Ok! I'll install VLC");
		btnOkIllInstall.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		JButton btnIAlreadyHave = new JButton("I already have VLC");
		btnIAlreadyHave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				panel1.setVisible(false);
				panel2.setVisible(true);
			}
			
		});
		
		GroupLayout gl_panel1 = new GroupLayout(panel1);
		gl_panel1.setHorizontalGroup(
			gl_panel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel1.createSequentialGroup()
							.addComponent(btnOkIllInstall)
							.addPreferredGap(ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
							.addComponent(btnIAlreadyHave))
						.addComponent(lblVlcNotFound, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel1.setVerticalGroup(
			gl_panel1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel1.createSequentialGroup()
					.addGap(31)
					.addComponent(lblVlcNotFound)
					.addPreferredGap(ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
					.addGroup(gl_panel1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOkIllInstall)
						.addComponent(btnIAlreadyHave))
					.addContainerGap())
		);
		panel1.setLayout(gl_panel1);
	}
	
	private void addPanel2()
	{
		panel2 = new JPanel();
		getContentPane().add(panel2, "name_6100887740106");
		
		JLabel lblNewLabel = new JLabel("<html><p>You indicate you already have VLC installed on your computer.</p><br><p>We could not find the installation of VLC, but that does not mean it doesn't exist. We only look for the most common locations.</p><br>\r\n\r\n<p>If you're sure you have VLC installed, please click the button <i>Specify VLC path</i>. This will open a new dialog where you can select the installation path of VLC. Please make sure select the folder\r\n<pre>.../videoLAN/VLC/</pre></p>The path should contain at least these files:<pre>libvlc.dll<br/>libvlccore.dll</pre>If the folders <i>lua</i> and <i>plugins</i> are not available in the same directory, please specify those as environment variables in your system.<br/><br/>After selecting the path, close the application and restart.</p><br><p>If you already specified the path before, try another path. If you're sure the path is correct, it seems this application will not run on your computer</p><br><p>If you do not have VLC installed, you can close this application, install VLC and then restart this application.</p>");
		
		JButton btnSpecifyVlcPath = new JButton("Specify VLC Path");
		btnSpecifyVlcPath.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String newPath = VLCLibSelector.show();
				prefs.setVLCUrl(newPath);
			}
		});
		
		JButton btnOopsTakeMe = new JButton("Oops, take me back!");
		btnOopsTakeMe.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				panel1.setVisible(true);
				panel2.setVisible(false);
			}
		});
		
		JButton btnGotItClose = new JButton("Got it! Close this application");
		btnGotItClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		GroupLayout gl_panel2 = new GroupLayout(panel2);
		gl_panel2.setHorizontalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_panel2.createSequentialGroup()
							.addComponent(btnSpecifyVlcPath)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOopsTakeMe)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnGotItClose)))
					.addContainerGap())
		);
		gl_panel2.setVerticalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addGroup(gl_panel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSpecifyVlcPath)
						.addComponent(btnOopsTakeMe)
						.addComponent(btnGotItClose))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addContainerGap())
		);
		
		panel2.setLayout(gl_panel2);
	}
}

