package server;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ServerConnectionListener extends Thread {
	private Socket clientSocket;
	
	ObjectInputStream inObjectStream;
	ObjectOutputStream outObjectStream;
	
	
	BufferedReader inStream;
	PrintStream outStream;
	
	boolean stop;
	
	public ServerConnectionListener(Socket clientSocket) {
		this.clientSocket = clientSocket;
		
		openStreams();
	}
	
	
	
	private void openStreams() {
		try {
			System.out.println("Opening object output stream.");
			outObjectStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			System.out.println("Opening object input stream.");
			inObjectStream = new ObjectInputStream(clientSocket.getInputStream());
			
			System.out.println("(+) All streams has been succesfully opened.");
		} catch (IOException e) {
			System.out.println("(-) Could not open input or output stream.");
			e.printStackTrace();
		}
	}



	public void run() {
		stop = false;
		
		while(!stop){
			try {
				System.out.println("Waiting for message...");
				NetworkMessage msg = (NetworkMessage)(inObjectStream.readObject());
				
				if(msg == null) close();
				
				parseMessage(msg);
				
			} catch (ClassNotFoundException | IOException e) {
				close();
				System.out.println("(-) Error reading incoming message.");
				e.printStackTrace();
			}
		}
	}
	
	
	private void parseMessage(NetworkMessage msg) {
		System.out.println("Received message.");
		
		switch (msg.getMsgType()){
			case NetworkMessage.PICTURE:
				System.out.println("Message is a picture.");
				imgReceived(msg.getImageIcon());
				break;
			
			case NetworkMessage.COMMAND:
				System.out.println("Message is a command: " + msg.getCommand());
				cmdReceived(msg.getCommand());
				break;
				
			default:
				break;
		}
		
		
		
	}
	
	
	private void imgReceived(ImageIcon icon){
		try {
			BufferedImage buffImg = toBufferedImage(icon.getImage());
			ImageIO.write(buffImg, "jpg", new File("pictures/prijato.jpg"));
			System.out.println("(+) Picture succesfully saved.");
		} catch (IOException e) {
			System.out.println("(-) Error converting incoming message.");
			e.printStackTrace();
		}
	}
	
	private void cmdReceived(String command){
		
	}
	
	
	public BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	private void close(){
		System.out.println("Logging out.");
		try { 
			System.out.println("Closing output stream.");
			if(outStream != null) outStream.close();
		}
		catch(Exception e) {}
		try {
			System.out.println("Closing input stream.");
			if(inStream != null) inStream.close();
		}
		catch(Exception e) {};
		try {
			System.out.println("Closing socket.");
			if(clientSocket != null) clientSocket.close();
		}
		catch (Exception e) {}
		
		
		System.out.println("(+) All streams has been closed!");
		stop = true;
	}
}
