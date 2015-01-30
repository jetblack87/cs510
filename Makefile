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

hw2:
	make run solvetype=BREADTH_FIRST inputfile=dat/hw2/SBP-level0.txt
	make run solvetype=BREADTH_FIRST inputfile=dat/hw2/SBP-level1.txt
	make run solvetype=BREADTH_FIRST inputfile=dat/hw2/SBP-level2.txt
	make run solvetype=BREADTH_FIRST inputfile=dat/hw2/SBP-level3.txt
	make run solvetype=DEPTH_FIRST inputfile=dat/hw2/SBP-level0.txt
	make run solvetype=DEPTH_FIRST inputfile=dat/hw2/SBP-level1.txt
	make run solvetype=DEPTH_FIRST inputfile=dat/hw2/SBP-level2.txt
	make run solvetype=DEPTH_FIRST inputfile=dat/hw2/SBP-level3.txt

hw2-extra-credit:
	make run maxmoves=1000 solvetype=ITERATIVE_DEPTH_FIRST inputfile=dat/hw2/SBP-level0.txt
	make run maxmoves=1000 solvetype=ITERATIVE_DEPTH_FIRST inputfile=dat/hw2/SBP-level1.txt
	make run maxmoves=1000 solvetype=ITERATIVE_DEPTH_FIRST inputfile=dat/hw2/SBP-level2.txt
	make run maxmoves=1000 solvetype=ITERATIVE_DEPTH_FIRST inputfile=dat/hw2/SBP-level3.txt
