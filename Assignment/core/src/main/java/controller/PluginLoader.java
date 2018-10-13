package controller;

import java.nio.file.*;

public class PluginLoader extends ClassLoader{
	public Class load(String root_plugin_dir){
		Path cl_path;
		byte[] byte_arr;
		Object class_inst = null;
		Class<?> loaded_cl = null;

		try{
			cl_path = Paths.get(root_plugin_dir, "build", "classes", "main", root_plugin_dir + ".class");
			byte_arr = Files.readAllBytes(cl_path);
			loaded_cl = defineClass(null, byte_arr, 0, byte_arr.length);

			System.out.println("Plugin path: " + cl_path.toString());
		} catch(Exception e){
			e.printStackTrace();
		} finally{

		}

		return (loaded_cl != null) ? loaded_cl : null;
	}
}