package controller;

import view.*;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

public class QuizTimer{
	private Timer timer;
	private int current_time;
	private int current_limit;
	private QuizWindow quiz_w;

	public void countdown(int time){
		this.timer = new Timer();
		this.current_time = time;
		this.current_limit = time;

		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				current_time -= 1;

				if(current_time >= 0)
					quiz_w.update_time(Integer.toString(current_time));
				else{
					quiz_w.submit_answer();
					cancel();
				}
			}
		}, 0, 1000);
	}

	public void set_window(QuizWindow quiz_w){
		this.quiz_w = quiz_w;
	}

	public void cancel_timer(){
		this.timer.cancel();
	}
}