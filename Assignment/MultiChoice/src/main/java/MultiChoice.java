import model.*;
import controller.*;

public class MultiChoice extends QuestionTypePlugin{
	/**
	 * Create and returns a MultiChoice Question
	 * @param  question                 the question label of the Question
	 * @param  options                  the answer options of the Question
	 * @param  option_answer_idx        the index (0-indexed) of the answer within the {@code options} given
	 * @return                          MultiChoice Question based on the information provided
	 * @throws UnsetControllerException if QuizController was not set prior to calling this method
	 */
	public MultiChoiceQuestion create_multichoice(String question, String[] options, int option_answer_idx) throws UnsetControllerException{
		MultiChoiceQuestion mc_q = new MultiChoiceQuestion(
			question,
			options,
			option_answer_idx
		);

		if(this.quiz_controller != null)
			mc_q.set_controller(this.quiz_controller);
		else
			throw new UnsetControllerException("No controller set");

		return mc_q;
	}
}