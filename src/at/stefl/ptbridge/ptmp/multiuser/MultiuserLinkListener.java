package at.stefl.ptbridge.ptmp.multiuser;

public interface MultiuserLinkListener {
	
	public void linkAdded(int linkId, MultiuserLinkDefinition definition);
	
	public void linkChanged(int linkId, MultiuserLinkDefinition definition);
	
	public void linkDetached(int linkId, MultiuserLinkDefinition definition);
	
}