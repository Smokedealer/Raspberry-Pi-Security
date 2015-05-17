package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**This class handles the connection(s) of client(s).
 * Creates listeners for incoming messages and allows user
 * to set their desired path for images to be saved. 
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ServerConnectionHandler{
	/** Socket for clients to connect to. */
	private ServerSocket serverSocket;
	
	/** Arraylist of connected clients (only one expected, this is for future expansions) */
	private ArrayList<Socket> clients;
	
	
	/** Signalizes when to stop listening for clients. */
	private boolean run;
	
	/** Path to the root of the webserver responsible for showing the pictures online */
	private String wwwPath;
	
	
	
	
	/**Initializes default variables and starts listening for clients.
	 * 
	 * @param port - which port is the server listening on
	 */
	public ServerConnectionHandler(int port) {
		clients = new ArrayList<Socket>(1);
		run = true;
		startServer(port);
	}

	
	
	
	/**Opens the server socket. Sets the www root folder. Listens for connections. Adding clients to array.
	 * Creates new instance of connection listener for each client accepted.
	 * 
	 * @param port 
	 */
	private void startServer(int port) {
		try {
			System.out.println("Starting server on port " + port + "!");
			serverSocket = new ServerSocket(port); 
			System.out.println("Server started!");
		} catch (IOException e) {
			System.out.println("Failed to start the server!");
			e.printStackTrace();
		}
		
		wwwPathManager();
		
		
		while (run) {
			Socket newClient;
			
			
			try {
				newClient = serverSocket.accept();
				System.out.println("Client connected!");
				clients.add(newClient);
				
				System.out.println("Starting connection listener.");
				new ServerConnectionListener(newClient, this).start();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	
	/** 
	 * Provides a wizard for easy www root folder setup
	 */
	private void wwwPathManager() {
		System.out.println("Do you want to specify location where the displaying web is located? default: \"/var/www/\" [y/n]: ");
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			String choice = sc.nextLine();
			
			if (choice.equals("y")) {
				System.out.print("Enter the location of the webserver's www folder: ");
				setWwwPath(sc.nextLine());
				

				break;
			} else if (choice.equals("n")) {
				//setWwwPath("D:/");
				setWwwPath("/var/www/");
				break;
			} else {
				System.out.println("Invalid choice.");
			}
		}
	}

	public String getWwwPath() {
		return wwwPath;
	}

	public void setWwwPath(String wwwPath) {
		this.wwwPath = wwwPath;
	}
	
	
}
