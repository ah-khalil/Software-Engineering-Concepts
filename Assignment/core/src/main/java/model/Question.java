package model;

import view.*;
import controller.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

/**
 * Question abstract class contains a set of commonalities between all types of Questions, such as
 * invoke(), set_controller(), get_question_monitor().
 * Subclasses of Question must implement abstract methods.
 * 
 * @author Ali Khalil
 */
public abstract class Question{
	protected int time_limit;
	protected Object question_monitor;
	protected QuizController quiz_controller;

	/**
	 * Constructor for Question
	 */
	public Question(){
		this.time_limit = 0;
		this.question_monitor = new Object();
	}

	/**
	 * Retrieve this Question's monitor
	 * @return this Question's monitor
	 */
	public Object get_question_monitor(){
		return this.question_monitor;
	}

	/**
	 * Set the controller for this Question
	 * @param quiz_controller the QuizController to be used for this Question
	 */
	public void set_controller(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}

	/**
	 * Get this Question's time limit
	 * @return this Question's time limit
	 */
	public int get_time_limit(){
		return this.time_limit;
	}

	/**
	 * Add this Question to this Question's controller (which should be set).
	 * This version calls the other invoke() method with parameter of -1 to 
	 * indicate that the Question has no time limit
	 * @return a Future of the Question's result
	 */
	public Future<Boolean> invoke() throws UnsetControllerException{
		return invoke(-1);
	}

	/**
	 * Add this Question to this Question's controller (which should be set)
	 * @param time the amount of time to answer this Question
	 *             (< 0 if no time limit)
	 * @return a Future of the Question's result
	 */
	public Future<Boolean> invoke(int time) throws UnsetControllerException{
		Future<Boolean> ret_future;

		this.time_limit = time;

		if(this.quiz_controller != null)
			ret_future = this.quiz_controller.add_task(this, this.question_monitor);
		else
			throw new UnsetControllerException("No controller set for Question: " + get_question());

		return ret_future;
	}

	//Abstract Methods
	public abstract void submit();
	public abstract String get_question();
	public abstract Boolean check_answer();
	public abstract JPanel get_ui_elements();
}