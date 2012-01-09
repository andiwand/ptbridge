package at.andiwand.packettracer.test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import at.andiwand.packettracer.ptmp.PTMPConfiguration;
import at.andiwand.packettracer.ptmp.PTMPConnection;
import at.andiwand.packettracer.ptmp.PTMPState;
import at.andiwand.packettracer.ptmp.PTMPStateListener;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserConnection;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkDefinition;
import at.andiwand.packettracer.ptmp.multiuser.MultiuserLinkListener;
import at.andiwand.packettracer.ui.Gui;
import at.andiwand.packettracer.ui.Gui.OnInputValidatedCallback;

public class GuiTestMultiuserConnection {

	private static void showGui() {
		new Gui(new OnInputValidatedCallback() {

			@Override
			public void onInputValidated(String name, String host, int port,
					String password) {
				try {
					connect(Inet4Address.getByName(host), port, name, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void connect(InetAddress address, int port, String name, String password) throws IOException, InterruptedException {
		PTMPConnection ptmpConnection = new PTMPConnection();
		ptmpConnection.setPreferredConfiguration(new PTMPConfiguration(
				PTMPConfiguration.ENCODING_TEXT,
				PTMPConfiguration.ENCRYPTION_NONE,
				PTMPConfiguration.COMPRESSION_NO,
				PTMPConfiguration.AUTHENTICATION_CLEAR_TEXT));

		ptmpConnection.connect(address, port, name, password);
		System.out.println("ptmp connected");

		final MultiuserConnection multiuserConnection = new MultiuserConnection("*network name*");
		multiuserConnection.addLinkListener(new MultiuserLinkListener() {
			public void linkAdded(int linkId, MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.addMultiuserLink(linkId, definition);
				} catch (IOException e) {}
			}
			public void linkChanged(int linkId,
					MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.changeMultiuserLink(linkId, definition);
				} catch (IOException e) {}
			}
			public void linkDetached(int linkId,
					MultiuserLinkDefinition definition) {
				try {
					multiuserConnection.removeMultiuserLink(linkId);
				} catch (IOException e) {}
			}
		});
		multiuserConnection.connect(ptmpConnection);
		System.out.println("multiuser connected");

		final Object monitor = new Object();

		ptmpConnection.addStateListener(new PTMPStateListener() {
			public void stateChanged(PTMPState newState, PTMPState oldState) {
				if (newState != PTMPState.NOT_CONNECTED) return;

				synchronized (monitor) {
					monitor.notify();
				}
			}
		});

		synchronized (monitor) {
			monitor.wait();
		}

		multiuserConnection.close();
		System.out.println("multiuser closed");

		ptmpConnection.close();
		System.out.println("ptmp closed");
	}

	public static void main(String[] args) throws Throwable {
		showGui();
	}
}