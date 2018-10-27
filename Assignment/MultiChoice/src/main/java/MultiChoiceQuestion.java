import model.*;
import controller.*;

import java.awt.*;  
import java.util.*;
import javax.swing.*;

public class MultiChoiceQuestion extends Question{
	private JLabel question_label;
	private ButtonGroup button_group;
	private JRadioButton given_answer, actual_answer;
	private ArrayList<JRadioButton> question_options_arr_list;

	/**
	 * Constructor for MultiChoiceQuestion
	 * @param  q_label           the label of the Question
	 * @param  options           the options for the Question
	 * @param  option_answer_idx the index of {@code options} where the answer lies
	 */
	public MultiChoiceQuestion(String q_label, String[] options, int option_answer_idx){
		this.button_group = new ButtonGroup();
		this.question_label = new JLabel(q_label);
		this.question_options_arr_list = new ArrayList<JRadioButton>();

		set_radio_buttons(options, option_answer_idx);
	}

	public String get_question(){
		return this.question_label.getText();
	}

	public Boolean check_answer(){
		return (this.given_answer.equals(this.actual_answer));
	}

	public JPanel get_ui_elements(){
		JPanel question_panel = new JPanel();

		question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.PAGE_AXIS));
		question_panel.add(this.question_label);

		for(JRadioButton option : question_options_arr_list){
			question_panel.add(option);
		}

		return question_panel;
	}

	public void submit(){
		for(JRadioButton radio_button : this.question_options_arr_list){
			if(radio_button.isSelected())
				this.given_answer = radio_button;
		}
	}

	/**
	 * Initializes the JRadioButtons by assigning them the option labels given.
	 * Sets the answer to the question based on the array index given.
	 * @param options           		the option labels to assign to the JRadioButtons
	 * @param option_answer_idx 		the index wherein the answer lays inside the options string array
	 * @throws IllegalArgumentException if the answer index is greater or less than the length of the option array
	 */
	private void set_radio_buttons(String[] options, int option_answer_idx) throws IllegalArgumentException{
		if(option_answer_idx > options.length - 1 || option_answer_idx < 0){
			throw new IllegalArgumentException("Option answer index given is greater than total array length (Arrays are 0-based)");
		}

		//iterate through the options array and create a JRadioButton for each option
		for(int i = 0; i < options.length; i++){
			this.question_options_arr_list.add(new JRadioButton(options[i]));
			this.button_group.add(this.question_options_arr_list.get(i));

			//if the current question is at the index stated in option_answer_idx, set the Question answer to be that
			if(option_answer_idx == i)
				this.actual_answer = this.question_options_arr_list.get(i);

			/*Initialize the given answer so that if no answer is given check_answer() can stil make a comparison*/
			this.given_answer = new JRadioButton();
		}
	}
}