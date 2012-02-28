package at.andiwand.packettracer.bridge.traverser.translator;

import java.nio.charset.Charset;

import at.andiwand.packetsocket.pdu.TelnetSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTelnetSegment;


public class TelnetTranslator extends
		GenericPDUTranslator<TelnetSegment, MultiuserTelnetSegment> {
	
	private static final Charset CHARSET = Charset.forName("utf-8");
	
	@Override
	protected TelnetSegment toNetworkGeneric(MultiuserTelnetSegment segment) {
		TelnetSegment result = new TelnetSegment();
		
		result.setData(segment.getMessage().getBytes(CHARSET));
		
		return result;
	}
	
	@Override
	protected MultiuserTelnetSegment toMultiuserGeneric(TelnetSegment segment) {
		MultiuserTelnetSegment result = new MultiuserTelnetSegment();
		
		result.setMessage(new String(segment.getData(), CHARSET));
		result.setUnknown1(true);
		result.setUnknown2(0);
		
		return result;
	}
	
}