'''
*** Authors: Nicolas Carchio and Oliver Gilan *** 
	
	Root-Level DNS server (RS)
	- maintains a DNS_table of three fields: 
		- Hostname
		- IP address
		- Flag (A or NS)
	1. reads in DNS records from a file 
		- dictionary to maintain the table 
	2. client sends queried hostname as a string to TS
	3. The TS program looks up the hostname in its DNS_table (dictionary) 
		3a. if there is a match, we sent the entry as a string
		3b. if there is no match, we send an error message: "Error:HOST NOT FOUND"
'''

import sys
import socket

port = int(sys.argv[1])
dnsFile = sys.argv[2]

dns = {}

with open(dnsFile) as f:
    for line in f:
        tokens = line.lower().strip().split()
        host = tokens[0]
        ip = tokens[1]
        flag = tokens[2]

        if flag == "ns":
            dns["ns"] = line.lower()
        else:
            dns[host] = line.lower()

#  establish socket and start listening on the port
rs_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
rs_socket.bind(('', port))
print "RS server has been initialized and is listening for connections on port: {}".format(port)

while True:
    msg, addr = rs_socket.recvfrom(1024)
    # decoded = msg.decode()
    print msg
    # print dns[decoded]
    if msg in dns:
        returnMessage = str.encode(dns[msg])
        rs_socket.sendto(returnMessage, addr)
    else: 
        returnMessage = str.encode(dns["ns"])
        rs_socket.sendto(returnMessage, addr)








