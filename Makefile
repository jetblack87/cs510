inputfile=dat/hw1/SBP-level0.txt
maxmoves=100
solvetype=RANDOM_WALK

default: clean build run

build:
	@mkdir bin || echo bin directory already exists
	javac -d bin `find src -name "*.java"`

run:
	java -cp bin mwa29.cs510.Main -file $(inputfile) -maxMoves $(maxmoves) -solveType $(solvetype)

clean:
	@rm -fr bin
