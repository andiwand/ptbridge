package at.andiwand.packettracer.bridge.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import at.andiwand.packettracer.bridge.ptmp.PTMPAuthentication;
import at.andiwand.packettracer.bridge.ptmp.PTMPCompression;
import at.andiwand.packettracer.bridge.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncoding;
import at.andiwand.packettracer.bridge.ptmp.PTMPEncryption;
import at.andiwand.packettracer.bridge.ptmp.packet.PTMPEncodedPacket;
import at.andiwand.packettracer.bridge.ptmp.packet.PTMPPacketReader;
import at.andiwand.packettracer.bridge.ptmp.packet.PTMPPacketWriter;


public class TestPTMPReaderWriter {
	
	public static void main(String[] args) throws Throwable {
		PTMPConfiguration configuration = new PTMPConfiguration(
				PTMPEncoding.TEXT, PTMPEncryption.NONE, PTMPCompression.NO,
				PTMPAuthentication.CLEAR_TEXT, 0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PTMPPacketWriter packetWriter = new PTMPPacketWriter(outputStream,
				configuration);
		packetWriter.setEncryptionKey("asdf".getBytes());
		
		PTMPEncodedPacket packet = new PTMPEncodedPacket(1, "hallo welt\0"
				.getBytes(), configuration);
		System.out.println(packet);
		System.out.println(new String(packet.getBytes(configuration))
				.replaceAll("\0", "."));
		packetWriter.writePacket(packet);
		
		byte[] rawData = outputStream.toByteArray();
		System.out.println(new String(rawData).replaceAll("\0", "."));
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(rawData);
		PTMPPacketReader packetReader = new PTMPPacketReader(inputStream,
				configuration);
		packetReader.setDecryptionKey("asdf".getBytes());
		
		PTMPEncodedPacket reassamledPacket = packetReader.readPacket();
		System.out.println(reassamledPacket);
		System.out.println(new String(reassamledPacket.getBytes(configuration))
				.replaceAll("\0", "."));
	}
	
}