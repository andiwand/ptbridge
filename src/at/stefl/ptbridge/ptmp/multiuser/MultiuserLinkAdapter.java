package at.stefl.ptbridge.ptmp.multiuser;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import at.stefl.ptbridge.ptmp.multiuser.packet.MultiuserNetworkPacket;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;


public class MultiuserLinkAdapter {
	
	private final MultiuserConnection multiuserConnection;
	private final int linkId;
	
	private final Deque<MultiuserPDU> packetQueue = new LinkedList<MultiuserPDU>();
	
	private boolean closed;
	
	MultiuserLinkAdapter(MultiuserConnection multiuserConnection, int linkId) {
		this.multiuserConnection = multiuserConnection;
		this.linkId = linkId;
	}
	
	public MultiuserPDU receive() throws IOException {
		try {
			synchronized (packetQueue) {
				if (packetQueue.isEmpty()) packetQueue.wait();
				
				if (closed) throw new IOException("Link is already closed!");
				
				return packetQueue.poll();
			}
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	public void send(MultiuserPDU packet) throws IOException {
		if (closed) throw new IOException("Link is already closed!");
		
		MultiuserNetworkPacket networkPacket = new MultiuserNetworkPacket(
				linkId, packet);
		multiuserConnection.send(networkPacket);
	}
	
	void queueNetworkPacket(MultiuserNetworkPacket networkPacket) {
		synchronized (packetQueue) {
			if (closed) return;
			
			packetQueue.add(networkPacket.getPayload());
			
			if (packetQueue.size() == 1) packetQueue.notify();
		}
	}
	
	void connect() {
		closed = false;
	}
	
	void close() {
		closed = true;
		
		synchronized (packetQueue) {
			packetQueue.clear();
			packetQueue.notify();
		}
	}
	
}