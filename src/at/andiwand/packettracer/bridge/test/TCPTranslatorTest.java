package at.andiwand.packettracer.bridge.test;

import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTCPSegment;
import at.andiwand.packettracer.bridge.traverser.translator.TCPTranslator;


public class TCPTranslatorTest {
	
	public static void main(String[] args) {
		TCPTranslator tcpTranslator = new TCPTranslator();
		TCPSegment networkTCPSegment;
		MultiuserTCPSegment multiuserTCPSegment;
		
		int x = 0;
		int y = 0;
		
		networkTCPSegment = new TCPSegment();
		networkTCPSegment.setSequenceNumber(x);
		
		multiuserTCPSegment = tcpTranslator.toMultiuser(networkTCPSegment);
		System.out.println(multiuserTCPSegment.getSequenceNumber());
		System.out.println(multiuserTCPSegment.getAcknowledgmentNumber());
		System.out.println();
		
		multiuserTCPSegment = new MultiuserTCPSegment();
		multiuserTCPSegment.setSequenceNumber(y);
		multiuserTCPSegment.setAcknowledgmentNumber(++x);
		
		networkTCPSegment = tcpTranslator.toNetwork(multiuserTCPSegment);
		System.out.println(networkTCPSegment.getSequenceNumber());
		System.out.println(networkTCPSegment.getAcknowledgmentNumber());
		System.out.println();
		
		networkTCPSegment = new TCPSegment();
		networkTCPSegment.setSequenceNumber(x);
		networkTCPSegment.setAcknowledgmentNumber(++y);
		
		multiuserTCPSegment = tcpTranslator.toMultiuser(networkTCPSegment);
		System.out.println(multiuserTCPSegment.getSequenceNumber());
		System.out.println(multiuserTCPSegment.getAcknowledgmentNumber());
		System.out.println();
	}
	
}