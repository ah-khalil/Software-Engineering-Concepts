import view.*;
import model.*;
import controller.*;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.reflect.*;
import java.nio.file.attribute.BasicFileAttributes;

public class QuizApplication{
	public static void main(String[] args){
		Path path;
		Object plugin, r;
		Class plugin_cl;
		Method pl_method;
		Constructor constr;
		PluginLoader loader;

		try{

			if(args.length < 1){
				System.out.println("There isn't any quiz plugin: nothing is gonna be displayed. Soz!");		
			} else {
				loader = new PluginLoader();
				plugin_cl = loader.load(args[0], "QuizPlugin.class");
				constr = plugin_cl.getConstructor();
				plugin = constr.newInstance();
				pl_method = plugin_cl.getMethod("run_quiz");

				System.out.println("Plugin Class Name: " + plugin.getClass());
				System.out.println("Constructor Name: " + constr.getName());
				System.out.println("Method Name: " + pl_method.getName());

				pl_method.invoke(plugin, loader);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}