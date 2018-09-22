package edu.curtin.comp3003.filesearcher;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FSWindow extends JFrame
{
    // A list-like container used to keep track of search results.
    private DefaultListModel<String> searchResults;
    
    public FSWindow()
    {
        super("File Searcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        JPanel searchPanel = new JPanel(new FlowLayout());        
        final JTextField searchPathBox = new JTextField("/usr", 20);
        final JTextField searchTermBox = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Path:"));
        searchPanel.add(searchPathBox);
        searchPanel.add(new JLabel("Search text:"));
        searchPanel.add(searchTermBox);
        searchPanel.add(searchButton);
        
        // When the "Search" button is pressed...
        searchButton.addActionListener(new ActionListener()
        {
            @Override 
            public void actionPerformed(ActionEvent e)
            {
                FSFilter finder = new FSFilter(
                    searchPathBox.getText(), 
                    searchTermBox.getText(), 
                    FSWindow.this);
                    
                // finder.search();
            }
        });
        
        searchResults = new DefaultListModel<>();        
        JScrollPane resultsList = new JScrollPane(new JList<String>(searchResults));
        
        JPanel auxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Clear results");
        auxPanel.add(clearButton);
        
        // When the "Clear results" button is pressed...
        clearButton.addActionListener(new ActionListener()
        {   
            @Override 
            public void actionPerformed(ActionEvent e)
            {
                searchResults.clear();
            }
        });
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(searchPanel, BorderLayout.NORTH);
        contentPane.add(resultsList, BorderLayout.CENTER);   
        contentPane.add(auxPanel, BorderLayout.SOUTH);
        pack();
    }
    
    public void addResult(String result)
    {
        searchResults.addElement(result);
    }
    
    public void showError(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
