package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FSFileFinder
{
    private String searchPath;
    private String searchTerm;
    private FSWindow window;

    public FSFileFinder(String searchPath, String searchTerm, FSWindow window)
    {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.window = window;
    }
    
    public void search()
    {
        try
        {
            // Recurse through the directory tree
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    // Check whether each file contains the search term, and if
                    // so, add it to the list.
                    String fileStr = file.toString();
                    if(fileStr.contains(searchTerm))
                    {
                        window.addResult(fileStr);
                    }
                    
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException e)
        {
            // This error handling is a bit quick-and-dirty, but it will suffice here.
            window.showError(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
