package controller;

import view.*;
import model.*;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class QuizController{
	private final int POOL_SIZE = 1;

	private Object monitor;
	private QuizWindow quiz_w;
	private ExecutorService exec_service;
	private LinkedBlockingQueue<Question> question_type_b_list;	

	public QuizController(LinkedBlockingQueue question_queue, Object monitor){
		this.monitor = monitor;
		this.question_type_b_list = question_queue;
		this.exec_service = Executors.newFixedThreadPool(POOL_SIZE);
	}

	public void set_window(QuizWindow quiz_window){
		this.quiz_w = quiz_window;
	}

	public Future<Boolean> add_task(Question question){
		return this.exec_service.submit(new Callable<Boolean>(){
			@Override
			public Boolean call(){
				try{
					question_type_b_list.put(question);

					synchronized(monitor){
						monitor.wait();
					}
				} catch(InterruptedException int_e){
					int_e.printStackTrace();
				}

				return question.check_answer();
			}
		});
	}
}