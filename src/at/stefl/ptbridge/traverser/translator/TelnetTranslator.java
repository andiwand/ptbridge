package at.stefl.ptbridge.traverser.translator;

import java.nio.charset.Charset;

import at.stefl.packetsocket.io.ExtendedDataOutputStream;
import at.stefl.packetsocket.pdu.TelnetSegment;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserTelnetSegment;


public class TelnetTranslator extends
		GenericPDUTranslator<TelnetSegment, MultiuserTelnetSegment> {
	
	private static final Charset CHARSET = Charset.forName("us-ascii");
	
	private boolean firstDone;
	private boolean promptModeDone;
	
	@Override
	protected TelnetSegment toNetworkGeneric(MultiuserTelnetSegment segment) {
		TelnetSegment result = new TelnetSegment();
		
		ExtendedDataOutputStream dataOutputStream = new ExtendedDataOutputStream();
		
		if (!promptModeDone && (segment.getUnknown2() > 0)) {
			dataOutputStream.write(new byte[] {(byte) 0xff, (byte) 0xfc, 0x01});
			promptModeDone = true;
		} else if (!firstDone) {
			dataOutputStream.write(new byte[] {(byte) 0xff, (byte) 0xfb, 0x01});
			firstDone = true;
		}
		
		String message = segment.getMessage();
		message = message.replaceAll("\n", "\r\n");
		message = message
				.replaceAll("\b\b\b\b\b\b\b\b", "\b\b\b\b\b\b\b\b\b\b");
		String filteredMessage = "";
		
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			char r;
			
			if (c == '\b') {
				filteredMessage += "\b \b";
				continue;
			} else if ((c == '\r') || (c == '\n')) {
				r = c;
			} else if ((c >= 0x20) && (c <= 0x7e)) {
				r = c;
			} else {
				continue;
			}
			
			filteredMessage += r;
		}
		
		dataOutputStream.write(filteredMessage.getBytes(CHARSET));
		
		dataOutputStream.close();
		result.setData(dataOutputStream.getData());
		
		return result;
	}
	
	@Override
	protected MultiuserTelnetSegment toMultiuserGeneric(TelnetSegment segment) {
		MultiuserTelnetSegment result = new MultiuserTelnetSegment();
		
		byte[] data = segment.getData();
		int dataOffset = 0;
		
		while ((dataOffset < data.length)
				&& ((data[dataOffset] & 0xff) == 0xff))
			dataOffset += 3;
		
		String message = new String(data, dataOffset, data.length - dataOffset,
				CHARSET);
		String filteredMessage = "";
		
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			char r;
			
			if (c == '\r') {
				r = c;
			} else if (c == '\n') {
				r = '\r';
			} else if ((c >= 0x20) && (c <= 0x7e)) {
				r = c;
			} else if (c == 0x7f) {
				filteredMessage += "\b \b";
				continue;
			} else {
				continue;
			}
			
			filteredMessage += r;
		}
		
		result.setMessage(filteredMessage);
		
		result.setUnknown1(true);
		result.setUnknown2(0);
		
		return result;
	}
}