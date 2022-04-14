IMPORTANT: # Please see my hardcodedvalues.py stuff before launching client.py and server.py
    # I built the HELLO to OLLEHHELLO stuff on top of proj0.py
    # I built the last file transfer part on top of 3, so that functionality is present in the server() and client() codes.
        That is, I built the last part of prompt 4 on top of the separated server and client codes.
        It is trivial to do three, so I assumed that modifying the separated original programs is okay too and hence worked on top of it.

    See my answers for 1 and 2 below.
    # server.py and client.py will also print the microseconds at which they accomplished something.
    # but note that due to OS scheduling, these are only approximations (do not consider them for anything more than
    getting an idea of what happens in which order).
---------------------------------------------------------------------------------------------------------------------------

(1) Understand the functionality implemented in the program. First, download,
    save and execute the program as is in your environment. Make sure it executes
    successfully and according to how you would expect.
============================================================================================
    I watched the recitation video on it and I understand it.
    I have also read the python sockets docs and understand why send and recv work so easily here.
        - I played around with the buffer sizes too.
        By making it smaller than the message, I saw message loss (this is to be expected:
            because the code does not recv in a loop and its termination condition is very simple).

---------------------------------------------------------------------------------------------------------------------------

(2) Try running the program immediately again when it finishes
    successfully. What do you see? Why? What happens when you remove the various
    sleep()s in the program?
=============================================================================================
=============================================================================================

a. Between running the program, the program terminating, and launching the program immediately again,
I got 2 events.
    1) The program runs fine and produces the same output.
    2) There is an error in the client() and server() methods (let's explore the server method first):
        -> note that the sockets which have been created in client and server are TCP sockets (socket.SOCK_STREAM).
        -> Exploring the server() function first:
            -> We get an 'OSError: [Errno 98] Address already in use' for line ss.bind(server_binding)
            -> The address being referred to here is ('', 50007) where '' is the end system which runs proj0.py file.

            Moreover,
            -> The documentation for ss.close() makes a note of the following:
                -> "close() releases the resource associated with a connection but does not necessarily close the connection immediately."

            -> In CS214, we learned about the notion of reaping processes. In this process the server() and client() create two socket objects which claim
                an address pair (for the TCP connection).

            Accordingly,
            -> The reason we get this error on the second launch of the program:
                    -> the parent process has not reaped the resources in use by proj0.py process
                    -> by definition, the process table contains references to resources which are in use by proj0.py (since it hasn't been reaped).

            -> Though the socket.close() call releases the resources, the OS has not recycled them.

            Conclusion,
            -> The ss.bind(('', 50007)) statement on the immediate next run of the program will attempt to use an address that has not been recycled, i.e. is still in use.

        -> The client() gives error ConnectionRefusedError: [Errno 111] Connection refused.
            -> Note that ('', 50007) address of type (Destination IP : Destination Port) was previously used for a different TCP connection.
            -> When the previous run of the program called ss.close(), the resources (TCP variables, bufffer) associated with the connection were released.
            -> So my guess is that the client is attempting to initiate a TCP connection where the server end of the TCP connectino's address pair ('', 50007)
                is no longer listening for TCP connections.

    Case 1 happens when the OS reaps the resources claimed by proj0.py process in the previous run. But in Case 2 we initiate a new process looking to claim
    the same resources used a process whose resources have not yet been reclaimed by the OS (and hence are not open for the new project).


b. By removing the sleep in the middle of thread calls to server() and client(), statements in both of those two will be interleaved.
    - Previously, the server function would set up a listening socket object and be suspended waiting for a connection.
    - Now there is interleaving where server, client socket is created.
        - But server thread manages to establish a listening socket before my client attempts to connect to it.
            - If some interleaving led to the contrary, the client would get a ConnectionRefusedError Errno 111.
            - I can confirm this by adding a time.sleep(5) statement before ss.accept(). Or I could change the port number in server but not in client.

c. By removing the last sleep in main function:
    - I got output like this:
    [S]: Server socket created
    [S]: Server host name is Windrunner
    [S]: Server IP address is 127.0.1.1
    Done.
    [C]: Client socket created
    [S]: Got a connection request from a client at ('127.0.0.1', 52978)
    [C]: Data received from server: Welcome to CS 352!

    -> This was unexpected.
    -> However threads in python which are not marked with the daemon=True arg when creation will not terminate when main terminates.
    -> That is, the process in which proj0.py is executing will wait for client() and server() threads to finish even if the main function thread has finished.
    -> Note: daemon threads shut down when main would call exit(0). Here daemon=False, so the client() doesn't terminate (not joined).

d. I wanted to explore more:
        [S]: Server socket created
        [S]: Server host name is Windrunner
        [S]: Server IP address is 127.0.1.1
        [C]: Client socket created
        [S]: Got a connection request from a client at ('127.0.0.1', 52948)
        Done.
        [C]: Data received from server: Welcome to CS 352!

     -> I put a time.sleep(5) before the recv call to see whether the server send message would not arrive.
     -> But it did, because TCP is a reliable data-delivery service.
        -> I understand the behind workings to be: the client did not send an ACK for the server's sent message and hence the server TCP socket kept resending.

