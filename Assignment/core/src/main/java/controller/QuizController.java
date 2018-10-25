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

public class QuizController{
	private int score, total;
	private QuizWindow quiz_w;
	private ExecutorService exec_service;
	private Object submit_monitor, next_monitor;
	private LinkedBlockingDeque<Question> question_type_b_list;	

	public QuizController(LinkedBlockingDeque<Question> question_queue, Object submit_monitor, Object next_monitor, QuizWindow q_w){
		this.score = 0;
		this.total = 0;
		this.quiz_w = q_w;
		this.next_monitor = next_monitor;
		this.submit_monitor = submit_monitor;
		this.question_type_b_list = question_queue;

		new Thread(new Runnable(){
			public void run(){
				remove_questions();
			}
		}).start();
	}

	public Future<Boolean> add_task(Question question, Object question_monitor){
		FutureTask<Boolean> future_task = new FutureTask<Boolean>(
			new Callable<Boolean>(){
				public Boolean call(){
					try{
						synchronized(question_monitor){
							question_monitor.wait();
						}
					} catch(InterruptedException int_e){
						int_e.printStackTrace();
					}

					return question.check_answer();
				}
			}
		);

		try{
			question_type_b_list.put(question);
		} catch(Exception int_e) {
			int_e.printStackTrace();
		}

		new Thread(future_task).start();

		return future_task;
	}

	public void remove_questions(){
		Question curr_question = null, next_question = null;
		Object question_monitor = null;

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

			quiz_w.disable_submit_button(true);
			quiz_w.disable_next_button(true);
			quiz_w.display_final(Integer.toString(score), Integer.toString(total));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void display_and_wait(Question curr_question, Question next_question, Object question_monitor){
		try{	
			synchronized(submit_monitor){
				quiz_w.set_current_question(curr_question);
				quiz_w.set_next_question(next_question);
				quiz_w.disable_submit_button(false);
				quiz_w.disable_next_button(true);
				quiz_w.display_question();
				submit_monitor.wait();
			}

			synchronized(question_monitor){
				question_monitor.notify();
			}

			synchronized(next_monitor){
				total += 1;
				score += curr_question.check_answer() ? 1 : 0;
				quiz_w.disable_next_button(false);
				quiz_w.disable_submit_button(true);
				quiz_w.update_score(Integer.toString(score), Integer.toString(total));

				next_monitor.wait();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}