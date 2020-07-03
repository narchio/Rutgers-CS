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
dnsFile = "PROJI-DNSRS.txt"

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
try:
    rs_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    rs_socket.bind(('', port))
    rs_socket.listen(1)
except socket.error as err:
    print('{} \n'.format("socket open error ",err))
print "RS server has been initialized at host: {}, and is listening for connections on port: {}".format(socket.gethostname(), port)
rssockid,addr=rs_socket.accept()

while True:
    try: 
        msg = rssockid.recv(256)
        if msg in dns:
            returnMessage = dns[msg]
            rssockid.send(returnMessage)
            # rssockid.close()
        else: 
            returnMessage = dns["ns"]
            rssockid.send(returnMessage)
            # rssockid.close()
    except socket.error as er:
        print "Client connection has been terminated"
        break


