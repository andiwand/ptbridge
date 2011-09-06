package at.andiwand.packetsocket;

import java.io.IOException;


public class EthernetSocket {
	
	public static final int MAX_FRAME_SIZE = 1500;
	
	
	public static final int PROTOCOL_LOOP		= 0x0060;
	public static final int PROTOCOL_PUP		= 0x0200;
	public static final int PROTOCOL_PUPAT		= 0x0201;
	public static final int PROTOCOL_IP			= 0x0800;
	public static final int PROTOCOL_X25		= 0x0805;
	public static final int PROTOCOL_ARP		= 0x0806;
	public static final int PROTOCOL_BPQ		= 0x08FF;
	public static final int PROTOCOL_IEEEPUP	= 0x0a00;
	public static final int PROTOCOL_IEEEPUPAT	= 0x0a01;
	public static final int PROTOCOL_DEC		= 0x6000;
	public static final int PROTOCOL_DNA_DL		= 0x6001;
	public static final int PROTOCOL_DNA_RC		= 0x6002;
	public static final int PROTOCOL_DNA_RT		= 0x6003;
	public static final int PROTOCOL_LAT		= 0x6004;
	public static final int PROTOCOL_DIAG		= 0x6005;
	public static final int PROTOCOL_CUST		= 0x6006;
	public static final int PROTOCOL_SCA		= 0x6007;
	public static final int PROTOCOL_TEB		= 0x6558;
	public static final int PROTOCOL_RARP		= 0x8035;
	public static final int PROTOCOL_ATALK		= 0x809B;
	public static final int PROTOCOL_AARP		= 0x80F3;
	public static final int PROTOCOL_8021Q		= 0x8100;
	public static final int PROTOCOL_IPX		= 0x8137;
	public static final int PROTOCOL_IPV6		= 0x86DD;
	public static final int PROTOCOL_PAUSE		= 0x8808;
	public static final int PROTOCOL_SLOW		= 0x8809;
	public static final int PROTOCOL_WCCP		= 0x883E;
	public static final int PROTOCOL_PPP_DISC	= 0x8863;
	public static final int PROTOCOL_PPP_SES	= 0x8864;
	public static final int PROTOCOL_MPLS_UC	= 0x8847;
	public static final int PROTOCOL_MPLS_MC	= 0x8848;
	public static final int PROTOCOL_ATMMPOA	= 0x884c;
	public static final int PROTOCOL_LINK_CTL	= 0x886c;
	public static final int PROTOCOL_ATMFATE	= 0x8884;
	public static final int PROTOCOL_PAE		= 0x888E;
	public static final int PROTOCOL_AOE		= 0x88A2;
	public static final int PROTOCOL_TIPC		= 0x88CA;
	public static final int PROTOCOL_1588		= 0x88F7;
	public static final int PROTOCOL_FCOE		= 0x8906;
	public static final int PROTOCOL_FIP		= 0x8914;
	public static final int PROTOCOL_EDSA		= 0xDADA;
	
	public static final int PROTOCOL_802_3		= 0x0001;
	public static final int PROTOCOL_AX25		= 0x0002;
	public static final int PROTOCOL_ALL		= 0x0003;
	public static final int PROTOCOL_802_2		= 0x0004;
	public static final int PROTOCOL_SNAP		= 0x0005;
	public static final int PROTOCOL_DDCMP		= 0x0006;
	public static final int PROTOCOL_WAN_PPP	= 0x0007;
	public static final int PROTOCOL_PPP_MP		= 0x0008;
	public static final int PROTOCOL_LOCALTALK	= 0x0009;
	public static final int PROTOCOL_CAN		= 0x000C;
	public static final int PROTOCOL_PPPTALK	= 0x0010;
	public static final int PROTOCOL_TR_802_2	= 0x0011;
	public static final int PROTOCOL_MOBITEX	= 0x0015;
	public static final int PROTOCOL_CONTROL	= 0x0016;
	public static final int PROTOCOL_IRDA		= 0x0017;
	public static final int PROTOCOL_ECONET		= 0x0018;
	public static final int PROTOCOL_HDLC		= 0x0019;
	public static final int PROTOCOL_ARCNET		= 0x001A;
	public static final int PROTOCOL_DSA		= 0x001B;
	public static final int PROTOCOL_TRAILER	= 0x001C;
	public static final int PROTOCOL_PHONET		= 0x00F5;
	public static final int PROTOCOL_IEEE802154	= 0x00F6;
	public static final int PROTOCOL_CAIF		= 0x00F7;
	
	
	
	static {
		System.loadLibrary("PacketSocket");
	}
	
	
	
	private static native int openImpl(int protocol)
			throws EthernetSocketException;
	private static native void closeImpl(int socket)
			throws EthernetSocketException;
	
	private static native void bindImpl(int socket, String interfaceName)
			throws EthernetSocketException;
	
	
	private static native void enablePromiscModeImpl(int socket,
			String interfaceName) throws EthernetSocketException;
	
	
	private static native int receiveImpl(int socket, byte[] buffer,
			int offset, int length, int flags) throws EthernetSocketException;
	private static native int receiveFromImpl(int socket, String interfaceName,
			byte[] buffer, int offset, int length, int flags)
			throws EthernetSocketException;
	
	private static native int sendImpl(int socket, byte[] buffer, int offset,
			int length, int flags) throws EthernetSocketException;
	private static native int sendToImpl(int socket, String interfaceName,
			byte[] buffer, int offset, int length, int flags)
			throws EthernetSocketException;
	
	
	
	
	private int socket;
	
	private boolean closed;
	
	
	
	public EthernetSocket(int protocol) throws IOException {
		socket = openImpl(protocol);
	}
	
	
	
	public void close() throws EthernetSocketException {
		if (closed)
			return;
		closed = true;
		
		closeImpl(socket);
	}
	
	public void bind(String interfaceName) throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		bindImpl(socket, interfaceName);
	}
	
	
	public void enablePromiscMode(String interfaceName)
			throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		enablePromiscModeImpl(socket, interfaceName);
	}
	
	
	public int receive(byte[] buffer) throws EthernetSocketException {
		return receive(buffer, 0, buffer.length);
	}
	public int receive(byte[] buffer, int offset, int length)
			throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		return receiveImpl(socket, buffer, offset, length, 0);
	}
	public int receiveFrom(String interfaceName, byte[] buffer)
			throws EthernetSocketException {
		return receiveFrom(interfaceName, buffer, 0, buffer.length);
	}
	public int receiveFrom(String interfaceName, byte[] buffer, int offset,
			int length) throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		return receiveFromImpl(socket, interfaceName, buffer, offset, length, 0);
	}
	
	public int send(byte[] buffer) throws EthernetSocketException {
		return send(buffer, 0, buffer.length);
	}
	public int send(byte[] buffer, int offset, int length)
			throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		return sendImpl(socket, buffer, offset, length, 0);
	}
	public int sendTo(String interfaceName, byte[] buffer)
			throws EthernetSocketException {
		return sendTo(interfaceName, buffer, 0, buffer.length);
	}
	public int sendTo(String interfaceName, byte[] buffer, int offset,
			int length) throws EthernetSocketException {
		if (closed)
			throw new EthernetSocketException("Socket is already closed!");
		
		return sendToImpl(socket, interfaceName, buffer, offset, length, 0);
	}
	
}