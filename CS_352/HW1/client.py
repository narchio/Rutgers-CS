import threading
import time
import random
import socket as mysoc

#client task
def client():
    try:
        cs=mysoc.socket(mysoc.AF_INET, mysoc.SOCK_STREAM)
        print("[C]: Client socket created")
    except mysoc.error as err:
        print('{} \n'.format("socket open error ",err))

    # Define the port on which you want to connect to the server
    port = 50007
    sa_sameas_myaddr =mysoc.gethostbyname(mysoc.gethostname())
    # connect to the server on local machine
    server_binding=(sa_sameas_myaddr,port)
    cs.connect(server_binding)
    
    # # recieve server message
    # data_from_server=cs.recv(100)
    # # receive data from the server
    # print("[C]: Data received from server::  ",data_from_server.decode('utf-8'))
    print("[C]: Please hit ENTER to continue")

    # get user input, list comp to strip each line of the newline and grab word 
    filename = "HW1test.txt"
    with open(filename) as fd: 
    	file_contents = [current_line.rstrip() for current_line in fd]

    # loop through list of words of the file, send/recieve one at a time (packet style)
    server_responses = []
    for word in file_contents:
    	print("[C]: Sending this word to server::  ", word)
    	cs.send(word.encode('utf-8'))
    	# get response and print
    	data_from_server2 = cs.recv(100000)
    	server_response = data_from_server2.decode('utf-8')
    	server_responses.append(server_response)
    	print("[C]: Data received from server::  ", data_from_server2.decode('utf-8'))
    
    # lets the server know it is done reading and can break the loop
    word = "...ending connection..."
    cs.send(word.encode('utf-8'))

    # write the output string into the file
    with open(str("HW1out.txt"), 'w') as f: 
    	for curr_line in server_responses: 	
    		f.write(curr_line + '\n')

    # close the cclient socket
    cs.close()
    exit()

# start the client thread (basically is functioning as a main method)
t2 = threading.Thread(name='client', target=client)
t2.start()

input("")

exit()
