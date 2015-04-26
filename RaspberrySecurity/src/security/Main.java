package security;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import client.ClientConnectionHandler;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;



public class Main {
/*
	static {
	    Webcam.setDriver(new V4l4jDriver());
	}*/
	
	
	static int pocet = 0;
	
	static StartupSettings settings;
	
	static ClientConnectionHandler clientHandler;
	
	static Scanner sc;
	
	public static void main(String[] args) throws IOException {
		sc = new Scanner(System.in);
		
		Webcam webcam = Webcam.getDefault();
		
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
			System.out.println("Got the camera.");
		} else {
			System.out.println("No webcam detected");
			return;
		}
		
		if(args.length > 0 && args[0].equals("-r")){
			settings = new StartupSettings();
			startUpManager(webcam);
		}
		
		loadSettings(webcam);
			
		webcam.open();
		
		detectMotion(webcam);
		
		if(settings.isGui()) new GUI("Security Panel", webcam);
		else{
			System.out.println("Running...");
		}
	}
	
	
	private static void loadSettings(Webcam webcam) {
		settings = StartupSettings.load();
		
		if(settings == null) {
			settings = new StartupSettings();
			startUpManager(webcam);
		}else{
			webcam.setViewSize(settings.getResolution());
			clientHandler = new ClientConnectionHandler(settings.getServer(), settings.getPort());
			clientHandler.connect();
		}
	}


	private static void startUpManager(Webcam webcam) {
		setResolution(webcam);
		setGui();
		setConnection();
		settings.save();
	}
	
	private static void setConnection() {
		while (true){
			System.out.println("Do you want to connect to remote server? [y/n]");

			String choice = sc.nextLine();
			
			if(choice.equals("y")){
				System.out.print("Enter the IP address or Hostname of the server: ");
				String address = sc.nextLine();
				
				int port;
				while(true){
					try {
						System.out.print("Enter the port: ");
						port = sc.nextInt();
						sc.nextLine();
						
						if(port < 0 || port > 65535){
							System.out.println("Port out of range (0 - 65536).");
							continue;
						}
						
						System.out.println("Port set to " + port);
						
						break;
					} catch (Exception e) {
						sc.nextLine();
						System.out.println("Invalid input.");
						continue;
					}
				}
				
				settings.setServer(address);
				settings.setPort(port);
				settings.setSend(true);
				
				System.out.println("Connection to server enabled.");
				break;
			}else if(choice.equals("n")){
				settings.setSend(false);
				System.out.println("");
				break;
			}else{
				System.out.println("Invalid choice.");
			}
		}
	}


	private static void setGui() {
		while (true){
			System.out.println("Do you want to show GUI? [y/n]");

			String choice = sc.nextLine();
			
			if(choice.equals("y")){
				settings.setGui(true);
				System.out.println("Show GUI set to YES.");
				break;
			}else if(choice.equals("n")){
				settings.setGui(false);
				System.out.println("Show GUI set to NO.");
				break;
			}else{
				System.out.println("Invalid choice.");
			}
		}
	}


	public static void setResolution(Webcam webcam){
		System.out.println("Type in the number of desired resolution: ");
		
		int i = 1;
		for(Dimension d : webcam.getViewSizes()){
			System.out.println(i + ") " + d.width + "x" + d.height);
			i++;
		}
		
		int choice = 0;
		
		while(true){
			try {
				System.out.print("Your choice: ");
				choice = sc.nextInt();
				System.out.println("Chosen resolution (" + choice + ")");
				sc.nextLine();
				choice--;
				break;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input.");
				continue;
			}
		}
		
		
		if(choice < 0 || choice > webcam.getViewSizes().length){
			System.out.println("Selected resolution does not exist. Selecting first available.");
			choice = 0;
		}
		
		Dimension selected = webcam.getViewSizes()[choice];
		webcam.setViewSize(selected);
		
		settings.setResolution(selected);
	}


	private static void detectMotion(final Webcam webcam){
		WebcamMotionDetector wmd = new WebcamMotionDetector(webcam);
		
		wmd.setInterval(100);
		
		wmd.addMotionListener(new WebcamMotionListener() {
			
			@Override
			public void motionDetected(WebcamMotionEvent arg0) {
				pocet++;
				System.out.println("Motion detected!! " + pocet);
			}
		});
		
		wmd.start();
	}
	


}
