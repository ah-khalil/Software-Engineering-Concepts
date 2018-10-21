package model;

import view.*;
import controller.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

public abstract class Question{
	protected int time_limit;
	protected QuizController quiz_controller;

	public Question(){
		this.time_limit = 0;
	}

	public void set_controller(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}
	
	public abstract Boolean check_answer();

	public abstract void submit();
	public abstract JPanel get_ui_elements();

	public int get_time_limit(){
		return this.time_limit;
	}

	public Future<Boolean> invoke(){
		return invoke(-1);
	}

	public Future<Boolean> invoke(int time){
		this.time_limit = time;
		return this.quiz_controller.add_task(this);
	}
}