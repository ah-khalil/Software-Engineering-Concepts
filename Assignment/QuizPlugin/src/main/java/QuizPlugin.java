import model.*;
import controller.*;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * QuizPlugin loades the classes needed and then generates the plugins needed to make Questions.
 * It then invokes them to allow the user to answer them.
 * Some Questions may be invoked depending on the the correctness of the previous answers.
 */
public class QuizPlugin extends Quiz{
	public QuizPlugin(QuizController quiz_controller){
		super(quiz_controller);
	}

	/**
	 * Implements the run_quiz() method in the Quiz superclass.
	 * Loads a series of QuestionTypePlugin classes and generates Questions out of them.
	 * Invokes the Questions to send the to the QuizController to be added to the deque.
	 * 
	 * @param plug_loader
	 */
	public void run_quiz(PluginLoader plug_loader) throws LoaderException, UnsetControllerException{
		//Needed to signal the QuizController to stop trying to remove Questions from the deque.
		TerminalQuestion terminal_q = new TerminalQuestion(this.quiz_controller);

		MultiChoiceQuestion mc_q, mc_q_two, mc_q_three;
		ShortAnswerQuestion sh_q, sh_q_two, sh_q_three;

		Future<Boolean> mc_q_result, mc_q_two_result, mc_q_three_result;
		Future<Boolean> sh_q_result, sh_q_two_result, sh_q_three_result;

		MultiChoice multichoice_plugin;
		ShortAnswer shortanswer_plugin;

		try{
			//load QuestionTypePlugins using the given PluginLoader object
			multichoice_plugin = (MultiChoice) plug_loader.load("MultiChoice").newInstance();
			shortanswer_plugin = (ShortAnswer) plug_loader.load("ShortAnswer").newInstance();

			//need to set the plugins' controller otherwise Question creation methods will incur an Exception
			multichoice_plugin.set_controller(this.quiz_controller);
			shortanswer_plugin.set_controller(this.quiz_controller);

			//creating a MultiChoice Question
			mc_q = multichoice_plugin.create_multichoice(
				"When was the United States Declaration of Independence ratified?",
				new String[]{
					"1776",
					"1786",
					"1756",
					"1774"
				},
				0
			);

			mc_q_two = multichoice_plugin.create_multichoice(
				"Who is the monarch of Australia?",
				new String[]{
					"Queen Elizabeth II",
					"King Joffrey, the First of His Name",
					"John Howard",
					"Tracer, from Overwatch",
					"Bill Gates"
				},
				0
			);

			mc_q_three = multichoice_plugin.create_multichoice(
				"Which video game series revolves around a secret agent with an animal codename?",
				new String[]{
					"Devil May Cry",
					"Super Mario",
					"Metal Gear Solid",
					"Tomb Raider"
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

			//invocation of the Questions with a time limit of 100 seconds
			mc_q_result = mc_q.invoke(100);
			mc_q_two_result = mc_q_two.invoke(100);
			mc_q_three_result = mc_q_three.invoke(100);
			sh_q_result = sh_q.invoke(100);

			//invocation of sh_q_three only if mc_q_two was answered correctly
			if(mc_q_two_result.get())
				sh_q_three_result = sh_q_three.invoke();

			if(sh_q_result.get())
				sh_q_two_result = sh_q_two.invoke(100);

			//MUST call TerminalQuestion invoke() to stop QuizController trying to remove
			//Questions from deque.
			terminal_q.invoke();
		} catch(InstantiationException | IllegalAccessException i_e){
			throw new LoaderException("Unable to load and run class");
		} catch(InterruptedException int_e){
			//Let the thread be interrupted
		} catch(ExecutionException e_e){}
	}
}