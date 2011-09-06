package at.andiwand.packettracer.traverser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;

import at.andiwand.library.network.pdu.Ethernet2Frame;
import at.andiwand.library.network.pdu.formatter.Ethernet2FrameFormatter;
import at.andiwand.packetsocket.EthernetSocket;
import at.andiwand.packetsocket.EthernetSocketException;
import at.andiwand.packettracer.ptmp.PTMPDataInputStream;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkAdapter;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserEncodedPDU;
import at.andiwand.packettracer.ptmp.multiuser.pdu.formatter.MultiuserEthernet2FrameFormatter;


public class EthernetTraverser {
	
	private final EthernetSocket ethernetSocket;
	private final MultiuserLinkAdapter multiuserLink;
	
	private final Ethernet2FrameFormatter frameFormatter = new Ethernet2FrameFormatter();
	private final MultiuserEthernet2FrameFormatter multiuserFrameFormatter = new MultiuserEthernet2FrameFormatter();
	
	private final EthernetReceiver ethernetReceiver = new EthernetReceiver();
	private final MultiuserLinkReceiver multiuserLinkReceiver = new MultiuserLinkReceiver();
	
	
	public EthernetTraverser(EthernetSocket ethernetSocket,
			MultiuserLinkAdapter linkAdapter) {
		this.ethernetSocket = ethernetSocket;
		this.multiuserLink = linkAdapter;
		
		ethernetReceiver.start();
		multiuserLinkReceiver.start();
	}
	
	
	private void sendToEthernet(Ethernet2Frame frame)
			throws EthernetSocketException {
		byte[] bytes = frameFormatter.format(frame);
		ethernetSocket.send(bytes);
	}
	private void sendToMultiuserLink(Ethernet2Frame frame) throws IOException {
		byte[] bytes = multiuserFrameFormatter.format(frame);
		multiuserLink.send(bytes);
		
		// TODO remove
		System.out.println("send " + new String(bytes).replaceAll("\0", " | "));
		System.out.println("send " + new BigInteger(bytes).toString(16));
	}
	
	public void close() {
		ethernetReceiver.interrupt();
		multiuserLinkReceiver.interrupt();
		
		try {
			ethernetReceiver.join();
			multiuserLinkReceiver.join();
		} catch (InterruptedException e) {}
	}
	
	
	private class EthernetReceiver extends Thread {
		public void run() {
			byte[] buffer = new byte[EthernetSocket.MAX_FRAME_SIZE];
			
			try {
				while (!isInterrupted()) {
					try{
						int size = ethernetSocket.receive(buffer);
						Ethernet2Frame frame = frameFormatter
								.parse(buffer, 0, size);
						sendToMultiuserLink(frame);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				close();
			}
		}
	}
	private class MultiuserLinkReceiver extends Thread {
		public void run() {
			try {
				while (!isInterrupted()) {
					try {
						MultiuserEncodedPDU encodedPDU = multiuserLink
								.receive();
						// TODO remove
						System.out.println("rec  " + new String(encodedPDU.getBytes()).replaceAll("\0", " | "));
						System.out.println("rec  " + new BigInteger(encodedPDU.getBytes()).toString(16));
						ByteArrayInputStream inputStream = new ByteArrayInputStream(
								encodedPDU.getBytes());
						PTMPDataInputStream dataInputStream = new PTMPDataInputStream(
								inputStream, multiuserLink.encoding());
						
						String protocolName = dataInputStream.readString();
						if (!protocolName
								.equals(MultiuserEthernet2FrameFormatter.PROTOCOL_NAME))
							continue;
						
						Ethernet2Frame frame = multiuserFrameFormatter
								.parse(dataInputStream);
						sendToEthernet(frame);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				close();
			}
		}
	}
	
}