package at.andiwand.packettracer.bridge.traverser;

import java.io.IOException;

import at.andiwand.packetsocket.EthernetSocket;
import at.andiwand.packetsocket.EthernetSocketException;
import at.andiwand.packetsocket.pdu.Ethernet2Frame;
import at.andiwand.packetsocket.pdu.formatter.Ethernet2FrameFormatter;
import at.andiwand.packettracer.bridge.ptmp.multiuser.MultiuserLinkAdapter;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.andiwand.packettracer.bridge.traverser.translator.Ethernet2Translator;


public class EthernetTraverser {
	
	private class EthernetReceiver extends Thread {
		public void run() {
			byte[] buffer = new byte[EthernetSocket.MAX_FRAME_SIZE];
			
			try {
				while (!isInterrupted()) {
					try {
						int size = ethernetSocket.receive(buffer);
						Ethernet2Frame frame = frameFormatter.parse(buffer, 0,
								size);
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
						MultiuserPDU multiuserFrame = multiuserLink.receive();
						
						if (!(multiuserFrame instanceof MultiuserEthernet2Frame))
							continue;
						
						Ethernet2Frame frame = translator
								.toNetwork(multiuserFrame);
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
	
	private final EthernetSocket ethernetSocket;
	private final MultiuserLinkAdapter multiuserLink;
	
	private final Ethernet2FrameFormatter frameFormatter = new Ethernet2FrameFormatter();
	
	private final Ethernet2Translator translator = new Ethernet2Translator();
	
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
		MultiuserEthernet2Frame multiuserFrame = translator.toMultiuser(frame);
		multiuserLink.send(multiuserFrame);
	}
	
	public void close() {
		ethernetReceiver.interrupt();
		multiuserLinkReceiver.interrupt();
		
		try {
			ethernetReceiver.join();
			multiuserLinkReceiver.join();
		} catch (InterruptedException e) {}
	}
	
}