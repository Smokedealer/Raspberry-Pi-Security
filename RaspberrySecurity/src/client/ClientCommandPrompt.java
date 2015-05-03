package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import security.Main;
import server.NetworkMessage;

/**Provides command prompt for sending command to the server
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ClientCommandPrompt extends Thread {

	/** Scanner for user input */
	private Scanner sc;
	
	/** Output object stream for sending commands */
	private ObjectOutputStream outObjectStream;
	
	/**Constructor
	 * 
	 * @param outObjectStream
	 */
	public ClientCommandPrompt(ObjectOutputStream outObjectStream) {
		this.outObjectStream = outObjectStream;
	}
	
	/**
	 * Cycle for reading user input and sending messages to the server.
	 */
	@Override
	public void run() {
		sc = new Scanner(System.in);
		
		while(true){
			System.out.print("RPi Security Client >");
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
		System.out.println("help"); //TODO dodelat napovedu
		System.out.println("-------------------------------");
	}

	/**Sends the command to the server.
	 * 
	 * @param command
	 */
	private void sendCommand(String command){
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
