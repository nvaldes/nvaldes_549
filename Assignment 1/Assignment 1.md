# CS549 Assignment 1
## Noriel Valdes

The server creates a registry that the client connects to in order to retrieve the specific requests that it needs to make for each command.  In the client, this is a server proxy that implements the `IServer` interface.

Other than that, the code was just a matter of writing the GET and PUT commands on the client and server.  For each of these, one of the participants is reading from disk and writing to a stream, where the other is reading from a stream and writing to disk.  For a PASV transfer, the server's logic occurs asynchronously using a thread, and the client's server occurs in the main thread of execution.  This is reversed for an active transfer, with the client working asynchronously and the server locking for each transfer.

The video shows traversal of the filesystem on the server, plus a GET and PUT operation.  They also show verification that the transferred files made it to their final destinations.

