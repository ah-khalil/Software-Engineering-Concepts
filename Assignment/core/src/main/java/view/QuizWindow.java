package view;

import model.*;
import controller.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Thread;
import javax.swing.border.*;
import java.util.concurrent.LinkedBlockingQueue;

public class QuizWindow{
    private final JFrame window = new JFrame("Quiz Application");

    private Object monitor;
    private QuizTimer quiz_timer;
    private Question current_question;
    private LinkedBlockingQueue<Question> question_queue;
    private JPanel answer_pane, question_pane, results_pane, timer_pane, button_row_pane;

    public QuizWindow(LinkedBlockingQueue<Question> que_queue, Object monitor){
        this.monitor = monitor;
        this.question_queue = que_queue;
        this.quiz_timer = new QuizTimer();
        this.quiz_timer.set_window(this);

        init_window();
        get_next_question();
    }

    private void init_window(){
        Container pane;

        window.setSize(1000, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        init_panes();
        init_buttons();

        pane = window.getContentPane();
        pane.add(this.timer_pane, BorderLayout.LINE_START);
        pane.add(this.results_pane, BorderLayout.PAGE_START);
        pane.add(this.button_row_pane, BorderLayout.PAGE_END);
        
        window.pack();
        window.setVisible(true);
    }

    public void init_panes(){
    	this.timer_pane = new JPanel(new FlowLayout());
        this.answer_pane = new JPanel(new FlowLayout());
        this.results_pane = new JPanel(new FlowLayout());
        this.button_row_pane = new JPanel(new FlowLayout());
        this.question_pane = new JPanel(new FlowLayout()) {
        	@Override
        	public Dimension getMaximumSize() {
        		return new Dimension((int) super.getMaximumSize().width,(int) super.getMaximumSize().height);
        	}
        };

        this.results_pane.setPreferredSize(new Dimension(100, 50));
		this.results_pane.setMaximumSize(this.results_pane.getPreferredSize()); 
		this.results_pane.setMinimumSize(this.results_pane.getPreferredSize());
    }

    public void init_buttons(){
    	JButton quit_b, submit_b, next_b, reload_b, restart_b;

    	quit_b = new JButton("Quit");
        submit_b = new JButton("Submit");
        next_b = new JButton("Next Question");
        reload_b = new JButton("Reload");
        restart_b = new JButton("Restart");
        
        this.button_row_pane.add(quit_b);
        this.button_row_pane.add(submit_b);
        this.button_row_pane.add(next_b);
        this.button_row_pane.add(reload_b);
        this.button_row_pane.add(restart_b);

        quit_b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                close_window();
            }
        });

        submit_b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                submit_answer();
            }
        });

        next_b.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		clear_current_question();
        	}
        });
    }

    public void submit_answer(){
        current_question.submit();

        if(current_question.check_answer())
        	this.results_pane.add(new JLabel("Correct"));
        else
        	this.results_pane.add(new JLabel("Incorrect"));

        this.window.pack();

        synchronized(this.monitor){
           this.monitor.notify();
        }
    }

    public void close_window(){
        this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
    }

    public void clear_current_question(){
        this.question_pane.removeAll();
        this.results_pane.removeAll();
        this.get_next_question();
        this.window.pack();
    }

    public void get_next_question(){
        Container content_pane;

        content_pane = this.window.getContentPane();

        try{
            this.current_question = question_queue.take();
            this.question_pane.add(this.current_question.get_ui_elements());
            content_pane.add(this.question_pane, BorderLayout.CENTER);

            if(this.current_question.get_time_limit() > 0){
            	this.quiz_timer.countdown(this.current_question.get_time_limit());
            }

            this.window.pack();

        } catch(InterruptedException int_e){
            int_e.printStackTrace();
        }
    }

    public void update_time(String time){
    	this.timer_pane.add(
    		new JLabel(time)
    	);
    }
}