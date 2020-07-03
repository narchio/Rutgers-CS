'''
*** Authors: Nicolas Carchio and Oliver Gilan *** 
	
	Client-Level Script
	1. reads in hostnames from a file 
		- Loops line by line
	2. Sends hostname as a string to RS
        - Checks response flag. If
            - A: write to file
            - NS: send hostname to TS
        - Prints error if no response
	3. Write response to file
'''
import sys
import socket
import select

rsHost = sys.argv[1]
rsPort = int(sys.argv[2])
tsPort = int(sys.argv[3])
fileName = "PROJI-HNS.txt"

buffer = 256

try:
    cs=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    cs.connect((rsHost, rsPort))
    # print("[C]: Client socket created")
except socket.error as err:
    print('{} \n'.format("socket open error ",err))

# c_socket.settimeout(1.0)

with open(fileName) as infile:
    with open("RESOLVED.txt", "w+") as outfile:
        for line in infile:
            sanitized = line.lower().strip("\r\n")
            cs.send(sanitized)
            response = cs.recv(256)
            tokens = response.split()

            if tokens[2] == "a":
                outfile.write(response)
            else:
                try:
                    cst = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    cst.connect((tokens[0], tsPort))
                except socket.error as er:
                    print('{} \n'.format("socket open error ",er))
                cst.send(sanitized)
                response = cst.recv(256)
                if response != '':
                    outfile.write(response)
                cst.close()
cs.close()
