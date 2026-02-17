# Compilation & Running:
## With Makefile:

//launch rmiregistry (chosen port is 6090, can be modified in Makefile)
make rmi

//Compile the sources
make

//launch the server (default name: "Server Service")
make server 

//launch the client
make client



Multiple servers can run simultaneously. To start a server with a custom name

make server ARGS="Name of server"



## Without makefile
cd bin && rmiregistry <port> &

javac -d bin *.java


java -cp bin ChatServer <port> <serverName>

java -cp bin ChatClient localhost <port>



## When Running :
In the first window choose which server to connect to
In the second enter your username


# Commands
/quit # leave the chat
/hist # display the message history
/clear # clear the history 
/users # display list of connected users 
/pm <user> <message>  # send a private message to user







