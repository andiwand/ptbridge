package at.stefl.ptbridge.traverser;

import java.io.IOException;

import at.stefl.packetsocket.EthernetSocket;
import at.stefl.packetsocket.EthernetSocketException;
import at.stefl.packetsocket.pdu.Ethernet2Frame;
import at.stefl.packetsocket.pdu.formatter.Ethernet2FrameFormatter;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkAdapter;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.stefl.ptbridge.traverser.translator.Ethernet2Translator;


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
						// TODO: remove
						if (e.getMessage() != null
								&& e.getMessage()
										.contains("Unregistered type!"))
							continue;
						if ("Unsupported port!".equals(e.getMessage()))
							continue;
						if ("Unsupported option!".equals(e.getMessage()))
							continue;
						if ("Unregistered protocol".equals(e.getMessage()))
							continue;
						if ("Unknown option!".equals(e.getMessage()))
							continue;
						
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
//						e.printStackTrace();
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