package model;

import controller.*;

/**
 * Quiz abstract class simply sets QuizController to be used by all subclasses of Quiz
 */
public abstract class Quiz{
	protected QuizController quiz_controller;

	/**
	 * Contructor for QuizController
	 * @param  quiz_controller QuizController to use
	 */
	public Quiz(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}

	//Abstract method
	public abstract void run_quiz(PluginLoader plug_loader) throws LoaderException, UnsetControllerException;
}