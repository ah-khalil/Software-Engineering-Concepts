import model.*;
import controller.*;

public class QuizPlugin extends Quiz{
	public void run_quiz(PluginLoader plug_loader){
		MultiChoice multichoice_plugin;
		ShortAnswer shortanswer_plugin;

		try{
			multichoice_plugin = (MultiChoice) plug_loader.load("MultiChoice").newInstance();
			shortanswer_plugin = (ShortAnswer) plug_loader.load("ShortAnswer").newInstance();
			
			multichoice_plugin.create_multichoice(
				"Which one of these is trash?",
				new String[]{
					"Anime",
					"Avatar",
					"Ass",
					"Aloe Vera"
				},
				0
			);

			shortanswer_plugin.create_shortanswer(
				"In which American state do you Milli Rock?",
				new String[]{
					"New York",
					"NY",
					"NYC",
					"New York City" 
				}
			);
			System.out.println("TTGT for the Clout");
		} catch(InstantiationException inst_e){
			inst_e.printStackTrace();
		}  catch(IllegalAccessException il_a_e){
			il_a_e.printStackTrace();
		} finally{

		}
	}
}