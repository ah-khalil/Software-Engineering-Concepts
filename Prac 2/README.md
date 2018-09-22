FileSearcher 
============

(Part of worksheet 3 for COMP3003 Software Engineering Concepts)

This is a simple (simplistic) Java and Swing-based file searching application. It works, but it's essentially single-threaded, with everything happening inside Swing's event-dispatch thread.

Building and Running
--------------------

Use Gradle to compile the application:

    $ ./gradlew build
    
(Note: "gradlew" is Unix bash script, and "gradlew.bat" is an equivalent Windows script.)
    
The build process will create a .jar file in build/libs. You can run the application as follows:

    $ java -jar build/libs/file_searcher.jar
        
Source code
-----------

The source code is (according to convention) located in src/main/java/.
