import model.*;
import controller.*;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.concurrent.Future;
import java.nio.file.attribute.BasicFileAttributes;

public class QuizPlugin extends Quiz{
	public QuizPlugin(QuizController quiz_controller){
		super(quiz_controller);
	}

	public void run_quiz(PluginLoader plug_loader){
		MultiChoiceQuestion mc_q, mc_q_two, mc_q_three;
		Future<Boolean> mc_q_result, mc_q_two_result, mc_q_three_result;
		MultiChoice multichoice_plugin;

		try{
			multichoice_plugin = (MultiChoice) plug_loader.load("MultiChoice").newInstance();
			multichoice_plugin.set_controller(this.quiz_controller);

			mc_q = multichoice_plugin.create_multichoice(
				"Which one of these is trash?",
				new String[]{
					"Anime",
					"Avatar",
					"Ass",
					"Aloe Vera"
				},
				0
			);

			mc_q_two = multichoice_plugin.create_multichoice(
				"In which American state is the Milly Rock performed?",
				new String[]{
					"New York",
					"Arkansas",
					"Florida",
					"California",
					"New Jersey"
				},
				0
			);

			mc_q_three = multichoice_plugin.create_multichoice(
				"Which video game character has a recurring role in the Fox cartoon, Family Guy?",
				new String[]{
					"Dante, from Devil May Cry",
					"Mario, from Super Mario",
					"Sans, from Undertale",
					"Lara Croft, from Tomb Raider"
				},
				2
			);

			mc_q_result = mc_q.invoke();
			mc_q_two_result = mc_q_two.invoke();
			mc_q_three_result = mc_q_three.invoke();

			if(!mc_q_result.get()){
				System.out.println("You just got yote!");
			} else {
				System.out.println("You have yote on the question");
			}

			// this.quiz_controller.end_of_question();
		} catch(InstantiationException inst_e){
			inst_e.printStackTrace();
		}  catch(IllegalAccessException il_a_e){
			il_a_e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{

		}
	}
}