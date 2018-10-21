package model;

import controller.*;

public abstract class Quiz{
	protected QuizController quiz_controller;

	public Quiz(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}

	public abstract void run_quiz(PluginLoader plug_loader);
}