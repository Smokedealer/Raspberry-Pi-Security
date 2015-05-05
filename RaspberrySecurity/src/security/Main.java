package security;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import client.ClientConnectionHandler;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


/**The core of the application. Handles the user settings, motion detection and offers GUI.
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class Main {
	
	/**
	 * Sets the driver responsible for accessing the camera.
	 */
	static {
		/*Uncomment on Raspberry Pi. Comment else.*/
		
	    Webcam.setDriver(new V4l4jDriver());
	}
	
	/** Folder for pictures to be saved while not connected to the server */
	static File outFile;
	
	/** Object holds the information about the settings of the application */
	static StartupSettings settings;
	
	/** Connection handler reference */
	static ClientConnectionHandler clientHandler;
	
	/** Scanner for user input */
	static Scanner sc;
	
	/** Webcam for the whole project */
	static Webcam webcam;
	
	
	/** Whether to check for changes on GPIO or not */
	static boolean gpioSecure;
	
	
	/** Whether to react to camera input or not */
	static boolean camSecure;
	
	
	/**Call all the necessary methods to start the application
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		sc = new Scanner(System.in);
		
		webcam = Webcam.getDefault();
		
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
			System.out.println("Got the camera.");
		} else {
			System.out.println("No webcam detected");
			return;
		}
		
		if(args.length > 0 && args[0].equals("-r")){
			startUpManager(webcam);
		}else if(args.length > 0 && args[0].equals("-h")){
			System.out.println("Help: Start the program with -r to delete old settings and input new.");
			System.out.println("Example: java -jar ./monitor.jar -r");
			return;
		}
		
		outFile = new File("./pictures");
		outFile.mkdirs();
		
		loadSettings(webcam);
			
		webcam.open();
		
		setDoorControl();
		detectMotion(webcam);
		
		if(settings.isGui()) new GUI("Security Panel", webcam);
		else{
			System.out.println("Running...");
			
		}
	}
	
	
	
	private static void setDoorControl() {
		
		final GpioController gpio = GpioFactory.getInstance();
		
		final GpioPinDigitalInput door = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN);
		
		
		door.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent arg0) {
				if(!gpioSecure) return;
				System.out.println("Circuit state changed!");
				if(settings.send){
					clientHandler.getCommandPrompt().sendCommand("int " + arg0.getPin() + " " + arg0.getState());
				}else{
					System.out.println("int " + arg0.getPin() + " " + arg0.getState());
				}
			}
		});
	}



	/**Loads the preset settings
	 * 
	 * @param webcam
	 */
	private static void loadSettings(Webcam webcam) {
		settings = StartupSettings.load();
		
		if(settings == null) {
			settings = new StartupSettings();
			startUpManager(webcam);
		}else{
			webcam.setViewSize(settings.getResolution());
			
			if(settings.isSend()){
				clientHandler = new ClientConnectionHandler(settings.getServer(), settings.getPort());
				boolean connected = clientHandler.connect();
				if(!connected){
					System.out.println("Connection could not be astablished. Server unreachable.");
					settings.setSend(false);
				}
			}
		}
	}

	
	/**
	 * Gets the information about settings from user.
	 */
	private static void startUpManager(Webcam webcam) {
		settings = new StartupSettings();
		
		setResolution(webcam);
		setGui();
		setConnection();
		
		settings.save();
	}
	
	
	
	/**
	 * Wizard for setting the connection to the server
	 */
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
				break;
			}else{
				System.out.println("Invalid choice.");
			}
		}
	}

	
	/**
	 * Asks the user whether they want GUI or not. Sets the application accordingly.
	 */
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


	/**Gets all the available resolution on specified webcam and let's the user to chose one of them.
	 * Sets the application accordingly.
	 * 
	 * @param webcam
	 */
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

	
	/**
	 * Method responsible for catching motion from captured images.
	 * 
	 * @param webcam
	 */
	private static void detectMotion(final Webcam webcam){
		WebcamMotionDetector wmd = new WebcamMotionDetector(webcam);
		
		wmd.setInterval(500);
		
		wmd.addMotionListener(new WebcamMotionListener() {
			
			@Override
			public void motionDetected(WebcamMotionEvent arg0) {
				if(!camSecure)return;
				
				if(settings.isSend()){
					clientHandler.getConnectionListener().sendPicture(new ImageIcon(webcam.getImage()));
				}else{
					try {
						Date date = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy---kk-mm-ss");
						ImageIO.write(webcam.getImage(), "jpg", new File("./pictures/" + dateFormat.format(date) + ".jpg"));
						System.out.println("Image saved.");
					} catch (IOException e) {
						System.out.println("(-) Failed to save the image.");
					}
				}
			}
		});
		
		wmd.start();
	}


	public static StartupSettings getSettings() {
		return settings;
	}


	public static void setSettings(StartupSettings settings) {
		Main.settings = settings;
	}



	public static Webcam getWebcam() {
		return webcam;
	}



	public static boolean isGpioSecure() {
		return gpioSecure;
	}



	public static void setGpioSecure(boolean gpioSecure) {
		Main.gpioSecure = gpioSecure;
	}



	public static boolean isCamSecure() {
		return camSecure;
	}



	public static void setCamSecure(boolean camSecure) {
		Main.camSecure = camSecure;
	}

	
}
