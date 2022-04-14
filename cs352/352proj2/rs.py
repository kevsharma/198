import socket
import sys
from pprint import pprint

rsListenPort = None # configured when passed to argument
dns_records_dict = None # here we store the a dictionary of hostnames mapped to 2-tuples of the form (ip, A/NS)
dns_records_default = None # here we store the TS name server hostname
recordsfile = "PROJI-DNSRS.txt"

buffSize = 1

def dns_records():
    # dictionary: key = hostname; value = (IP, Type)
    with open(recordsfile) as file:
        records = file.readlines()

    # records = a list of [hostname, ip, type record] lists
    records = [record.strip().split() for record in records if record.strip()]
    # turn the records into a dictionary for faster lookups by hostname
    # records[0].lower() allows for case insensitive lookups.
    return dict([(record[0].lower(), (record[1], record[2])) for record in records])


def dns_lookup(clientrequest):
    result = ""
    if clientrequest in dns_records_dict:
        val = dns_records_dict[clientrequest]
        result = f"{clientrequest} {val[0]} {val[1]}"
    else:
        result = dns_records_default + " - NS"
    return result


# termination condition for main loop -> client closes connectin
if __name__ == "__main__":
    argv = sys.argv
    argc = len(argv)

    # Defensive checks against formatting.
    if argc != 3:
        print("Please specify rsportnumber and tshostname, in the form: python rs.py <rsListenPort: integer> <TSHostname: string>")
        exit(0)
    else:
        try:
            dns_records_default = argv[2]
            rsListenPort = int(argv[1])
        except:
            print("Improperly configured port number.")
            print("Please specify rsportnumber and tshostname, in the form: python rs.py <rsListenPort::integer> <TSHostname::string>")
            exit(0)

    # Read the records
    dns_records_dict = dns_records()

    # TSServerhostname : ('-', 'NS') is removed from dns_records_dict and put in its own reference variable
    tempNS = [k for k, v in dns_records_dict.items() if v[1] == 'NS']
    if len(tempNS) > 0:
        # remove the localhost - NS record if specified in PROJI-DNSRS.txt
        del dns_records_dict[tempNS[0]]

    # Initialization set up complete, now server should listen and service requests.
    # Create the socket.
    ss = None
    try:
        ss = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    except socket.error as err:
        print('socket open error: ', err)

    server_binding = ('', rsListenPort)
    ss.bind(server_binding)
    ss.listen(1)

    localhost_ip = (socket.gethostbyname(socket.gethostname()))
    csockid, addr = ss.accept() # addr is the address of client
    # csockid is the connection socket to service this client

    # recv a message from a client until they close the connection
    data_from_client = ""
    payloadLength = 0

    # receive all data and stop receiving message after payload length amount of bits read.
    while True:
        # It is not possible to send a zero-length message via SOCK_STREAM
        # Hence it is basically a test for EOF
        more = csockid.recv(buffSize).decode('utf-8')
        if not more: # This implies that the client has closed its side of the connection
            break

        data_from_client += more
        index_of_first_newline = data_from_client.find('\n')
        if index_of_first_newline != -1 and payloadLength == 0: # set the payload for the last time.
            payloadLength = int(data_from_client[0:index_of_first_newline]) # everything before newline - ###
            data_from_client = data_from_client[(index_of_first_newline+1):] # everything after

        if payloadLength != 0 and len(data_from_client) >= payloadLength:
            # Process the data we have received according to our protocol <- we got all our specified chars
            totalmsg = dns_lookup(data_from_client)
            totalmsg = str(len(totalmsg)) + "\n" + totalmsg
            # Send the lookup value back.
            csockid.send(totalmsg.encode('utf-8'))
            # Ensure that we reset the protocol enforcing values for the next query client may send
            data_from_client = ""
            payloadLength = 0

    # Close the server side sockets and connections
    ss.close()
    exit()
