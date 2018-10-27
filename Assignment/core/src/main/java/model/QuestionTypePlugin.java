package model;

import controller.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * QuestionTypePlugin abstract class contains only the QuizController used for all subclasses of QuestionTypePlugin
 *
 * @author Ali Khalil
 */
public abstract class QuestionTypePlugin{
	protected QuizController quiz_controller;

	/**
	 * Set QuizController for this QuestionTypePlugin
	 * @param quiz_controller QuizController to use
	 */
	public void set_controller(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}
}