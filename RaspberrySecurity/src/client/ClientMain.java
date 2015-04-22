package client;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ClientMain {

	static ClientConnectionHandler handler;
	
	public static void main(String[] args) {
		handler = new ClientConnectionHandler("localhost", 10000);
		
		try {
			ImageIcon img = new ImageIcon(ImageIO.read(new File("pictures/odesilan.jpg")));
			handler.getConnectionListener().sendPicture(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
