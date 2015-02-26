boardsize=8
playerone=random
playertwo=minimax:3

default: clean build run

build:
	@mkdir bin || echo bin directory already exists
	javac -d bin `find src -name "*.java"`

run:
	java -cp bin cs380.othello.Test -boardsize $(boardsize) -playerone $(playerone) -playertwo $(playertwo)

clean:
	@rm -fr bin

