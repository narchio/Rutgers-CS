'''
*** Authors: Nicolas Carchio and Oliver Gilan *** 
	
	Top-Level DNS server (TS)
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
import socket
import select
import sys

max_buffer = 256

# set up dictionary and read in from file
dns = {}
port = int(sys.argv[1])
dnsFile = "PROJI-DNSTS.txt"

with open(dnsFile) as f:
	for line in f:
	    tokens = line.lower().split()
	    host = tokens[0]
	    ip = tokens[1]
	    flag = tokens[2]
	    # assign the line to the dict
	    dns[host] = line.lower()

#  establish socket and start listening on the port
#  establish socket and start listening on the port
try:
    ts_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    ts_socket.bind(('', port))
    ts_socket.listen(1)
except socket.error as err:
    print('{} \n'.format("socket open error ",err))
print "TS server has been initialized at host: {}, and is listening for connections on port: {}".format(socket.gethostname(), port)

# listening loop
while (True):	
	tssockid,addr=ts_socket.accept()
	msg = tssockid.recv(256)
	if msg in dns:
		returnMessage = dns[msg]
		tssockid.send(returnMessage)
	else: 
		tssockid.send("{} - Error:HOST NOT FOUND\r\n".format(msg))


