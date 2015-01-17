inputfile=dat/SBP-level0.txt
maxmoves=100

default: clean run

build:
	@mkdir bin || echo bin directory already exists
	javac -d bin `find src -name "*.java"`

run: build
	java -cp bin mwa29.cs510.Main -file $(inputfile) -maxMoves $(maxmoves)

clean:
	@rm -fr bin