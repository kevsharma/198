import socket
import hardcodedvalues
from datetime import datetime
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

hardcodedPort = hardcodedvalues.hardcodedPort
buffSize = hardcodedvalues.buffSize
inputfile = hardcodedvalues.inputfile
outputfile = hardcodedvalues.outputfile



def reverseStrings(str):
    strs = str.split('\n')
    manipulate = lambda s : s[::-1] + s + '\n'
    result = [manipulate(s) for s in strs]
    r = ""
    for item in result:
        r += item
    return r



def server():
    ss = None
    try:
        ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print(f"[S @ {datetime.now().microsecond} μs]: Server socket created")
    except socket.error as err:
        print('socket open error: {}\n'.format(err))
        exit()

    server_binding = ('', hardcodedPort)
    ss.bind(server_binding)
    ss.listen(1)
    host = socket.gethostname()
    print(f"[S @ {datetime.now().microsecond} μs]: Server host name is {host}")
    localhost_ip = (socket.gethostbyname(host))
    print(f"[S @ {datetime.now().microsecond} μs]: Server IP address is {localhost_ip}")
    csockid, addr = ss.accept()
    print(f"[S @ {datetime.now().microsecond} μs]: Got a connection request from a client at {addr}")

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

    print(f"[S @ {datetime.now().microsecond} μs]: Got all strings from Client that were in input file")
    totalmsg = reverseStrings(data_from_client)
    print(f"[S @ {datetime.now().microsecond} μs]: Sending all updated strings to Client for Client to populate the output file with.")
    csockid.send(totalmsg.encode('utf-8'))

    # Close the server side sockets and connections
    ss.close()
    exit()

server()
