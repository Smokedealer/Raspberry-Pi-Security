package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerConnectionHandler{
	private ServerSocket serverSocket;
	private DatagramSocket webSocket;
	
	private ArrayList<Socket> clients;
	
	private boolean run;
	
	private String wwwPath;
	
	public ServerConnectionHandler(int port) {
		clients = new ArrayList<Socket>(1);
		run = true;
		startServer(port);
	}

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
				setWwwPath("D:/");
				//setWwwPath("/var/www");
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
