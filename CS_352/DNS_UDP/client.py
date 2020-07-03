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
import select

rsHost = sys.argv[1]
rsPort = int(sys.argv[2])
tsPort = int(sys.argv[3])
fileName = sys.argv[4]

buffer = 1024

c_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
c_socket.settimeout(0.5)

with open(fileName) as infile:
    with open("RESOLVED.txt", "w+") as outfile:
        for line in infile:
            sanitized = line.lower().strip("\r\n")
            c_socket.sendto(sanitized, (rsHost, rsPort))
            print "sent"
            try:
                msg, addr = c_socket.recvfrom(buffer)
                print 'rec'
                tokens = msg.split()
                print tokens
                if tokens[2] == "a":
                    outfile.write(msg)
                else:
                    c_socket.sendto(sanitized, (tokens[0], tsPort))

                    try:
                        msgTs, addrTs = c_socket.recvfrom(buffer)
                        outfile.write(msgTs)
                    except socket.timeout:
                        print "error: socket didn't receive any response"
                    except socket.error as er:
                        print er    
            except socket.timeout:
                print "error: socket didn't receive any response"
            except socket.error as er:
                print er

        
        
        
