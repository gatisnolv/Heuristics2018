# Heuristics2018
This is the repository for the team Ninya turtles for the course Heuristics
We solved the Tiling case
# Installation instructions:
The program can be compiled from WITHIN the ‘tiles’ folder with the command ‘javac *.java’. 
# Use instructions:
The program can be run from the top folder (containig both the tiles and tilings folders) with the following: 

java tiles.FindTilings <algorithm_to_use> <name_of_problem_file-OR-all> <timeout_in_seconds> <mode>

# Parameters:
The program expects 4 parameters:

algorithm_to_use: 1 (=basic) OR 2 (=advanced)

name_of_problem_file: name of the problem input file without the .tiles extension OR ‘all’ 

	if this parameter is ‘all’, then the algorithm_to_use parameter is ignored (but has to be present), but the output will be for both algorithms

timeout_in_seconds: integer>=0 (0=no timeout)

mode: print OR silent

# Example usage patterns (with explanation):
java tiles.FindTilings 1 2 60 print

This example will use the ‘basic’ algorithm to find solutions to the ‘2.tiles’ problem input file within the timeout of 60 seconds and as part of the output will print the solutions graphically. At the beginning of the output there will be reported the number of solutions found and the time elapsed as part of the search.


java tiles.FindTilings 2 15-0-0 0 silent

This example will use the ‘advanced’ algorithm to find solutions to the ’15-0-0.tiles’ problem input file without a timeout. The output will report the number of solutions found (total number of solutions to the problem) and the time elapsed as part of the search.


java tiles.FindTilings 1 all 60 silent

This example will search for solutions to all problems (1.tiles, 2.tiles, 3.tiles, and 15-0-0.tiles till 55-4-4.tiles) first with both the ‘basic’ algorithm for all problems, then with ‘advanced’, with a 60 second timeout per problem, and will not print the solutions graphically, reporting number of solutions and elapsed time per problem. (The first parameter algorithm_to_use is ignored, as explained above (but necessary to be present)).


java tiles.FindTilings 2 all 0 print

This example will search for solutions to all problems (1.tiles, 2.tiles, 3.tiles, and 15-0-0.tiles till 55-4-4.tiles) first with both the ‘basic’ algorithm for all problems, then with ‘advanced’, with no timeout per problem, and will report the number of solutions and elapsed time per problem and also will print the solutions graphically. (The first parameter algorithm_to_use is ignored, as explained above (but necessary to be present)).
