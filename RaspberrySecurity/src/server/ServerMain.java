package server;

/**Starting point of the server application. 
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ServerMain {
	static ServerConnectionHandler connectionHandler;

	public static void main(String[] args) {
		connectionHandler = new ServerConnectionHandler(10000);
	}

}
