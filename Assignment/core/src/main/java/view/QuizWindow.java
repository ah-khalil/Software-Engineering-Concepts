package view;

import model.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Thread;
import javax.swing.border.*;
import java.util.concurrent.LinkedBlockingQueue;

public class QuizWindow{
    private final JFrame window = new JFrame("Quiz Application");

    private Object monitor;
    private Question current_question;
    private JPanel answer_pane, question_pane;
    private LinkedBlockingQueue<Question> question_queue;

    public QuizWindow(LinkedBlockingQueue<Question> que_queue, Object monitor){
        this.monitor = monitor;
        this.question_queue = que_queue;

        init_window();
        get_next_question();
    }

    private void init_window(){
        Container pane;
        JPanel button_row_pane;
        JButton quit_b, submit_b, restart_b, reload_b;

        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pane = window.getContentPane();        
        pane.setLayout(new BorderLayout(0, 0));
        button_row_pane = new JPanel(new FlowLayout());
        this.answer_pane = new JPanel(new FlowLayout());
        this.question_pane = new JPanel();

        quit_b = new JButton("Quit");
        submit_b = new JButton("Submit");
        reload_b = new JButton("Reload");
        restart_b = new JButton("Restart");
        
        button_row_pane.add(quit_b);
        button_row_pane.add(submit_b);
        button_row_pane.add(reload_b);
        button_row_pane.add(restart_b);

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

        pane.add(button_row_pane, BorderLayout.PAGE_END);
        
        window.pack();
        window.setVisible(true);
    }

    public void submit_answer(){
        current_question.submit();

        synchronized(this.monitor){
           this.monitor.notify();
        }

        this.question_pane.removeAll();
        this.get_next_question();
        this.question_pane.revalidate();
        this.question_pane.repaint();
        this.window.pack();
    }

    public void close_window(){
        this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
    }

    public void get_next_question(){
        Container content_pane;

        content_pane = this.window.getContentPane();

        try{
            this.current_question = question_queue.take();
            this.question_pane.add(this.current_question.get_ui_elements());
            content_pane.add(this.question_pane);

            this.window.pack();
        } catch(InterruptedException int_e){
            int_e.printStackTrace();
        }
    }
}