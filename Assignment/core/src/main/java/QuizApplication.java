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

		SystemPrinter system_print = new SystemPrinter();

		/*Used for debugging purposes*/
		Method[] pl_method_arr;

        try{
        	next_monitor = new Object();
			submit_monitor = new Object();
			q_w = new QuizWindow(submit_monitor, next_monitor);
			test_control = new QuizController(submit_monitor, next_monitor, q_w);

			if(args.length < 1){
				System.out.println("There isn't any quiz plugin: nothing is gonna be displayed. Soz!");		
			} else {
				loader = new PluginLoader();
				plugin_cl = loader.load(args[0]);
				loader_cl = Class.forName("controller.PluginLoader");
				control_cl = Class.forName("controller.QuizController");
				constr = plugin_cl.getConstructor(control_cl);
				plugin = constr.newInstance(test_control);
				pl_method_arr = plugin_cl.getMethods();
				pl_method = plugin_cl.getMethod("run_quiz", loader_cl);
				pl_method.invoke(plugin, loader);
			}
		} catch(LoaderException load_e){
			system_print.print_message("ERROR: " + load_e.getMessage() + "\n");
		} catch(ClassNotFoundException cnf_e){
			system_print.print_message("ERROR: " + cnf_e.getMessage() + "\n");
		} catch(NoSuchMethodException nsm_e){
			system_print.print_message("ERROR: Please make sure there is a run_quiz() method in the QuizPlugin\n");
		} catch(InstantiationException ins_e){
			system_print.print_message("ERROR: " + ins_e.getMessage() + "\n");
		} catch(IllegalAccessException ia_e){
			system_print.print_message("ERROR: Please make sure the run_quiz() method is public, not private\n");
		} catch(Exception e){
			system_print.print_message("ERROR: An unknown error has occurred\n");
		}
	}
}