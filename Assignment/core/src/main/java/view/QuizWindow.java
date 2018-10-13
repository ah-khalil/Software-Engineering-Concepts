package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Demonstrates GUI concepts needed for the assignment. You may use/adapt any/all of this code for 
 * your assignment, but be aware that it isn't designed to be copied-and-pasted. You will need to
 * consider the structure at a fairly high level.
 * 
 * Other notes:
 * (a) Compile and run this code to see what it does, so you have a reference point when looking 
 *     through it.
 *
 * (b) This is Swing, of course, but you're still free to use JavaFX instead if you wish.
 * 
 * (c) Prioritise! A working GUI is infinitely better than a nice-looking GUI. ;-)
 */
public class QuizWindow
{
    public QuizWindow()
    {
        // Create a new Swing window, with specified title and size.
        final JFrame window = new JFrame("COMP3003 GUI Demo (Swing)");
        window.setSize(800, 600);
        
        // Causes the window's "close" button to exit the program. FYI, you don't necessarily want 
        // to do this in practice, as you won't be able to warn the user about unsaved work.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // The content pane is the highest-level container of GUI elements.
        Container pane = window.getContentPane();
        
        // Set it up so that elements are added vertically. (Note: there are a number of perfectly 
        // valid alternative layout mechanisms.)
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        // Setup three toolbar buttons.
        JToolBar toolbar = new JToolBar();
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton addRadioButtonsBtn = new JButton("Add Radio Buttons");
        JButton addTextFieldBtn = new JButton("Add Text Field");
        JButton clearBtn = new JButton("Clear");
        toolbar.add(addRadioButtonsBtn);
        toolbar.add(addTextFieldBtn);
        toolbar.add(clearBtn);
        
        // Create the panel where we'll be dynamically adding/clearing GUI elements.
        // (nb. Overriding getMaximumSize() is purely aesthetic, not functional.)
        JPanel dynamicPanel = new JPanel()
        {
            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(
                    super.getMaximumSize().width,
                    super.getPreferredSize().height);
            }
        };
        
        // The panel will have a grid-based layout, with 3 columns and unlimited rows. GUI 
        // elements are added in 'row-major' order; i.e. the first one in (0,0), then (0,1), then 
        // (0,2), then (1,0), (1,1), (1,2), (2,0), etc.
        dynamicPanel.setLayout(new GridLayout(0, 3));
        
        // Purely aesthetic.
        dynamicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);        
        dynamicPanel.setBorder(
            new CompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        // Add the major GUI components to the window.
        pane.add(toolbar);
        pane.add(dynamicPanel);
        
        // Setup a callback for the 'Add Radio Buttons' button.
        addRadioButtonsBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel("Some radio buttons:");
                JPanel radioButtonPanel = new JPanel();
                radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
                
                // Make new radio buttons
                JRadioButton[] radioButtons = {
                    new JRadioButton("Radio button 1"),
                    new JRadioButton("Radio button 2"),
                    new JRadioButton("Radio button 3")
                };
                
                ButtonGroup group = new ButtonGroup();
                for(int i = 0; i < radioButtons.length; i++)
                {
                    // Add each button to the GUI.
                    radioButtonPanel.add(radioButtons[i]);
                    
                    // Ensure only one radio button can be selected at a time.
                    group.add(radioButtons[i]);
                }
                
                JButton showBtn = new JButton("Show Value");
                
                // Setup a callback for the 'Show Value' button.
                showBtn.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // Demonstrate how to determine which radio button is selected. (Note 
                        // that, initially, no radio button is selected.)                        
                        for(int i = 0; i < radioButtons.length; i++)
                        {
                            if(radioButtons[i].isSelected())
                            {
                                JOptionPane.showMessageDialog(
                                    window, "Radio button #" + i + " ('" + 
                                            radioButtons[i].getText() + "')");
                                break;
                            }
                        }
                    }
                });
                
                dynamicPanel.add(label);
                dynamicPanel.add(radioButtonPanel);
                dynamicPanel.add(showBtn);   
                
                // Force the GUI to rework its layout. (If we don't do this, the changes we've 
                // made won't actually appear on the screen.)
                dynamicPanel.revalidate();
            }
        });
        
        // Setup a callback for the 'Add Text Field' button.
        addTextFieldBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JLabel label = new JLabel("A text field:");
                final JTextField textField = new JTextField();
                JButton showBtn = new JButton("Show Value");
                
                // Setup a callback for the 'Show Value' button.
                showBtn.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // Demonstrate how to obtain the contents of the text field.
                        JOptionPane.showMessageDialog(
                            window, "Text field value: '" + textField.getText() + "'");
                    }
                });
            
                dynamicPanel.add(label);
                dynamicPanel.add(textField);
                dynamicPanel.add(showBtn);
                
                // Force the GUI to rework its layout.
                dynamicPanel.revalidate();
            }
        });
        
        // Setup a callback for the 'Clear' button.
        clearBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Remove all elements currently within the panel.
                dynamicPanel.removeAll();
                
                // Force the GUI to rework its layout.
                dynamicPanel.revalidate();
            }
        });
        
        // This innocuous-looking call is what actually starts the GUI running. The main() method 
        // will now finish, but the program will keep going thanks to Swing having just created 
        // its event-dispatch thread.
        window.setVisible(true);
    }
}