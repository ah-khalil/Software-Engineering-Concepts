package controller;

import view.*;
import model.*;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * QuizController manages the addition and removal of questions in/from the LinkedBlockingDeque.
 * It keeps a reference of the QuizWindow GUI in order to update it in certain events.
 * 
 * @author Ali Khalil
 */
public class QuizController{
	private int score, total;
	private QuizWindow quiz_w;
	private ExecutorService exec_service;
	private Object submit_monitor, next_monitor;
	private LinkedBlockingDeque<Question> question_type_b_list;	


	/**
	 * Constructor for QuizController
	 * @param  submit_monitor	a monitor that waits for the Submit button to be pressed
	 * @param  next_monitor		a monitor that waits for the Next button to be pressed
	 * @param  q_w            	a reference to the QuizWindow used in the application
	 */
	public QuizController(Object submit_monitor, Object next_monitor, QuizWindow q_w){
		this.score = 0;
		this.total = 0;
		this.quiz_w = q_w;
		this.next_monitor = next_monitor;
		this.submit_monitor = submit_monitor;
		this.question_type_b_list = new LinkedBlockingDeque<Question>();

		//Immediately begin removing questions from the deque (Deque?)
		//Runs on another thread to prevent the main thread from being
		//blocked when taking [take()] from the deque
		new Thread(new Runnable(){
			public void run(){
				remove_questions();
			}
		}).start();
	}

	/**
	 * Responsible for entering the questions into the deque and for representing the
	 * result of the question as a Future, to be returned
	 * @param  question         the question to enter the deque
	 * @param  question_monitor the question's monitor; block the Future from returning until the question was answered
	 * @return                  a future representing the result of the question
	 */
	public Future<Boolean> add_task(Question question, Object question_monitor){
		FutureTask<Boolean> future_task = new FutureTask<Boolean>(
			new Callable<Boolean>(){
				public Boolean call(){
					try{
						synchronized(question_monitor){
							question_monitor.wait();
						}
					} catch(InterruptedException int_e){
						//Let the thread get interrupted
					}

					return question.check_answer();
				}
			}
		);

		try{
			question_type_b_list.put(question);
		} catch(InterruptedException int_e){
			//Let the thread get interrupted
		}

		//Start the future in a different thread to prevent any blocking
		//to the main thread
		new Thread(future_task).start();

		return future_task;
	}

	/**
	 * Removes questions from the deque repeatedly until a TerminalQuestion is reached.
	 */
	public void remove_questions(){
		Question curr_question = null, next_question = null;
		Object question_monitor = null;

		//Set current question to be the previous "next" question and take from the deque
		//to get the current "next" question.
		try{
			while(!(next_question instanceof TerminalQuestion)){
				if(curr_question == null)
					curr_question = question_type_b_list.take();
				else
					curr_question = next_question;

				next_question = question_type_b_list.take();
				question_monitor = curr_question.get_question_monitor();
				display_and_wait(curr_question, next_question, question_monitor);
			}

			//Once the end of the quiz is reached, disable both Submit and Next buttons
			//and display final score
			quiz_w.disable_submit_button(true);
			quiz_w.disable_next_button(true);
			quiz_w.display_final(Integer.toString(score), Integer.toString(total));
		} catch(InterruptedException int_e) {
			//Let the thread get interrupted
		}
	}

	/**
	 * Changes the state of the QuizWindow, updating the question and notifies the waiting question monitor
	 * that the question has been answered
	 * @param curr_question    the current question the QuizWindow has to display
	 * @param next_question    the next question the QuizWindow has to display
	 * @param question_monitor the current question's monitor
	 */
	private void display_and_wait(Question curr_question, Question next_question, Object question_monitor){
		try{
			//Set the current and next questions, and disable the Next Question button
			//Wait until the Submit button has been pressed
			synchronized(submit_monitor){
				quiz_w.set_current_question(curr_question);
				quiz_w.set_next_question(next_question);
				quiz_w.disable_submit_button(false);
				quiz_w.disable_next_button(true);
				quiz_w.display_question();
				submit_monitor.wait();
			}

			//Notify the awaiting future for the current question that the question has
			//been answered
			synchronized(question_monitor){
				question_monitor.notify();
			}

			//Update total number of questions and the score, disable Submit button, and
			//wait for the Next button to be pressed
			synchronized(next_monitor){
				total += 1;
				score += curr_question.check_answer() ? 1 : 0;
				quiz_w.disable_next_button(false);
				quiz_w.disable_submit_button(true);
				quiz_w.update_score(Integer.toString(score), Integer.toString(total));

				next_monitor.wait();
			}
		} catch(InterruptedException int_e){
			//Let the thread get interrupted
		}
	}
}