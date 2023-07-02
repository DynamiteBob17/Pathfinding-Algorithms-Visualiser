# About
- desktop pathfinding algorithms visualiser with audio feedback like in those sorting algorithm visualisers all over YouTube
- algorithms featured: depth-first, breadth-first and greedy-first search, Dijkstra's algorithm and A Star Search
- other than placing and removing the different sized blocks that are obstacles, you can also choose to generate a maze with 2 algorithms: Kruskal's and Recursive Backtracking algorithms
- also uses some serialization/deserialization features to save your custom key bindings for using the program
- [live demo](https://drive.google.com/file/d/1gvM60ihRaTIwT31ex3FoCBo9PGQA8zNv/view)

# How to use
If you have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and up installed you can:
- download the .jar executable from the release  
**OR**
- compile and run the repository as a Maven project with your method of choice.  
  
`mvn clean compile exec:java` to run from a command.  

> :warning: **If you run the program with no sound devices existing or connected to your system (if your sound is just muted this doesn't apply)**:  
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The MIDI sounds will not be loaded, so if you connect any sound device  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;while in the program, it won't be able to play any sounds. Therefore,  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;you need to restart the program, so it can load the sounds it needs.
