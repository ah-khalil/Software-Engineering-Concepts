public class QuizPlugin{
	private void run_quiz(PluginLoader plug_loader){
		MultiChoice multichoice_plugin;
		ShortAnswer shortanswer_plugin;

		multichoice_plugin = (MultiChoice) plug_loader.load("multi-choice", "MultiChoice");
		shortanswer_plugin = (ShortAnswer) plug_loader.load("short-answer", "ShortAnswer");
		
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
	}
}