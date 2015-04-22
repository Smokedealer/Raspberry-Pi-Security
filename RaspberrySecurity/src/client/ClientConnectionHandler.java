package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ClientConnectionHandler{
	private Socket socket;
	
	private BufferedReader inStream;
	
	private PrintStream outStream;
	
	private ObjectInputStream inObjectStream;
	
	private ObjectOutputStream outObjectStream;
	
	private ClientConnectionListener connectionListener;
	
	public ClientConnectionHandler(String host, int port) {
		connect(host, port);
	}

	private void connect(String host, int port) {
		try {
			
			System.out.println("Connecting to " + host + ":" + port + ".");
			
			socket = new Socket(host, port);
			
			System.out.println("(+) Connected to " + host + ":" + port + "!");
			
			openStreams();
			
			connectionListener = new ClientConnectionListener(this);
			connectionListener.start();
			
			new ClientCommandPrompt(outObjectStream).start();
			
		} catch (IOException e) {
			System.out.println("(-) Could not connect to the " + host + ":" + port);
			e.printStackTrace();
		}
	}
	
	
	private void openStreams() {
		try {
			System.out.println("Opening object output stream.");
			outObjectStream = new ObjectOutputStream(socket.getOutputStream());
			
			System.out.println("Opening object input stream.");
			inObjectStream = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("(+) All streams succesfully opened");
		
		} catch (IOException e) {
			System.out.println("(-) Could not open input or output stream.");
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getInStream() {
		return inStream;
	}

	public void setInStream(BufferedReader inStream) {
		this.inStream = inStream;
	}

	public PrintStream getOutStream() {
		return outStream;
	}

	public void setOutStream(PrintStream outStream) {
		this.outStream = outStream;
	}

	public ObjectInputStream getInObjectStream() {
		return inObjectStream;
	}

	public void setInObjectStream(ObjectInputStream inObjectStream) {
		this.inObjectStream = inObjectStream;
	}

	public ObjectOutputStream getOutObjectStream() {
		return outObjectStream;
	}

	public void setOutObjectStream(ObjectOutputStream outObjectStream) {
		this.outObjectStream = outObjectStream;
	}

	public ClientConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public void setConnectionListener(ClientConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}
	
	
}
