import model.*;
import controller.*;

import java.awt.*;  
import java.util.*;
import javax.swing.*;
import java.util.concurrent.Future;

public class MultiChoiceQuestion extends Question{
	private JLabel question_label;
	private ButtonGroup button_group;
	private JRadioButton given_answer, actual_answer;
	private ArrayList<JRadioButton> question_options_arr_list;

	public MultiChoiceQuestion(String q_label, String[] options, int option_answer_idx){
		this.button_group = new ButtonGroup();
		this.question_label = new JLabel(q_label);
		this.question_options_arr_list = new ArrayList<JRadioButton>();

		set_radio_buttons(options, option_answer_idx);
	}

	public Future<Boolean> invoke(){
		return invoke(-1);
	}

	public Future<Boolean> invoke(int time){
		return this.quiz_controller.add_task(this, time);
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

	private void set_radio_buttons(String[] options, int option_answer_idx){
		if(option_answer_idx > options.length - 1){
			throw new IllegalArgumentException("Option answer index given is greater than total array length (Arrays are 0-based)");
		}

		for(int i = 0; i < options.length; i++){
			this.question_options_arr_list.add(new JRadioButton(options[i]));
			this.button_group.add(this.question_options_arr_list.get(i));

			if(option_answer_idx == i)
				this.actual_answer = this.question_options_arr_list.get(i);

			/*Initialize the given answer so that if no answer is given check_answer() can stil make a comparison*/
			this.given_answer = new JRadioButton();
		}
	}
}