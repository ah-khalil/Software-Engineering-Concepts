package model;

import view.*;
import controller.*;

import java.awt.*;  
import java.util.*;
import javax.swing.*;

public class TerminalQuestion extends Question{
	public TerminalQuestion(QuizController quiz_controller){
		super.set_controller(quiz_controller);
	}

	public String get_question(){
		return "END OF QUESTIONS";
	}

	public void submit(){}
	public Boolean check_answer(){ return null; }
	public JPanel get_ui_elements(){ return null; }
}
