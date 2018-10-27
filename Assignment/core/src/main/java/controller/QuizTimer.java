package controller;

import view.*;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 * QuizTimer keeps track of the time remaining to answer questions that have a time limit
 * associated with them.
 * 
 * @author Ali Khalil
 */
public class QuizTimer{
	private Timer timer;
	private int current_time;
	private int current_limit;
	private QuizWindow quiz_w;

	/**
	 * Use the Timer object to tick down from the set {@code time} to 0.
	 * Once time is up, submit whatever was selected or use default answer
	 * in the question if nothing was selected
	 * @param time total time to answer the question
	 */
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

	/**
	 * Sets window to update
	 * @param quiz_w QuizWindow to update
	 */
	public void set_window(QuizWindow quiz_w){
		this.quiz_w = quiz_w;
	}

	/**
	 * Cancel the timer. Used for when time is up.
	 */
	public void cancel_timer(){
		this.timer.cancel();
	}
}