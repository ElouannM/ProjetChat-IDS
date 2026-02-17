PORT = 6090

all:
	mkdir -p bin
	javac -d bin *.java

rmi :
	cd bin && rmiregistry $(PORT) &

server :
	java -cp bin ChatServer $(PORT) $(ARGS)

client :
	java -cp bin ChatClient localhost $(PORT)

clean:
	rm -r bin/*.class
	pkill -f rmiregistry