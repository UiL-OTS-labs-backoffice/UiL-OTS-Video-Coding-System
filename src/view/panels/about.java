package view.panels;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.LayoutStyle.ComponentPlacement;

public class About extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public About() {
		setResizable(false);
		setTitle("About: UiL OTS Video Coding System");
		setType(Type.UTILITY);
		this.setSize(361, 156);
		this.setLocation(600, 400);
		
		JLabel lblUilOtsVideo = new JLabel("UiL OTS Video Coding System V0.8.0.0 (Beta)");
		lblUilOtsVideo.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblCreatedByJan = new JLabel("Created by Jan de Mooij");
		
		JLabel lblCopyrightedBy = new JLabel("Copyrighted by");
		
		JLabel lblUilOtsLabs = new JLabel("UiL OTS Labs (http://uilots-labs.wp.hum.uu.nl/)");
		
		JLabel lblUniversiteitUtrecht = new JLabel("Universiteit Utrecht (http://www.uu.nl/)");
		
		JLabel lblajdemooijuunl = new JLabel("(A.J.deMooij@uu.nl)");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCopyrightedBy)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCreatedByJan)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblajdemooijuunl))
						.addComponent(lblUilOtsVideo)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniversiteitUtrecht)
								.addComponent(lblUilOtsLabs))))
					.addContainerGap(105, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUilOtsVideo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCreatedByJan)
						.addComponent(lblajdemooijuunl))
					.addGap(18)
					.addComponent(lblCopyrightedBy)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblUilOtsLabs)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblUniversiteitUtrecht)
					.addContainerGap(156, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
}
