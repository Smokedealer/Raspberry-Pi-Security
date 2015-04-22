package security;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.Timer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class GUI extends JFrame {
	
	JFrame frame;
	JPanel controlPanel;
	Webcam webcam;
	WebcamPanel webcamPanel;
	
	JButton snap;
	JLabel cameraLabel, fpsLabel, resolutionLabel, connectedLabel, portLabel, eventsLabel;
	JList<String> events;
	
	

	public GUI(String title, Webcam webcam) {
		frame = this;
		this.webcam = webcam;
		
		setTitle(title);
		setSize(new Dimension(800, 480));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
				
		webcamPanel = new WebcamPanel(webcam);
		webcamPanel.setSize(640, 480);
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 360));
		controlPanel.setLayout(new GridBagLayout());

		setLayout(new BorderLayout());
		
		initItems();
		
		add(webcamPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.EAST);
		pack();
		
		repaint();
		
		Timer t = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshPanel();
			}
		});
		
		t.start();
	}
	
	private void refreshPanel(){
		fpsLabel.setText("FPS " + webcam.getFPS());
	}
	
	private void initItems(){
		GridBagConstraints gbc = new GridBagConstraints();
		
		snap = new JButton("Snapshot!");
		
		snap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ImageIO.write(webcam.getImage(), "PNG", new File("snap-" + System.currentTimeMillis() + ".png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
		controlPanel.add(snap, gbc);
		
		cameraLabel = new JLabel("Camera: " + webcam.getName());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(cameraLabel, gbc);
		
		fpsLabel = new JLabel("FPS: " + webcam.getFPS());
		gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(fpsLabel, gbc);
		
		resolutionLabel = new JLabel("Resolution: " + webcam.getViewSize().width + "x" + webcam.getViewSize().height);
		gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(resolutionLabel, gbc);
		
		connectedLabel = new JLabel("Server: ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(connectedLabel, gbc);
		
		portLabel = new JLabel("Port: ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(portLabel, gbc);
		
		eventsLabel = new JLabel("Events: ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
		controlPanel.add(eventsLabel, gbc);
		
		JScrollPane scrollPane = new JScrollPane();
		DefaultListModel<String> model = new DefaultListModel<String>();
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		model.addElement("14.12.2015 14:30:15");
		events = new JList<String>(model);
		scrollPane.setViewportView(events);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
		controlPanel.add(scrollPane, gbc);
		
	}
}
