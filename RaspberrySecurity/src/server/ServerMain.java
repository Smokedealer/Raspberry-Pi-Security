package server;

public class ServerMain {
	static ServerConnectionHandler connectionHandler;

	public static void main(String[] args) {
		connectionHandler = new ServerConnectionHandler(10000);
	}

}
