package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import security.Main;
import server.NetworkMessage;

public class ClientConnectionListener extends Thread {
	
	private ObjectInputStream inObjectStream;
	
	private ObjectOutputStream outObjectStream;
	
	private ClientConnectionHandler connectionHandler;
	
	boolean stop;
	
	public ClientConnectionListener(ClientConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		inObjectStream = connectionHandler.getInObjectStream();
		outObjectStream = connectionHandler.getOutObjectStream();
		stop = false;
	}
	
	
	
	public void run() {
		while(!stop){
			try {
				
				System.out.println("Waiting for message...");
				NetworkMessage msg = (NetworkMessage)inObjectStream.readObject();
				System.out.println("Received message");
				
				if(msg == null)close();
				
				parseMessage(msg);
				
			} catch (ClassNotFoundException e) {
				System.out.println("(-) Could not convert received object.");
			} catch (IOException e) {
				System.out.println("(-) Error reading stream.");
			}
		}
	}
	
	
	private void close(){
		System.out.println("Logging out.");
		try { 
			System.out.println("Closing output stream.");
			if(outObjectStream != null) outObjectStream.close();
		}
		catch(Exception e) {}
		try {
			System.out.println("Closing input stream.");
			if(inObjectStream != null) inObjectStream.close();
		}
		catch(Exception e) {};
		try {
			System.out.println("Closing socket.");
			if(connectionHandler.getSocket() != null) connectionHandler.getSocket().close();
		}
		catch (Exception e) {}
		
		Main.getSettings().setSend(false);
		
		System.out.println("(+) All streams has been closed!");
		stop = true;
	}
	
	
	private void parseMessage(NetworkMessage msg) {
		if(msg.getMsgType() != NetworkMessage.COMMAND) return;
		
		//TODO parse commands
	}



	public void sendPicture(ImageIcon img){
		try {
		
			System.out.println("Sending image.");
			NetworkMessage msg = new NetworkMessage(img);
			//outObjectStream.writeObject(msg);
			outObjectStream.writeUnshared(msg);
			outObjectStream.reset();
			System.out.println("(+) Image sent.");
	
		} catch (IOException e) {
			System.out.println("(-) Failed to send the image.");
			e.printStackTrace();
		}
	}
}
