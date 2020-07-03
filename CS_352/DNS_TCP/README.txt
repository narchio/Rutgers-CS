Oliver Gilan (olg7) and Nic Carchio (nfc28)

I: Client 
    The client functionality is simple. We get all the command-line arguments and then initialize a TCP socket. We then open the input file and the output file and for every line of the input file, we sanitize and send the line to the rs server. We then recieve the returned message from the rs server, which is then tokenized and the flag is checked. If the flag is 'a' it writes the message to the output file. If the flag is 'ns' it then takes the first token and resends the input line to the address given by the rs server with the port given from the command line. It then waits again to receive a response and if one is received, it writes it to the output file otherwise it prints an error to console.

II: Issues
    There are no known issues for our files. They all work as intended.

III: Problems
    Python indentation errors randomly. 
    We were getting errors when trying to encode and decode our messages so we took that out and it works.

IV: Learning Experience
    We build upon our knowledge of TCP's from the first homework and now understand how to communicate recursively through          multiple TCP connections. The recursive structure here, while very basic, begins with the root server. When the client queries the root server it either returns the intended response or cannot, if it cannot then it must go to the next level server, ts (top level server). The ts will then send the proper message to the client and if it cannot, will send a failure message to the client. TCP is versitile and reliable and through this, we can ensure that DNS messages will deliver safely as opposed to UDP which is more difficult to error check. 
