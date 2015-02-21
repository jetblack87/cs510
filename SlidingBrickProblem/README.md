# cs510
cs510 - AI

## Sliding Brick Puzzle

To build and execute the application with default options, run the following command:
`make`

To executing with a custom input file and max move count, run using the 'inputfile' and 'maxmoves' overrides:
`make inputfile=dat/SBP-level3.txt maxmoves=1000`

To change the solve type, (algorithm used to solve), use 'solvetype':
`make solvetype=BREADTH_FIRST inputfile=dat/SBP-level3.txt maxmoves=1000`

Available solvetypes:
RANDOM_WALK (from hw1)
BREATH_FIRST (from hw2)
DEPTH_FIRST (from hw2)
ITERATIVE_DEPTH_FIRST (extra credit for hw2)
A_STAR (from hw3)

The 'maxmoves' option only is applied to RANDOM_WALK and ITERATIVE_DEPTH_FIRST algorithms.

Additional targets for hw2:
Run for hw2: `make hw2`
Run for hw2 extra credit: `make hw2-extra-credit`

Additional targets for hw3:
Run for hw4: `make hw3` (output was used to generate output-part2.txt)


These steps have been tested on tux.
