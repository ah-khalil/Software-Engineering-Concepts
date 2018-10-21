import model.*;
import controller.*;

public class ShortAnswer extends QuestionTypePlugin{
	public ShortAnswerQuestion create_shortanswer(String question, String answer){
		ShortAnswerQuestion sh_q = new ShortAnswerQuestion(
			question,
			answer
		);

		sh_q.set_controller(this.quiz_controller);

		return sh_q;
	}
}