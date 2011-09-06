package at.andiwand.packetsocket.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packetsocket.EthernetSocket;


public class TestEthernetSocket {
	
	public static void main(String[] args) {
		EthernetSocket socket = null;
		
		try {
			socket = new EthernetSocket(EthernetSocket.PROTOCOL_ALL);
			socket.bind("wlan0");
			
			MACAddress src = MACAddress.getByAddress("00:24:8c:fd:fe:96");
			Inet4Address srcIp = (Inet4Address) Inet4Address.getByName("192.168.1.1");
			MACAddress dst = MACAddress.BROADCAST_ADDRESS;
			Inet4Address dstIp = (Inet4Address) Inet4Address.getByName("192.168.1.254");
			
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream outputStream = new DataOutputStream(arrayOutputStream);
			
			outputStream.write(dst.getAddress());
			outputStream.write(src.getAddress());
			outputStream.writeShort(0x0806);
			
			outputStream.writeShort(1);
			outputStream.writeShort(0x0800);
			outputStream.write(6);
			outputStream.write(4);
			outputStream.writeShort(1);
			outputStream.write(src.getAddress());
			outputStream.write(srcIp.getAddress());
			outputStream.write(dst.getAddress());
			outputStream.write(dstIp.getAddress());
			
			socket.send(arrayOutputStream.toByteArray());
			
			byte[] buffer = new byte[1500];
			while (true) {
				socket.receive(buffer);
				
				System.out.println(MACAddress.getByAddress(buffer, 0));
				System.out.println(MACAddress.getByAddress(buffer, MACAddress.SIZE));
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Throwable t) {}
		}
	}
	
}