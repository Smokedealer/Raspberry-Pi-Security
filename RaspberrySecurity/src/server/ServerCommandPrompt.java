package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ServerCommandPrompt extends Thread {
	/** Scanner for user input */
	Scanner sc;
	
	ObjectOutputStream outObjectStream;
	
	public ServerCommandPrompt(ObjectOutputStream outObjectStream) {
		this.outObjectStream = outObjectStream;
	}
	
	/**
	 * Cycle for reading user input and sending messages to the client.
	 */
	@Override
	public void run() {
		sc = new Scanner(System.in);
		
		while(true){
			System.out.print("RPi Security Server >");
			String command = sc.nextLine();
			
			if(command.equals("help") || command.equals("h")){
				showHelp();
				continue;
			}
			
			sendCommand(command);
		}
	}
	
	
	/**
	 * Shows the list of available command to the user.
	 */
	private void showHelp() {
		System.out.println("-------------------------------");
		System.out.println("cam stop \t - stops the camera feed.");
		System.out.println("cam start \t - starts/resumes the camera feed.");
		System.out.println("pin stop \t - stops the GPIO detection.");
		System.out.println("pin start \t - starts the GPIO detecton.");
		System.out.println("-------------------------------");
	}
	
	
	/**Sends the command to the client.
	 * 
	 * @param command
	 */
	public void sendCommand(String command){
		try {
			System.out.println("Sending command - " + command);
			NetworkMessage msg = new NetworkMessage(command);
			outObjectStream.writeObject(msg);
			System.out.println("(+) Command sent.");
	
		} catch (IOException e) {
			System.out.println("(-) Failed to send the command.");
			e.printStackTrace();
		}
	}
}
