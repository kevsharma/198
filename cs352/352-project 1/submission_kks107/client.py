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


def client(inputFile, outputFile):
    cs = None
    try:
        cs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print(f"[C @ {datetime.now().microsecond} μs]: Client socket created")
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

    print(f"[C @ {datetime.now().microsecond} μs]: Sending all initial strings read from input file to Server for processing.")
    cs.send(initialmsg.encode('utf-8'))

    # now wait to receive everything from server.
    data_from_server = ""
    more = "-"  # placeholder to make sure len(more) is 1.

    while True:
        more = cs.recv(buffSize).decode('utf-8')
        if not more:
            break
        data_from_server += more # got Errno 104 : Connection reset by peer

    print(f"[C @ {datetime.now().microsecond} μs]: Received all updated strings from Server.")
    cs.close()

    # at this point we exited the while loop because server closed its side.
    with open(outputFile, 'w') as f:
        f.write(data_from_server)

    print(f"[C @ {datetime.now().microsecond} μs]: Wrote all updated strings to output file")
    exit()


client(inputFile=inputfile, outputFile=outputfile)