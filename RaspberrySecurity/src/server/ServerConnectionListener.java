package server;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/** Listens for incoming messages from associated client and sorts received images
 * to subfolders. 
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ServerConnectionListener extends Thread {
	/** The socket of the client this listener belongs to  */
	private Socket clientSocket;
	
	/** Stream for receiving objects */
	ObjectInputStream inObjectStream;

	/** Stream for sending objects */
	ObjectOutputStream outObjectStream;
	
	
	/** Connection handler this listener is associated to */
	ServerConnectionHandler connectionHandler;
	
	/** The only instance of an image for easy deallocation and to prevent memory leaks */
	BufferedImage bimage;
	
	/** If false the program will terminate the thread for listening for messages*/
	boolean stop;
	
	/** Output file containing all recorded events. */
	File log = new File("events.log");
	
	
	/**Creates and instance of a listener and opens input and output streams.
	 * 
	 * @param clientSocket - client socket
	 * @param connectionHandler - handler responsible for creating this instance
	 */
	public ServerConnectionListener(Socket clientSocket, ServerConnectionHandler connectionHandler) {
		this.clientSocket = clientSocket;
		this.connectionHandler = connectionHandler;
		
		openStreams();
	
		new ServerCommandPrompt(outObjectStream).start();;
	}
	
	
	
	
	/**
	 * Opens input object stream and output object stream for future communication
	 */
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


	/**
	 * Cycle for receiving messages
	 */
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
	
	
	/**Parses received message.
	 * 
	 * @param msg - received message
	 */
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
	
	/**If image is received, this method will save it to an archive and 
	 * to a file that is currently being showed on the website. 
	 * 
	 * @param msg - message containing the image
	 */
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
			logEvent("Movement detected.");
			System.out.println("(+) Picture succesfully saved.");
		} catch (IOException e) {
			System.out.println("(-) Error converting incoming message.");
			e.printStackTrace();
		}
	}
	
	/**Logs the event detected on GPIO
	 * @param command
	 */
	private void cmdReceived(String command){
		logEvent("Pin detected change of it's state: " + command);
	}
	
	
	/**Transforms ImageIcon to BufferedImage
	 * 
	 * @param icon - ImageIcon to be transformed
	 * @return BufferedImage taken from imageicon
	 */
	public BufferedImage toBufferedImage(ImageIcon icon)
	{
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
			
			return bi;
	}
	
	/**Logs the event with time and short description.
	 * 
	 * @param msg
	 */
	private void logEvent(String msg){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(log, true)))) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss - ");
			
			
		    out.println(sdf.format(date) + msg);
		}catch (IOException e) {
			System.out.println("(-) failed to write to the log file.");
		}
	}
	
	
	/**
	 * Closes all streams and the socket itself.
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
			if(clientSocket != null) clientSocket.close();
		}
		catch (Exception e) {}
		
		
		System.out.println("(+) All streams has been closed!");
		stop = true;
	}
}
