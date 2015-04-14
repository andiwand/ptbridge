package at.stefl.ptbridge.test;

import at.stefl.ptbridge.ptmp.multiuser.MultiuserInterfaceType;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkDefinition;
import at.stefl.ptbridge.ptmp.multiuser.MultiuserLinkType;


public class MultiuserLinkDefinitionTest {
	
	public static void main(String[] args) {
		MultiuserLinkDefinition definition = new MultiuserLinkDefinition(
				"Ethernet Bridge Interface");
		definition.setType(MultiuserLinkType.STRAIGHT_THROUGH);
		definition.setBandwidth(10000);
		definition.setFullDuplex(true);
		definition.setAutoBandwidth(true);
		definition.setAutoDuplex(true);
		definition
				.setInterfaceType(MultiuserInterfaceType.COPPER_FAST_ETHERNET);
		definition.setInterfaceCrossing(false);
		definition.setInterfaceUp(true);
		definition.setDeviceUp(true);
	}
	
}