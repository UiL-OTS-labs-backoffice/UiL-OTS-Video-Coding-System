package view.panels;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;

import javax.swing.SwingConstants;
import java.awt.Font;

public class LoadingPanel extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel lblStatusMsg;
	
	public LoadingPanel(){
		setResizable(false);
		
		setUndecorated(true);
		getContentPane().setLayout(null);
		setSize(512,512);
		setLocationRelativeTo(null);
		setVisible(true);
		
		// Create logo
		JLabel splashImageLogo = new JLabel();
		splashImageLogo.setBounds(0, 0, 512,512);
		add(splashImageLogo);
		
		BufferedImage image;
		String filename = "/img/icons/512x512.png";
		try {
			image = ImageIO.read(LoadingPanel.class.getResource(filename));
			splashImageLogo.setIcon(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create text
		lblStatusMsg = new JLabel("Initializing Variables...");
		lblStatusMsg.setFont(new Font("Dialog", Font.PLAIN, 20));
		lblStatusMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatusMsg.setForeground(Color.WHITE);
		lblStatusMsg.setBounds(0, 470, 512, 40);
		add(lblStatusMsg);
		setComponentZOrder(lblStatusMsg, 0);
	}
	
	public void updateMsg(String msg){
		lblStatusMsg.setText(msg);
	}
}
