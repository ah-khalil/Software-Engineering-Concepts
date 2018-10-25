package controller;

import model.*;
import java.io.File;
import java.nio.file.*;
import java.lang.reflect.*;

/**
 * PluginLoader class is used to load a series of classes from a given directory.
 * It is an extension of ClassLoader, utilizing the class's defineClass() method
 * to generate a class object of the loaded-in class.
 * 
 * @author Ali Khalil
 */
public class PluginLoader extends ClassLoader{
	/**
	 * Loads classes from {@code root_plugin_dir}, travelling to the build/classes/java/main subdirectories to
	 * find the class files. It converts the class files into a byte array and uses ClassLoader's defineClass()
	 * method to return a Class object. 
	 * While the classes have been loaded, the return of this method is the Class object that inherits from either
	 * Quiz or QuestionTypePlugin.
	 * 
	 * @param root_plugin_dir	the name (case sensitive) of the plugin's root directory
	 * @return 					Class object of the class that either inherits from Quiz or QuestionTypePlugin
	 */
	public Class load(String root_plugin_dir){
		Path cl_path;
		File cl_file;
		byte[] byte_arr;
		Object class_inst = null;
		Class<?> ret_cl = null, loaded_cl = null, quiz_cl, q_pl_cl;

		try{
			quiz_cl = Class.forName("model.Quiz");
			q_pl_cl = Class.forName("model.QuestionTypePlugin");
			cl_path = Paths.get(root_plugin_dir, "build", "classes", "java", "main");
			cl_file = cl_path.toFile();

			for(File file_entry : cl_file.listFiles()){
				byte_arr = Files.readAllBytes(file_entry.toPath());
				loaded_cl = defineClass(null, byte_arr, 0, byte_arr.length);

				if(quiz_cl.isAssignableFrom(loaded_cl) || q_pl_cl.isAssignableFrom(loaded_cl))
					ret_cl = loaded_cl;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{

		}

		return ret_cl;
	}
}