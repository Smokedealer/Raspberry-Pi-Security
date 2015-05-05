package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import security.Main;
import server.NetworkMessage;

/**Listens for incoming messages and behaves accordingly to them. 
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ClientConnectionListener extends Thread {
	/** Input Object Stream */
	private ObjectInputStream inObjectStream;
	
	/** Output Object Stream */
	private ObjectOutputStream outObjectStream;
	
	/** Handler associated to this listener */
	private ClientConnectionHandler connectionHandler;
	
	/** If true this listener stops to listen for incoming messages */
	boolean stop;
	
	/**Constructor
	 * 
	 * @param connectionHandler
	 */
	public ClientConnectionListener(ClientConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		inObjectStream = connectionHandler.getInObjectStream();
		outObjectStream = connectionHandler.getOutObjectStream();
		stop = false;
	}
	
	
	/**
	 * Cycle for receiving messages and passing them to the parser.
	 */
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
				close();
			} catch (IOException e) {
				close();
				System.out.println("(-) Error reading stream.");
			}
		}
	}
	
	/**
	 * Closes all streams and socket itself. Switches the destination of saved files to the local file system.
	 */
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
		
		if(msg.getCommand().equals("cam stop")){
			System.out.println("Stopping the camera.");
			Main.setCamSecure(false);
		}else if(msg.getCommand().equals("cam start")){
			Main.setCamSecure(true);
		}else if(msg.getCommand().equals("pin stop")){
			Main.setGpioSecure(false);
		}else if(msg.getCommand().equals("pin start")){
			Main.setGpioSecure(true);
		}else{
			System.out.println("Command not recognised.");
		}
	}


	/**Sends the image to the server.
	 * 
	 * @param img
	 */
	public void sendPicture(ImageIcon img){
		try {
		
			System.out.println("Sending image.");
			NetworkMessage msg = new NetworkMessage(img);
			outObjectStream.writeUnshared(msg);
			outObjectStream.reset();
			System.out.println("(+) Image sent.");
	
		} catch (IOException e) {
			System.out.println("(-) Failed to send the image.");
			e.printStackTrace();
		}
	}
}
