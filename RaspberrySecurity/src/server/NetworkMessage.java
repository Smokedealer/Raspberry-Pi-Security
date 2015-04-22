package server;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class NetworkMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int EMPTY = 0;
	public static final int PICTURE = 1;
	public static final int COMMAND = 2;

	
	private int msgType;
	private String command;
	private ImageIcon img;
	
	
	public NetworkMessage(String command) {
		this(NetworkMessage.COMMAND, command, null);
	}
	
	public NetworkMessage(ImageIcon img) {
		this(NetworkMessage.PICTURE, null, img);
	}
	
	public NetworkMessage(int msgType, String command, ImageIcon img) {
		setMsgType(msgType);
		setCommand(command);
		setImg(img);
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public ImageIcon getImageIcon() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
	
	
}
