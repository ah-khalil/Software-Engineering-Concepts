package model;

import view.*;
import controller.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

public abstract class Question{
	protected QuizController quiz_controller;

	public void set_controller(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}
	
	public abstract Boolean check_answer();

	public abstract void submit();
	public abstract JPanel get_ui_elements();

	public abstract Future<Boolean> invoke();
	public abstract Future<Boolean> invoke(int time);

}