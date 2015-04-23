package security;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StartupSettings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4123877317857569136L;
	
	private static String iniFile = "settings.ini";

	Dimension resolution;
	boolean gui;
	String server;
	int port;
	boolean send;

	public static StartupSettings load() {
		try {
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream(new File(iniFile)));
			StartupSettings ret = (StartupSettings) loader.readObject();
			loader.close();
			return ret;
		} catch (FileNotFoundException e) {
			System.out.println("Init file does not exist.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save() {
		try {
			ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(new File(iniFile)));
			saver.writeObject(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Dimension getResolution() {
		return resolution;
	}

	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}

	public boolean isGui() {
		return gui;
	}

	public void setGui(boolean gui) {
		this.gui = gui;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

}
