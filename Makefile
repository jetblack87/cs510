inputfile=dat/SBP-level0.txt

default: clean run

build:
	@mkdir bin || echo bin directory already exists
	javac -d bin `find src -name "*.java"`

run: build
	java -cp bin mwa29.cs510.Main $(inputfile)

clean:
	@rm -fr bin