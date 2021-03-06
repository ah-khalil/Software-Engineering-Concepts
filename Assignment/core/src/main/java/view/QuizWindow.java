package view;

import model.*;
import controller.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Thread;
import javax.swing.border.*;
import java.util.concurrent.LinkedBlockingDeque;

public class QuizWindow{
    private final JFrame window = new JFrame("Quiz Application");

    private QuizTimer quiz_timer;
    private Object next_monitor, submit_monitor;
    private Question current_question, next_question;
    private JButton quit_b, submit_b, next_b, reload_b, restart_b;
    private JPanel answer_pane, question_pane, results_pane, timer_pane, score_pane, button_row_pane, number_keep_panel;

    public QuizWindow(Object submit_monitor, Object next_monitor){
        this.quiz_timer = new QuizTimer();
        this.quiz_timer.set_window(this);
        this.next_monitor = next_monitor;
        this.submit_monitor = submit_monitor;

        init_window();
    }

    /**
     * Initializes the window with the basic button and panel layout
     * Calls other initialization functions
     */
    private void init_window(){
        Container pane;
        JPanel number_keep_panel;

        window.setSize(1000, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        init_panes();
        init_buttons();

        pane = window.getContentPane();
        pane.add(this.results_pane, BorderLayout.PAGE_START);
        pane.add(this.button_row_pane, BorderLayout.PAGE_END);
        pane.add(this.number_keep_panel, BorderLayout.LINE_START);

        window.pack();
        window.setVisible(true);
    }

    /**
     * Initializes the QuizWindow's panels
     */
    private void init_panes(){
    	this.timer_pane = new JPanel(new FlowLayout());
        this.score_pane = new JPanel(new FlowLayout());
        this.answer_pane = new JPanel(new FlowLayout());
        this.results_pane = new JPanel(new FlowLayout());
        this.button_row_pane = new JPanel(new FlowLayout());
        this.number_keep_panel = new JPanel(new BorderLayout());
        this.question_pane = new JPanel(new BorderLayout()) {
        	@Override
        	public Dimension getMaximumSize() {
        		return new Dimension((int) super.getMaximumSize().width, (int) super.getMaximumSize().height);
        	}
        };

        this.number_keep_panel.add(this.score_pane, BorderLayout.PAGE_END);
        this.number_keep_panel.add(this.timer_pane, BorderLayout.PAGE_START);

        this.results_pane.setPreferredSize(new Dimension(100, 50));
        this.results_pane.setMaximumSize(this.results_pane.getPreferredSize()); 
        this.results_pane.setMinimumSize(this.results_pane.getPreferredSize());
    }

    /**
     * Initializes the QuizWindow's buttons
     */
    private void init_buttons(){
    	this.quit_b = new JButton("Quit");
        this.submit_b = new JButton("Submit");
        this.next_b = new JButton("Next Question");
        this.reload_b = new JButton("Reload");
        this.restart_b = new JButton("Restart");
        
        this.button_row_pane.add(this.quit_b);
        this.button_row_pane.add(this.submit_b);
        this.button_row_pane.add(this.next_b);
        this.button_row_pane.add(this.reload_b);
        this.button_row_pane.add(this.restart_b);

        //Close window when Quit button is pressed
        this.quit_b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                close_window();
            }
        });

        //Submit the answer when Submit is pressed
        this.submit_b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                submit_answer();
            }
        });

        //Clear question and notify QuizController to continue to remove questions
        //from the deque
        this.next_b.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		clear_current_question();

                synchronized(next_monitor){
                    next_monitor.notify();
                }
        	}
        });
    }

    /**
     * Stops the timer and submits the answer given to the Question.
     * Print to the window whether or not the answer was correct.
     * Notify the QuizController that Question was answered.
     */
    public void submit_answer(){
        this.quiz_timer.cancel_timer();
        this.current_question.submit();

        if(current_question.check_answer())
        	this.results_pane.add(new JLabel("Correct"));
        else
        	this.results_pane.add(new JLabel("Incorrect"));

        this.window.pack();

        synchronized(this.submit_monitor){
           this.submit_monitor.notify();
        }
    }

    /**
     * Update the time remaining in the window
     * @param time time remaining to answer the Question
     */
    public void update_time(String time){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                //As removeAll() will error if there is nothing
                //in the panel to begin, check if there is something
                //there.
                if(timer_pane.getComponents().length > 0)
                    timer_pane.removeAll();

                timer_pane.add(new JLabel(time));
                window.pack();
            }
        });
    }

    /**
     * Update the score accumulated and total Questions answered in the window
     * @param score total score
     * @param total total Questions answered
     */
    public void update_score(String score, String total){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                if(score_pane.getComponents().length > 0)
                    score_pane.removeAll();

                score_pane.add(new JLabel(score + "/" + total));
                window.pack();
            }
        });
    }

    public void set_current_question(Question question){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                current_question = question;
            }
        });
    }

    public void set_next_question(Question question){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                next_question = question;
            }
        });
    }

    public void disable_next_button(boolean is_disabled){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                next_b.setEnabled(!is_disabled);
            }
        });
    }

    public void disable_submit_button(boolean is_disabled){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                submit_b.setEnabled(!is_disabled);                
            }
        });
    }

    /**
     * Clear the window for new Question
     */
    private void clear_current_question(){
        this.question_pane.removeAll();
        this.results_pane.removeAll();
        this.timer_pane.removeAll();
        this.window.pack();
    }

    /**
     * Use the current and next Questions to update and display the user interface
     * so that the user can answer the Question.
     * Starts a countdown with the QuizTimer if the Question's time limit is > 0.
     */
    public void display_question(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                int question_time_limit;
                Container content_pane = window.getContentPane();

                question_pane.add(current_question.get_ui_elements(), BorderLayout.PAGE_START);
                question_pane.add(new JLabel("Next Question: " + next_question.get_question()), BorderLayout.PAGE_END);
                content_pane.add(question_pane, BorderLayout.CENTER);
                question_time_limit = current_question.get_time_limit();

                if(question_time_limit > 0)
                    quiz_timer.countdown(question_time_limit);

                window.pack();
            }
        });
    }

    /**
     * Display the final score
     * @param score total number of Questions answered correctly
     * @param total total number of Questions
     */
    public void display_final(String score, String total){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                results_pane.add(new JLabel("You're score was " + score + " / " + total));
                window.pack();
            }
        });
    }

    /**
     * Close the window. Used when the Quit button is clicked.
     */
    private void close_window(){
        this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
    }
}