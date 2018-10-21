package controller;

import view.*;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

public class QuizTimer{
	private Timer timer;
	private QuizWindow quiz_w;

	public QuizTimer(){
		timer = new Timer();
	}

	public void countdown(int time){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Timer time = new Timer();

				time.scheduleAtFixedRate(new TimerTask(){
					public void run(){
						quiz_w.update_time("Yeet");
					}
				}, 1000, 1);
			}
		});
	}

	public void set_window(QuizWindow quiz_w){
		this.quiz_w = quiz_w;
	}
}