package security;

import java.awt.image.BufferedImage;
import java.util.Date;

public class SecurityEvent {
	private BufferedImage eventImage;
	private Date eventTime;
	
	public SecurityEvent(BufferedImage img, Date date) {
		eventImage = img;
		eventTime = date;
	}

	public BufferedImage getEventImage() {
		return eventImage;
	}

	public void setEventImage(BufferedImage eventImage) {
		this.eventImage = eventImage;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	
	
}
