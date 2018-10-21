import model.*;
import controller.*;

public class MultiChoice extends QuestionTypePlugin{
	public MultiChoiceQuestion create_multichoice(String question, String[] options, int option_answer_idx){
		MultiChoiceQuestion mc_q = new MultiChoiceQuestion(
			question,
			options,
			option_answer_idx
		);

		mc_q.set_controller(this.quiz_controller);

		return mc_q;
	}
}