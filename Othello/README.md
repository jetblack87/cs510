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
* random - uses the provided random player
* minimax - uses the minimax player. Can supply optional 'maxdepth' by using the form "minimax:<maxDepth>" (example: minimax:3).



These steps have been tested on tux.
