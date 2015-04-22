package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import server.NetworkMessage;

public class ClientConnectionListener extends Thread {
	
	private ObjectInputStream inObjectStream;
	
	private ObjectOutputStream outObjectStream;
	
	public ClientConnectionListener(ClientConnectionHandler connectionHandler) {
		inObjectStream = connectionHandler.getInObjectStream();
		outObjectStream = connectionHandler.getOutObjectStream();
	}
	
	
	
	public void run() {
		while(true){
			try {
				
				System.out.println("Waiting for message...");
				NetworkMessage msg = (NetworkMessage)inObjectStream.readObject();
				System.out.println("Received message");
				
				parseMessage(msg);
				
			} catch (ClassNotFoundException e) {
				System.out.println("(-) Could not convert received object.");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("(-) Error reading stream.");
				e.printStackTrace();
			}
		}
	}
	
	private void parseMessage(NetworkMessage msg) {
		if(msg.getMsgType() != NetworkMessage.COMMAND) return;
		
		//TODO parse commands
	}



	public void sendPicture(ImageIcon img){
		try {
		
			System.out.println("Sending image.");
			NetworkMessage msg = new NetworkMessage(img);
			outObjectStream.writeObject(msg);
			System.out.println("(+) Image sent.");
	
		} catch (IOException e) {
			System.out.println("(-) Failed to send the image.");
			e.printStackTrace();
		}
	}
}
