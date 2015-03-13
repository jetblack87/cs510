#!/usr/bin/perl

my $count = $ARGV[0];
my $players = $ARGV[1] ?$ARGV[1] : 'playerone=alphabetaplus:4 playertwo=random';

system('make clean build');

my $playerone_wins = 0;
my $playertwo_wins = 0;
my $ties = 0;

print "count: $count\n";
print "players: $players\n";
foreach (my $i = 0; $i < $count; $i++) {
    @output = `make run $players`;
    foreach my $line (@output) {
	if ($line =~ /Final state with score: (.*)/) {
	    my $score = $1;
	    if ($score > 0) {
		$playerone_wins++;
	    } elsif ($score < 0) {
		$playertwo_wins++;
	    } else {
		$ties++;
	    }
	}
    }
    print '.';
}
print "\n";
print "playerone wins: $playerone_wins\n";
print "playertwo wins: $playertwo_wins\n";
print "ties: $ties\n";
