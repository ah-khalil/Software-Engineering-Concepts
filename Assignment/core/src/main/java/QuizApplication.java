import view.*;
import model.*;
import controller.*;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
import javax.swing.SwingUtilities;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.nio.file.attribute.BasicFileAttributes;

public class QuizApplication{
	public static void main(String[] args){
    	Path path;
		QuizWindow q_w;
		Method pl_method;
		Constructor constr;
		PluginLoader loader;
		QuizController test_control;
		Class<?> plugin_cl, loader_cl, control_cl;
		Object plugin, submit_monitor, next_monitor;
		LinkedBlockingDeque<Question> question_type_b_list;

		/*Used for debugging purposes*/
		Method[] pl_method_arr;

        try{
			if(args.length < 1){
				System.out.println("There isn't any quiz plugin: nothing is gonna be displayed. Soz!");		
			} else {
				next_monitor = new Object();
				submit_monitor = new Object();
				question_type_b_list = new LinkedBlockingDeque<Question>();
				q_w = new QuizWindow(submit_monitor, next_monitor);
				test_control = new QuizController(question_type_b_list, submit_monitor, next_monitor, q_w);

				loader = new PluginLoader();
				plugin_cl = loader.load(args[0]);
				loader_cl = Class.forName("controller.PluginLoader");
				control_cl = Class.forName("controller.QuizController");
				constr = plugin_cl.getConstructor(control_cl);
				plugin = constr.newInstance(test_control);
				pl_method_arr = plugin_cl.getMethods();
				pl_method = plugin_cl.getMethod("run_quiz", loader_cl);
				pl_method.invoke(plugin, loader);

				// System.out.println("Plugin Class Name: " + plugin.getClass());
				// System.out.println("Plugin Class Constructors: " + Arrays.toString(plugin_cl.getConstructors()));
				// System.out.println("Constructor Name: " + constr.getName());
				// System.out.println("Method Name: " + pl_method.getName());

				// Thread thread = new Thread(new Runnable(){
				// 	public void run(){
				// 		try{
				// 		} catch(IllegalAccessException ia_e){
				// 			ia_e.printStackTrace();
				// 		} catch(InvocationTargetException inv_t_e){
				// 			inv_t_e.printStackTrace();
				// 		}
				// 	}
				// });

				// thread.start();

				// while(thread.isAlive()){
				// 	System.out.println("Its Dat Boi!");
				// }
			}

		} catch(Exception e){
			e.printStackTrace();
		}
	}
}