package server;

/**Starting point of the server application. 
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class ServerMain {
	static ServerConnectionHandler connectionHandler;

	public static void main(String[] args) {
		int port = 10000;
		
		if(args.length > 0 && args[0].equals("-h")){
			System.out.println("Help: To specify port lauch applicaton with -p [portNumber] parameter.");
			System.out.println("Example: java -jar server.jar -p 26600");
			return;
		}else if(args.length > 1 && args[0].equals("-p")){
			port = parsePort(args[1]);
		}
		connectionHandler = new ServerConnectionHandler(port);
	}

	
	/**Parses port number from argument
	 * 
	 * @param arg - String containing port
	 * @return
	 */
	private static int parsePort(String arg) {
		try {
			int port = Integer.parseInt(arg);
			if(port < 0 || port > 65535) throw new Exception();
			System.out.println("Using port " + port + ".");
			return port;
		} catch (Exception e) {
			System.out.println("Port could not be recognised. Using port 10000 instead.");
			return 10000;
		}
	}

}
