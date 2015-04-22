package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import server.NetworkMessage;

public class ClientCommandPrompt extends Thread {

	private Scanner sc;
	
	private ObjectOutputStream outObjectStream;
	
	public ClientCommandPrompt(ObjectOutputStream outObjectStream) {
		this.outObjectStream = outObjectStream;
	}
	
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
	
	private void showHelp() {
		System.out.println("-------------------------------");
		System.out.println("help"); //TODO dodelat napovedu
		System.out.println("-------------------------------");
	}

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
