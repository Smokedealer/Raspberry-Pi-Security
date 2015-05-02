package server;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 
 * @author Matìj Kareš, karesm@students.zcu.cz
 *
 */
public class ServerConnectionListener extends Thread {
	private Socket clientSocket;
	
	ObjectInputStream inObjectStream;
	ObjectOutputStream outObjectStream;
	
	ServerConnectionHandler connectionHandler;
	
	BufferedReader inStream;
	PrintStream outStream;
	
	BufferedImage bimage;
	
	boolean stop;
	
	public ServerConnectionListener(Socket clientSocket, ServerConnectionHandler connectionHandler) {
		this.clientSocket = clientSocket;
		this.connectionHandler = connectionHandler;
		
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
		NetworkMessage msg;
		
		while(!stop){
			try {
				System.out.println("Waiting for message...");
				msg = (NetworkMessage)(inObjectStream.readUnshared());
				
				if(msg == null) close();
				
				parseMessage(msg);
				
				System.gc();
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
				imgReceived(msg);
				break;
			
			case NetworkMessage.COMMAND:
				System.out.println("Message is a command: " + msg.getCommand());
				cmdReceived(msg.getCommand());
				break;
				
			default:
				break;
		}
		
		
		
	}
	
	
	private void imgReceived(NetworkMessage msg){
		ImageIcon icon = msg.getImageIcon();
		try {
			BufferedImage buffImg = toBufferedImage(icon);
			File actImagePath = new File(connectionHandler.getWwwPath() + "actImage/img.jpg");
			actImagePath.mkdirs();
			ImageIO.write(buffImg, "png", actImagePath);
			
			SimpleDateFormat folderFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat fileFormat = new SimpleDateFormat("kk-mm-ss");
			File archivePath = new File(connectionHandler.getWwwPath() + "archive/" + (folderFormat.format(msg.getDate())) + "/" + (fileFormat.format(msg.getDate())) + ".png");
			archivePath.mkdirs();
			ImageIO.write(buffImg, "png", archivePath);
			System.out.println("(+) Picture succesfully saved.");
		} catch (IOException e) {
			System.out.println("(-) Error converting incoming message.");
			e.printStackTrace();
		}
	}
	
	private void cmdReceived(String command){
		//TODO n
		
	}
	
	
	public BufferedImage toBufferedImage(ImageIcon icon)
	{
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
			
			return bi;
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
