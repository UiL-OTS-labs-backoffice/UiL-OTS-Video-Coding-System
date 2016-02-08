package view.panels;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.LayoutStyle.ComponentPlacement;

import controller.Globals;

public class About extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public About() {
		setResizable(false);
		setTitle("About: UiL OTS Video Coding System");
		setIconImages(Globals.getIcons());
		this.setSize(400, 250);
		this.setLocation(600, 400);
		
		JLabel lblUilOtsVideo = new JLabel("UiL OTS Video Coding System V3.8.6 (Hello there)");
		lblUilOtsVideo.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblCreatedByJan = new JLabel("Created by Jan de Mooij");
		
		JLabel lblCopyrightedBy = new JLabel("Copyrighted by");
		
		JLabel lblUilOtsLabs = new JLabel("UiL OTS Labs (http://uilots-labs.wp.hum.uu.nl/)");
		
		JLabel lblUniversiteitUtrecht = new JLabel("Universiteit Utrecht (http://www.uu.nl/)");
		
		JLabel lblajdemooijuunl = new JLabel("(A.J.deMooij@uu.nl)");
		
		JLabel lblUses = new JLabel("Uses:");
		
		JLabel lblVlcj = new JLabel("VLCj 2.4.0");
		
		JLabel lblJna = new JLabel("JNA 3.5.2");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUses)
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
								.addComponent(lblUilOtsLabs)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblJna)
								.addComponent(lblVlcj))))
					.addContainerGap(16, Short.MAX_VALUE))
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblUses)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblVlcj)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblJna)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
}