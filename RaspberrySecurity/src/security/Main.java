package security;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;



public class Main {
/*
	static {
	    Webcam.setDriver(new V4l4jDriver());
	}*/
	
	
	static int pocet = 0;
	
	public static void main(String[] args) throws IOException {
		
		Webcam webcam = Webcam.getDefault();
		
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
			System.out.println("Got the camera.");
		} else {
			System.out.println("No webcam detected");
			return;
		}
		
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		

		detectMotion(webcam);
		
		new GUI("Security Panel", webcam);
		
		
		//System.exit(0);
	}
	
	
	private static void detectMotion(final Webcam webcam){
		WebcamMotionDetector wmd = new WebcamMotionDetector(webcam);
		
		wmd.setInterval(100);
		
		wmd.addMotionListener(new WebcamMotionListener() {
			
			@Override
			public void motionDetected(WebcamMotionEvent arg0) {
				pocet++;
				System.out.println("Motion detected!! " + pocet);
			}
		});
		
		wmd.start();
	}
	


}
