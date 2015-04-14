package at.stefl.ptbridge.traverser.translator;

import java.util.HashMap;
import java.util.Map;

import at.stefl.commons.network.Assignments;
import at.stefl.packetsocket.pdu.PDU;
import at.stefl.packetsocket.pdu.TCPSegment;
import at.stefl.packetsocket.pdu.TelnetSegment;
import at.stefl.packetsocket.pdu.formatter.PDUFormatterFactory;
import at.stefl.ptbridge.ptmp.PTMPEncoding;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserTCPSegment;
import at.stefl.ptbridge.ptmp.multiuser.pdu.MultiuserTelnetSegment;


public class TCPTranslator2 extends
		GenericPDUTranslator<TCPSegment, MultiuserTCPSegment> {
	
	private static enum Direction {
		NETWORK,
		PACKET_TRACER;
	}
	
	private static class ConnectionSlot {
		private final int sourcePort;
		private final int destinationPort;
		private final Direction direction;
		
		public ConnectionSlot(int sourcePort, int destinationPort,
				Direction direction) {
			this.sourcePort = sourcePort;
			this.destinationPort = destinationPort;
			this.direction = direction;
		}
		
		@Override
		public String toString() {
			return "[(" + sourcePort + ", " + destinationPort + ") to "
					+ direction + "]";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj == this) return true;
			
			if (!(obj instanceof ConnectionSlot)) return false;
			ConnectionSlot connection = (ConnectionSlot) obj;
			
			return ((sourcePort == connection.sourcePort) && (destinationPort == connection.destinationPort))
					|| ((sourcePort == connection.destinationPort) && (destinationPort == connection.sourcePort));
		}
		
		@Override
		public int hashCode() {
			return sourcePort ^ destinationPort;
		}
	}
	
	// TODO: implement dictionary
	// TODO: auto clean
	private static class SequenceTranslator {
		private final Map<Long, Long> toMultiuserMap = new HashMap<Long, Long>();
		private final Map<Long, Long> toNetworkMap = new HashMap<Long, Long>();
		
		@Override
		public String toString() {
			return toMultiuserMap.toString();
		}
		
		private void putTranslation(long networkSequence, long multiuserSequence) {
			System.out.println("	put translation (nw <-> mu) "
					+ networkSequence + " <-> " + multiuserSequence);
			
			toMultiuserMap.put(networkSequence, multiuserSequence);
			toNetworkMap.put(multiuserSequence, networkSequence);
		}
		
		public long seqToMultiuser(TCPSegment segment,
				MultiuserPDU multiuserPayload) {
			long seq = segment.getSequenceNumber();
			
			if (segment.getFlags() == Assignments.TCP.FLAG_ACK)
				return toMultiuserMap.get(seq);
			
			Long result = toMultiuserMap.get(seq);
			
			if (result == null) {
				result = seq;
				putTranslation(seq, seq);
			}
			
			PDU networkPayload = segment.getPayload();
			int networkPayloadSize = 1;
			int multiuserPayloadSize = 1;
			
			if (networkPayload != null) {
				networkPayloadSize = PDUFormatterFactory.FACTORY
						.getFormatterInstance(networkPayload.getClass())
						.format(networkPayload).length;
				multiuserPayloadSize = "CTelnetPacket\0".length()
						+ multiuserPayload.getBytes(PTMPEncoding.BINARY).length;
			}
			
			System.out.println("size " + networkPayloadSize + " -> "
					+ multiuserPayloadSize);
			
			putTranslation(seq + networkPayloadSize, result
					+ multiuserPayloadSize);
			
			return result;
		}
		
		public long seqToNetwork(PDU networkPayload, MultiuserTCPSegment segment) {
			long seq = segment.getSequenceNumber();
			
			if (segment.getFlags() == Assignments.TCP.FLAG_ACK)
				return toNetworkMap.get(seq);
			
			Long result = toNetworkMap.get(seq);
			
			if (result == null) {
				result = seq;
				putTranslation(seq, seq);
			}
			
			MultiuserPDU multiuserPayload = segment.getPayload();
			int networkPayloadSize = 1;
			int multiuserPayloadSize = 1;
			
			if (networkPayload != null) {
				networkPayloadSize = PDUFormatterFactory.FACTORY
						.getFormatterInstance(networkPayload.getClass())
						.format(networkPayload).length;
				multiuserPayloadSize = "CTelnetPacket\0".length()
						+ multiuserPayload.getBytes(PTMPEncoding.BINARY).length;
			}
			
			System.out.println("size " + multiuserPayloadSize + " -> "
					+ networkPayloadSize);
			
			putTranslation(result + networkPayloadSize, seq
					+ multiuserPayloadSize);
			
			return result;
		}
		
		public long ackToMultiuser(long ack) {
			Long result = toMultiuserMap.get(ack);
			if (result == null) return 0;
			return result;
		}
		
		public long ackToNetwork(long ack) {
			Long result = toNetworkMap.get(ack);
			if (result == null) return 0;
			return result;
		}
		
	}
	
	private static final TranslationHelper TRANSLATION_ASSOCIATOR = new TranslationHelper() {
		{
			putTranslator(TelnetSegment.class, MultiuserTelnetSegment.class,
					TelnetTranslator.class);
		}
	};
	
	private final Map<ConnectionSlot, SequenceTranslator> sequenceTranslatorMap = new HashMap<ConnectionSlot, SequenceTranslator>();
	
	// TODO: complete
	@Override
	protected TCPSegment toNetworkGeneric(MultiuserTCPSegment segment) {
		TCPSegment result = new TCPSegment();
		
		// TODO: remove
		int minPort = Math.min(segment.getSourcePort(), segment
				.getDestinationPort());
		if (minPort != 23) throw new RuntimeException();
		
		result.setSourcePort(segment.getSourcePort());
		result.setDestinationPort(segment.getDestinationPort());
		result.setReserved((byte) 0);
		result.setFlags(segment.getFlags());
		result.setWindow(14600);
		result.setChecksum((short) 0);
		result.setUrgentPointer(0);
		
		if (segment.getPayload() != null) {
			Class<?> payloadClass = segment.getPayload().getClass();
			PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
					.getTranslator(payloadClass);
			PDU payload = payloadTranslator.toNetwork(segment.getPayload());
			result.setPayload(payload);
		} else {
			result.setPayload(null);
		}
		
		ConnectionSlot connectionSlot = new ConnectionSlot(segment
				.getSourcePort(), segment.getDestinationPort(),
				Direction.NETWORK);
		SequenceTranslator sequenceTranslator = sequenceTranslatorMap
				.get(connectionSlot);
		
		if (sequenceTranslator == null) {
			sequenceTranslator = new SequenceTranslator();
			sequenceTranslatorMap.put(connectionSlot, sequenceTranslator);
		}
		
		System.out.println("-- To Network TCP Translator --");
		
		result.setSequenceNumber(sequenceTranslator.seqToNetwork(result
				.getPayload(), segment));
		result.setAcknowledgmentNumber(sequenceTranslator.ackToNetwork(segment
				.getAcknowledgmentNumber()));
		
		System.out.println("seq " + segment.getSequenceNumber() + " -> "
				+ result.getSequenceNumber());
		System.out.println("ack " + segment.getAcknowledgmentNumber() + " -> "
				+ result.getAcknowledgmentNumber());
		System.out.println("flags " + segment.getFlags());
		
		return result;
	}
	
	// TODO: complete
	@Override
	protected MultiuserTCPSegment toMultiuserGeneric(TCPSegment segment) {
		MultiuserTCPSegment result = new MultiuserTCPSegment();
		
		if (segment.getPayload() != null) {
			Class<?> payloadClass = segment.getPayload().getClass();
			PDUTranslator payloadTranslator = TRANSLATION_ASSOCIATOR
					.getTranslator(payloadClass);
			MultiuserPDU payload = payloadTranslator.toMultiuser(segment
					.getPayload());
			result.setPayload(payload);
		} else {
			result.setPayload(null);
		}
		
		// TODO: remove
		int minPort = Math.min(segment.getSourcePort(), segment
				.getDestinationPort());
		if (minPort != 23) throw new RuntimeException();
		
		result.setSourcePort(segment.getSourcePort());
		result.setDestinationPort(segment.getDestinationPort());
		result.setUnknown1(0);
		result.setUnknown2((byte) 0);
		result.setUnknown4((byte) 15);
		result.setFlags(segment.getFlags());
		result.setUnknown5((short) -1);
		result.setUnknown6((short) 0);
		result.setUnknown7((short) 0);
		result.setUnknown8(0);
		result.setUnknown9(1);
		
		ConnectionSlot connectionSlot = new ConnectionSlot(segment
				.getSourcePort(), segment.getDestinationPort(),
				Direction.NETWORK);
		SequenceTranslator sequenceTranslator = sequenceTranslatorMap
				.get(connectionSlot);
		
		if (sequenceTranslator == null) {
			sequenceTranslator = new SequenceTranslator();
			sequenceTranslatorMap.put(connectionSlot, sequenceTranslator);
		}
		
		System.out.println("-- To Multiuser TCP Translator --");
		
		result.setSequenceNumber(sequenceTranslator.seqToMultiuser(segment,
				result.getPayload()));
		result.setAcknowledgmentNumber(sequenceTranslator
				.ackToMultiuser(segment.getAcknowledgmentNumber()));
		
		System.out.println("seq " + segment.getSequenceNumber() + " -> "
				+ result.getSequenceNumber());
		System.out.println("ack " + segment.getAcknowledgmentNumber() + " -> "
				+ result.getAcknowledgmentNumber());
		System.out.println("flags " + segment.getFlags());
		
		return result;
	}
}