package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionHandler{
	private ServerSocket serverSocket;
	
	private ArrayList<Socket> clients;
	
	private boolean run;
	
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
		
		
		while (run) {
			Socket newClient;
			
			
			try {
				newClient = serverSocket.accept();
				System.out.println("Client connected!");
				clients.add(newClient);
				
				System.out.println("Starting connection listener.");
				new ServerConnectionListener(newClient).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
