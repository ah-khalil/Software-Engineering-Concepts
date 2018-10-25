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

	/**
	 * @param plug_loader
	 */
	public void run_quiz(PluginLoader plug_loader){
		TerminalQuestion terminal_q = new TerminalQuestion(this.quiz_controller);

		MultiChoiceQuestion mc_q, mc_q_two, mc_q_three;
		ShortAnswerQuestion sh_q, sh_q_two, sh_q_three;

		Future<Boolean> mc_q_result, mc_q_two_result, mc_q_three_result;
		Future<Boolean> sh_q_result, sh_q_two_result, sh_q_three_result;

		MultiChoice multichoice_plugin;
		ShortAnswer shortanswer_plugin;

		try{
			multichoice_plugin = (MultiChoice) plug_loader.load("MultiChoice").newInstance();
			shortanswer_plugin = (ShortAnswer) plug_loader.load("ShortAnswer").newInstance();
			multichoice_plugin.set_controller(this.quiz_controller);
			shortanswer_plugin.set_controller(this.quiz_controller);

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

			sh_q = shortanswer_plugin.create_shortanswer(
				"Who won the 2016 US Presidential Election?",
				"Donald Trump"
			);

			sh_q_two = shortanswer_plugin.create_shortanswer(
				"How many bullet catridges does the CSAA have?",
				"Six"
			);

			sh_q_three = shortanswer_plugin.create_shortanswer(
				"How many milligrams of sodium in a 600ml bottle of Coke Zero?",
				"25mg"
			);

			mc_q_result = mc_q.invoke(100);
			mc_q_two_result = mc_q_two.invoke(100);
			mc_q_three_result = mc_q_three.invoke(100);
			sh_q_result = sh_q.invoke(100);

			if(mc_q_two_result.get())
				sh_q_three_result = sh_q_three.invoke();

			if(sh_q_result.get())
				sh_q_two_result = sh_q_two.invoke(100);

			if(!mc_q_result.get()){
				System.out.println("You just got yote!");
			} else {
				System.out.println("You have yote on the question");
			}

			terminal_q.invoke();
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