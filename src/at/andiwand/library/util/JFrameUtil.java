package at.andiwand.library.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class JFrameUtil {
	
	public static void centerFrame(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		
		int x = (screenSize.width - frameSize.width) >> 1;
		int y = (screenSize.height - frameSize.height) >> 1;
		
		frame.setLocation(x, y);
	}
	
	private JFrameUtil() {}
	
}