import threading
import time
import random

import socket


# I hope to impress by designing my program such that the server takes in one big string
# The server does not populate the output textfile as many clients would demand one server to do which isn't polite.

# Secondly, you can play around with my buffSize and my hardcodedPort and the program will still work! :)

# Lessosn that I learned the hard way:
    # You need to send the payload size of the message first in order
    # Because the recv() is blocking and only gives 0 when send() side socket.close() is called.
    # Otherwise you can't send 0 bytes like cs.send("".encode('utf-8')) and expect 0 bytes to be received

    # ^ The above for a stream of bytes transfer.

# The approach I used was indicate how long my message is going to me. So that recv can terminate promptly.
# I tested my code with special characters like: aﬃrmative the ff. This will break and I chose not to fix.

hardcodedPort = 50007
buffSize = 2048
inputfile = "../in-proj0.txt"
outputfile = "../out-proj0.txt"


def server():
    ss = None
    try:
        ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print("[S]: Server socket created")
    except socket.error as err:
        print('socket open error: {}\n'.format(err))
        exit()

    server_binding = ('', hardcodedPort)
    ss.bind(server_binding)
    ss.listen(1)
    host = socket.gethostname()
    print("[S]: Server host name is {}".format(host))
    localhost_ip = (socket.gethostbyname(host))
    print("[S]: Server IP address is {}".format(localhost_ip))
    csockid, addr = ss.accept()
    print("[S]: Got a connection request from a client at {}".format(addr))

    # recv a message from a client until they close the connection
    data_from_client = ""
    payloadLength = 0

    # receive all data and stop receiving message after payload length amount of bits read.
    while True:
        more = csockid.recv(buffSize).decode('utf-8')
        data_from_client += more
        if data_from_client.find('\n') != -1 and payloadLength == 0: # set the payload for the last time.
            payloadLength = int(data_from_client[0:data_from_client.find('\n')])
            data_from_client = data_from_client[data_from_client.find('\n')+1:]

        if payloadLength != 0 and len(data_from_client) >= payloadLength:
            break

    print("[S]: Got all strings from Client that were in input file")
    totalmsg = reverseStrings(data_from_client)
    print("[S]: Sending all updated strings to Client for Client to populate the output file with.")
    csockid.send(totalmsg.encode('utf-8'))

    # Close the server side sockets and connections
    ss.close()
    exit()


def client(inputFile, outputFile):
    cs = None
    try:
        cs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print("[C]: Client socket created")
    except socket.error as err:
        print('socket open error: {} \n'.format(err))
        exit()

    # Define the port on which you want to connect to the server
    port = hardcodedPort
    localhost_addr = socket.gethostbyname(socket.gethostname())

    # connect to the server on local machine
    server_binding = (localhost_addr, port)
    cs.connect(server_binding)

    # open the file and send all the strings.

    initialmsg = ""
    with open(inputFile, 'r') as f:
        for line in f.readlines():
            # send all the lines
            initialmsg += line

    initialmsgLength = len(initialmsg)
    initialmsg = str(initialmsgLength) + '\n' + initialmsg #payload will denote how long the msg we are sending is

    print("[C]: Sending all initial strings read from input file to Server for processing.")
    cs.send(initialmsg.encode('utf-8'))

    # now wait to receive everything from server.
    data_from_server = ""
    more = "-"  # placeholder to make sure len(more) is 1.

    while True:
        more = cs.recv(buffSize).decode('utf-8')
        if not more:
            break
        data_from_server += more # got Errno 104 : Connection reset by peer

    print("[C]: Received all updated strings from Server.")
    cs.close()

    # at this point we exited the while loop because server closed its side.
    with open(outputFile, 'w') as f:
        f.write(data_from_server)

    print("[C]: Wrote all updated strings to output file")
    exit()


def reverseStrings(str):
    strs = str.split('\n')
    manipulate = lambda s : s[::-1] + s + '\n'
    result = [manipulate(s) for s in strs]
    r = ""
    for item in result:
        r += item
    return r



if __name__ == "__main__":
    t1 = threading.Thread(name='server', target=server)
    t1.start()

    time.sleep(random.random() * 5)

    t2 = threading.Thread(name='client', target=client, args=(inputfile, outputfile))
    t2.start()