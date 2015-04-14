package at.stefl.ptbridge.ptmp.multiuser;

import java.io.IOException;


public class MultiuserLinkMirrorAdapter implements MultiuserLinkListener {
	
	private MultiuserConnection connection;
	
	public MultiuserLinkMirrorAdapter(MultiuserConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public void linkAdded(int linkId, MultiuserLinkDefinition definition) {
		try {
			connection.addMultiuserLink(linkId, definition);
		} catch (IOException e) {}
	}
	
	@Override
	public void linkChanged(int linkId, MultiuserLinkDefinition definition) {
		try {
			connection.changeMultiuserLink(linkId, definition);
		} catch (IOException e) {}
	}
	
	@Override
	public void linkDetached(int linkId, MultiuserLinkDefinition definition) {
		try {
			connection.removeMultiuserLink(linkId);
		} catch (IOException e) {}
	}
	
}