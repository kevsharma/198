import socket
import sys
import re

# global values

inputfile = 'PROJI-HNS.txt'
outputfile = 'RESOLVED.txt'

rsHostname = None
rsListenPort = None
tsListenPort = None
tsHostname = None

buffsize = 1


def fetch_hostname_queries(filename):
    with open(filename) as f:
        qs = f.readlines()

    # return qs and strip the leading+trailing spaces
    return [q.strip().lower() for q in qs]# if q.strip()]


def write_query_results(filename, query_results):
    with open(filename, 'w') as f:
        if query_results is None:
            f.write("")
        else:
            query_results = [(q+'\n') for q in query_results]
            query_results[-1] = query_results[-1][:-1] # last line shouldn't have newline
            [f.write(q) for q in query_results]

    return None


def query_dns_server(clientsocket, query_string):
    # Send the query to rs_server, prefixed by the msg length
    query_forthe_rs_server = str(len(query_string)) + '\n' + query_string  # protocol
    clientsocket.send(query_forthe_rs_server.encode("utf-8"))

    # Read the response, protocol prefixes the msg length
    # Our protocol prefixes msg by payload length + \n + paylaod
    # Thus we know after first \n encountered, what the payload length is
    q_result = ""  # from the rs_server for the query_forthe_res_server
    payloadLength = 0
    # receive all data and stop receiving message after payload length amount of bits read
    while True:
        more = clientsocket.recv(buffsize).decode("utf-8")
        q_result += more
        if q_result.find('\n') != -1 and payloadLength == 0:
            payloadLength = int(q_result[0:q_result.find('\n')])
            q_result = q_result[(q_result.find('\n') + 1):]

        # break condition, so we don't get stuck in recv loop
        if payloadLength != 0 and len(q_result) >= payloadLength:  # if it reaches payload limit
            break  # out of the infinite loop

    return q_result


# Recursive DNS Client
# Will query Hostname, if NS -> creates TCP connection to TS server
# Queries that TS server for said query before querying Hostname for any more queries.
if __name__ == "__main__":
    argv = sys.argv
    argc = len(argv)

    if argc < 4:
        print("Inadequate number of arguments")
        exit()
    else:
        try:
            rsHostname = argv[1]
            rsListenPort = int(argv[2])
            tsListenPort = int(argv[3])
        except:
            print("Please properly format the rsListenPort and tsListenPort")

    # Get the queries to process
    queries = fetch_hostname_queries(inputfile)
    if len(queries) == 0:
        print("No queries to process. Completed.")
        write_query_results(outputfile, None)
        exit(0)

    processed_queries = []
    # Creates client connection to rs
    # Creates client connection to ts if RS miss
    # Closes client connections
    cs_rs = None
    try:
        cs_rs = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    except socket.error as err:
        print(err)
        exit(0)

    rs_server_binding = (socket.gethostbyname(rsHostname), rsListenPort)
    cs_rs.connect(rs_server_binding)

    cs_ts = None  # Set up only on cache miss.

    # Query each time.
    for dnsquery in queries:
        # for any lines that
        if dnsquery == "":
            processed_queries.append("")
            continue

        dnsquery_result = query_dns_server(cs_rs, dnsquery)

        if dnsquery_result.endswith(" - NS"):
            if cs_ts is None:  # If this is the first query miss from RS server
                try:
                    cs_ts = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                except socket.error as err:
                    print(err)
                    exit(0)

                tsHostname = dnsquery_result[0: dnsquery_result.find(" - NS")]

                # Connect to the TS server.
                ts_server_binding = (socket.gethostbyname(tsHostname), tsListenPort)
                cs_ts.connect(ts_server_binding)

            # Assuming connected socket cs_ts
            dnsquery_result = query_dns_server(cs_ts, dnsquery)

        processed_queries.append(dnsquery_result)

    if cs_ts is not None:
        cs_ts.close()
    cs_rs.close()

    # With the processed_queries, dump into RESOLVED.txt
    write_query_results(outputfile, processed_queries)
