package server;

import java.io.Serializable;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * This class makes objects that can be sent through the network. Every object holds message type, image, command and date
 * (at least those that had been provided)  
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class NetworkMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int EMPTY = 0;
	public static final int PICTURE = 1;
	public static final int COMMAND = 2;

	
	private int msgType;
	private String command;
	private ImageIcon img;
	private Date date;
	
	
	public NetworkMessage(String command) {
		this(NetworkMessage.COMMAND, command, null, null);
	}
	
	public NetworkMessage(ImageIcon img) {
		this(NetworkMessage.PICTURE, null, img, new Date());
		
	}
	
	/**Constructor
	 * 
	 * @param msgType
	 * @param command
	 * @param img
	 * @param date
	 */
	public NetworkMessage(int msgType, String command, ImageIcon img, Date date) {
		setMsgType(msgType);
		setCommand(command);
		setImg(img);
		setDate(date);
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
