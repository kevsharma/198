import threading
import time
import random
import socket

hardcodedPort = 50017

# This file handles the "HELLO" to "OLLEHHELLO" portion.
# See server.py and client.py for the final step in which we client sends data from a file into a server.

# I have included this here solely to show my work (how I got the recv and send stuff changed).
# Please note that the readme did not specify whether to build 4 off of the original proj0.py file or on top of 3.
# I chose to do both:
    # I built the HELLo to OLLEHHELLO stuff on top of proj0.py
    # I built the last file transfer part on top of 3, so that functionality is present in the server() and client() codes.


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

    # recv a message from a client
    data_from_client = csockid.recv(2048).decode('utf-8')
    print("[S]: Data received from client: {}".format(data_from_client))

    # format the message received and send it back
    msg = data_from_client[::-1] + data_from_client
    csockid.send(msg.encode('utf-8'))

    # Close the server socket
    ss.close()
    exit()


def client():
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

    # set up a message to send to the server (simple string).
    initial_msg = "HELLO"
    cs.send(initial_msg.encode('utf-8'))

    # wait for manipulated string back and print.
    msg_from_server = cs.recv(2048).decode('utf-8')
    print("[C]: Data received from server: {}".format(msg_from_server))

    # close the client socket
    cs.close()
    exit()

if __name__ == "__main__":
    t1 = threading.Thread(name='server', target=server)
    t1.start()

    time.sleep(random.random() * 5)

    t2 = threading.Thread(name='client', target=client)
    t2.start()

    time.sleep(2)

    print("Done.")
    exit()
