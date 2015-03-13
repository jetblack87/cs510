# cs510
cs510 - AI

## Othello

To build and execute the application with default options, run the following command:
`make`

To executing with a custom configuration, add those as <name>=<value> arguments:
`make boardsize=8 playerone=random playertwo=minimax:3`

The available options are:
* boardsize: the size of the othello board
* playerone: the options for playerone
* playertwo: the options for playertwo

The possible types for the players is:
* random (HW 4) - uses the provided random player
* minimax (HW 4) - uses the minimax player. Can supply optional 'maxdepth' by using the form "minimax:<maxDepth>" (example: minimax:3).
* alphabeta (HW 4 Extra Credit 2.A) - uses the minimax player with alphabeta pruning. Can supply optional 'maxdepth' by using the form "minimax:<maxDepth>" (example: alphabeta:5).
* tournament (HW 4 Extra Credit 2.B) - uses the minimax player for tournament. Can supply optional 'maxtime' (in milliseconds) by using the form "minimax:<maxTime>" (example: minimax:3).
* alphabetaplus (HW 5 1.A) - uses a customized evaluation function to determine the worth of a move - "alphabeta:<maxdepth>"
* montecarlo (HW 5 1.B) - player that uses the Monte Carlo method to determine the next move - "montecarlo:<iterations>"
* montecarlotournament (HW 5 Extra Credit 2.A) - player that uses the Monte Carlo method in tournament mode (using given 'maxtime' in milliseconds) - "montecarlotournament:<maxTime>

The script runlots.pl has been created to aid in testing. It takes a number of times to run and player configurations:
 ./runlots.pl 100 'playerone=montecarlo:5000 playertwo=random'

It outputs the number of wins for each player and the number of ties:
javac -d bin `find src -name "*.java"`
count: 100
players: playerone=montecarlo:100 playertwo=random
....................................................................................................
playerone wins: 84
playertwo wins: 15
ties: 1


These steps have been tested on tux.
