import threading
import time
import random
import socket as mysoc

# server task
def server():
    try:
        ss=mysoc.socket(mysoc.AF_INET, mysoc.SOCK_STREAM)
        print("[S]: Server socket created")
    except mysoc.error as err:
        print('{} \n'.format("socket open error ",err))
    server_binding=('',50007)
    ss.bind(server_binding)
    ss.listen(1)
    host=mysoc.gethostname()
    print("[S]: Server host name is: ",host)
    localhost_ip=(mysoc.gethostbyname(host))
    print("[S]: Server IP address is  ",localhost_ip)
    csockid,addr=ss.accept()
    print ("[S]: Got a connection request from a client at", addr)
    
    # # send a intro  message to the client.
    # msg="Welcome to CS 352"
    # csockid.send(msg.encode('utf-8'))

    final_msg = ""
    while True: 
        # recieve message from client
        data_from_client = csockid.recv(100000)
        if data_from_client: 
            recieved_msg = data_from_client.decode('utf-8')
            if recieved_msg == "...ending connection...": 
                break
            # now convert to ascii and send back over
            return_msg = ""
            for i in recieved_msg: 
                curr_char = ord(i)
                return_msg = return_msg + str(curr_char) + "_"
            final_msg = return_msg[0:-1]
            # send message back to client 
            csockid.send(final_msg.encode('utf-8'))


    # Close the server socket
    ss.close()
    exit()

# start the client thread (basically is functioning as a main method)
t1 = threading.Thread(name='server', target=server)
t1.start()

#input("Hit ENTER  to exit")

exit()
