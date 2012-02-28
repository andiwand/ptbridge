package at.andiwand.packettracer.bridge.traverser.translator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import at.andiwand.packetsocket.pdu.PDU;
import at.andiwand.packetsocket.pdu.TCPSegment;
import at.andiwand.packetsocket.pdu.TelnetSegment;
import at.andiwand.packetsocket.pdu.formatter.PDUFormatterFactory;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserPDU;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTCPSegment;
import at.andiwand.packettracer.bridge.ptmp.multiuser.pdu.MultiuserTelnetSegment;


public class TCPTranslator extends
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
			
			return Arrays.equals(toPortArray(), connection.toPortArray());
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(toPortArray());
		}
		
		private int[] toPortArray() {
			switch (direction) {
			case NETWORK:
				return new int[] {sourcePort, destinationPort};
			case PACKET_TRACER:
				return new int[] {destinationPort, sourcePort};
			default:
				throw new IllegalStateException("Unreachable section!");
			}
		}
	}
	
	// TODO: implement dictionary
	// TODO: auto clean
	private static class SequenceTranslator {
		private final Map<Long, Long> networkMap = new HashMap<Long, Long>();
		private final Map<Long, Long> packetTracerMap = new HashMap<Long, Long>();
		
		@Override
		public String toString() {
			return networkMap.toString();
		}
		
		private void putTranslation(long networkSequence,
				long packetTracerSequence) {
			networkMap.put(networkSequence, packetTracerSequence);
			packetTracerMap.put(packetTracerSequence, networkSequence);
		}
		
		public long toMultiuser(long number, PDU networkPayload,
				MultiuserPDU multiuserPayload) {
			Long resultInteger = networkMap.get(number);
			long result;
			
			if (resultInteger == null) {
				result = number;
				putTranslation(result, result);
			} else {
				result = resultInteger;
			}
			
			int networkPayloadSize;
			int packetTracerPayloadSize;
			
			if (networkPayload == null) {
				networkPayloadSize = 1;
				packetTracerPayloadSize = 1;
			} else {
				networkPayloadSize = PDUFormatterFactory.FACTORY
						.getFormatterInstance(networkPayload.getClass())
						.format(networkPayload).length;
				packetTracerPayloadSize = multiuserPayload
						.getBytes(PTMPEncoding.BINARY).length;
			}
			
			putTranslation(number + networkPayloadSize, result
					+ packetTracerPayloadSize);
			
			return result;
		}
		
		public long toNetwork(long number, PDU networkPayload,
				MultiuserPDU multiuserPayload) {
			Long resultInteger = packetTracerMap.get(number);
			long result;
			
			if (resultInteger == null) {
				result = number;
				putTranslation(result, result);
			} else {
				result = resultInteger;
			}
			
			int networkPayloadSize;
			int packetTracerPayloadSize;
			
			if (multiuserPayload == null) {
				networkPayloadSize = 1;
				packetTracerPayloadSize = 1;
			} else {
				networkPayloadSize = PDUFormatterFactory.FACTORY
						.getFormatterInstance(networkPayload.getClass())
						.format(networkPayload).length;
				packetTracerPayloadSize = multiuserPayload
						.getBytes(PTMPEncoding.BINARY).length;
			}
			
			putTranslation(result + networkPayloadSize, number
					+ packetTracerPayloadSize);
			
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
		
		result.setSourcePort(segment.getSourcePort());
		result.setDestinationPort(segment.getDestinationPort());
		result.setDataOffset((byte) 0);
		result.setReserved((byte) 0);
		result.setFlags(segment.getFlags());
		result.setWindow(0);
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
		
		result.setSequenceNumber(sequenceTranslator
				.toNetwork(segment.getSequenceNumber(), result.getPayload(),
						segment.getPayload()));
		result.setAcknowledgmentNumber(sequenceTranslator.toNetwork(segment
				.getAcknowledgmentNumber(), result.getPayload(), segment
				.getPayload()));
		
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
		
		result.setSourcePort((short) segment.getSourcePort());
		result.setDestinationPort((short) segment.getDestinationPort());
		result.setUnknown2(0);
		result.setUnknown3((byte) 0);
		result.setUnknown4((byte) 0);
		result.setFlags(segment.getFlags());
		result.setUnknown5((short) 0);
		result.setUnknown6((short) 0);
		result.setUnknown7((short) 0);
		result.setUnknown8(0);
		result.setUnknown9(0);
		
		ConnectionSlot connectionSlot = new ConnectionSlot(segment
				.getSourcePort(), segment.getDestinationPort(),
				Direction.NETWORK);
		SequenceTranslator sequenceTranslator = sequenceTranslatorMap
				.get(connectionSlot);
		
		if (sequenceTranslator == null) {
			sequenceTranslator = new SequenceTranslator();
			sequenceTranslatorMap.put(connectionSlot, sequenceTranslator);
		}
		
		result.setSequenceNumber((int) sequenceTranslator
				.toMultiuser(segment.getSequenceNumber(), segment.getPayload(),
						result.getPayload()));
		result.setAcknowledgmentNumber((int) sequenceTranslator.toMultiuser(
				segment.getAcknowledgmentNumber(), segment.getPayload(), result
						.getPayload()));
		
		return result;
	}
}