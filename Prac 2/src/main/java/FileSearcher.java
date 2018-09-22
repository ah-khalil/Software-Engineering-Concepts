package edu.curtin.comp3003.filesearcher;

import javax.swing.SwingUtilities;

public class FileSearcher
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override 
            public void run()
            {            
                FSWindow window = new FSWindow();
                window.setVisible(true);
            }
        });
    }
}
