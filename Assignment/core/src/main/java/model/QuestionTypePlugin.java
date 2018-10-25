package model;

import controller.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public abstract class QuestionTypePlugin{
	protected QuizController quiz_controller;

	public void set_controller(QuizController quiz_controller){
		this.quiz_controller = quiz_controller;
	}
}