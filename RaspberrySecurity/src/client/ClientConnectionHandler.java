package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**Class responsible for establishing and retaining connection to the server.
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ClientConnectionHandler{
	/** Host or IP address */
	private String host;
	
	/** Port on the server */
	private int port;
	
	/** Created socket */
	private Socket socket;
	
	/** Input stream */
	private BufferedReader inStream;
	
	/** Output stream */
	private PrintStream outStream;
	
	/** Input Object stream */
	private ObjectInputStream inObjectStream;
	
	/** Output Object stream */
	private ObjectOutputStream outObjectStream;
	
	/** Associated listener */
	private ClientConnectionListener connectionListener;
	
	private ClientCommandPrompt commandPrompt;
	
	/**Constructor
	 * 
	 * @param host
	 * @param port
	 */
	public ClientConnectionHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}

	
	/** Connects to the server
	 * 
	 * @return true if connection was successful
	 */
	public boolean connect() {
		try {
			
			System.out.println("Connecting to " + host + ":" + port + ".");
			
			socket = new Socket(host, port);
			
			System.out.println("(+) Connected to " + host + ":" + port + "!");
			
			openStreams();
			
			connectionListener = new ClientConnectionListener(this);
			connectionListener.start();
			
			commandPrompt = new ClientCommandPrompt(outObjectStream);
			commandPrompt.start();
			
			return true;
		} catch (IOException e) {
			System.out.println("(-) Could not connect to the " + host + ":" + port);
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Opens the input and output streams
	 */
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


	public ClientCommandPrompt getCommandPrompt() {
		return commandPrompt;
	}


	public void setCommandPrompt(ClientCommandPrompt commandPrompt) {
		this.commandPrompt = commandPrompt;
	}
	
	
}
