package model;

import view.*;
import controller.*;

import java.awt.*;  
import java.util.*;
import javax.swing.*;

/**
 * TerminalQuestion class is used as a poison pill to signal the end of Question removal
 * Subclasses Question as it is invoked as any other Question would be
 */
public class TerminalQuestion extends Question{
	/**
	 * Constructor for TerminalQuestion
	 * @param  quiz_controller QuizController used for invocation
	 */
	public TerminalQuestion(QuizController quiz_controller){
		super.set_controller(quiz_controller);
	}

	/**
	 * Get this Question's question. Implemented abstract method.
	 * @return This Question's question
	 */
	public String get_question(){
		return "END OF QUESTIONS";
	}

	//Unused implemented abstract methods
	public void submit(){}
	public Boolean check_answer(){ return null; }
	public JPanel get_ui_elements(){ return null; }
}
