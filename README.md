# ptbridge

This tool makes it possible to bridge traffic between virtual packet tracer networks and
real networks. Therefore it translates packets and keeps states for the protocol translation.
Sadly packet tracer packets and protocols are different to real ones which makes it difficult
to translate them and inflexible. So there is only a handful of protocols implemented and
these could be unstable.

It is not possible to pass generic data through pt networks, like usually over ip, tcp or udp.

## Supported protocols

* Ethernet
* IP
* UDP
* TCP
* ARP
* ICMP
* DHCP (broken)
* Telnet

## Usage

To use this tool you need to setup a linux vm on your host os. For this I used VirtualBox and
Ubuntu. Then you will need a bridged interface in the vm to the network you want ptbridge to
be connected. In the vm ptbridge needs to be started as root due to low level network access.

Then you need to setup packet tracer for multiuser connection. There are alot tutorials and
videos about this on the internet. The packet tracer instance need to be in server mode.
Try the connection with a second packet tracer instance. After that, you can connect the
ptbridge to packer tracer.

The usage of this tool should be self explanatory:
`./ptbridge.jar <interface> <pt_ip>:<pt_port> <password>`
