package security;

import java.awt.image.BufferedImage;
import java.util.Date;

/**Holds date of event and picture of the event
 * 
 * @author Matej Kares, karesm@students.zcu.cz
 *
 */
public class SecurityEvent {
	private BufferedImage eventImage;
	private Date eventTime;
	
	/**Constructor
	 * 
	 * @param img - of the event
	 * @param date - of the event
	 */
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
