import view.*;
import model.*;
import controller.*;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
import javax.swing.SwingUtilities;
import java.nio.file.attribute.BasicFileAttributes;

public class QuizApplication{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
            @Override 
            public void run()
            {
            	Path path;
				Object plugin;
				QuizWindow q_w;
				Method pl_method;
				Constructor constr;
				PluginLoader loader;
				Class<?> plugin_cl, loader_cl;

				/*Used for debugging purposes*/
				Method[] pl_method_arr;

                try{
					if(args.length < 1){
						System.out.println("There isn't any quiz plugin: nothing is gonna be displayed. Soz!");		
					} else {
						loader = new PluginLoader();
						loader_cl = Class.forName("controller.PluginLoader");
						plugin_cl = loader.load("QuizPlugin");
						constr = plugin_cl.getConstructor();
						plugin = constr.newInstance();
						pl_method_arr = plugin_cl.getMethods();
						pl_method = plugin_cl.getMethod("run_quiz", loader_cl);
						pl_method.invoke(plugin, loader);

						System.out.println("Plugin Class Name: " + plugin.getClass());
						System.out.println("Constructor Name: " + constr.getName());
						System.out.println("Method Name: " + pl_method.getName());

						q_w = new QuizWindow();
					}
				} catch(Exception e){
					e.printStackTrace();
				}
            }
        });
	}
}