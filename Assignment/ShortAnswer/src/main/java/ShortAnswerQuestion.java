import model.*;
import controller.*;

import java.awt.*;  
import java.util.*;
import javax.swing.*;

public class ShortAnswerQuestion extends Question{
	private JLabel question_label;
	private JTextArea answer_text_field;
	private String given_answer, actual_answer;

	public ShortAnswerQuestion(String question, String answer){
		this.question_label = new JLabel(question);
		this.answer_text_field = new JTextArea();
		this.actual_answer = answer;

		//always best to give given_answer a default value in case it was not answered
		this.given_answer = "";
	}

	public String get_question(){
		return this.question_label.getText();
	}

	public Boolean check_answer(){
		return (this.given_answer.equals(this.actual_answer));
	}

	public void submit(){
		this.given_answer = this.answer_text_field.getText();
	}

	public JPanel get_ui_elements(){
		JPanel question_panel = new JPanel();
		JPanel text_panel = new JPanel();

		question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.PAGE_AXIS));
		text_panel.setLayout(new BoxLayout(text_panel, BoxLayout.PAGE_AXIS));
		text_panel.add(this.answer_text_field);
		question_panel.add(this.question_label);
		question_panel.add(text_panel);

		return question_panel;
	}
}