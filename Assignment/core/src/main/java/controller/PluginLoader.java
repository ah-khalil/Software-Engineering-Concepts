package controller;

import model.*;
import java.io.File;
import java.nio.file.*;
import java.lang.reflect.*;

public class PluginLoader extends ClassLoader{
	public Class load(String root_plugin_dir){
		Path cl_path;
		File cl_file;
		byte[] byte_arr;
		Object class_inst = null;
		Class<?> ret_cl = null, loaded_cl = null, quiz_cl, q_pl_cl;

		try{
			quiz_cl = Class.forName("model.Quiz");
			q_pl_cl = Class.forName("model.QuestionTypePlugin");
			cl_path = Paths.get(root_plugin_dir, "build", "classes", "main");
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

		return (ret_cl != null) ? ret_cl : null;
	}
}