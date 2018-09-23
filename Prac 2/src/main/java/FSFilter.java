package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.*;
import java.util.LinkedList;

public class FSFilter{
	private FSWindow fs_window;
	private LinkedList<String> file_name_list;
	private String search_path, search_filter;
	private BlockingQueue<QueueElement> file_queue;
	private Thread searcher_thread, filterer_thread;

	public FSFilter(String search_path, String search_filter, FSWindow fs_window){
		this.fs_window = fs_window;
		this.search_path = search_path;
		this.search_filter = search_filter;
		this.file_name_list = new LinkedList<String>();
		this.file_queue = new LinkedBlockingQueue<QueueElement>();

		searcher_thread = new Thread(new Runnable(){
			public void run(){
				search_path();
			}
		});

		filterer_thread = new Thread(new Runnable(){
			public void run(){
				filter_file();
			}
		});

		try{
			searcher_thread.start();
			filterer_thread.start();
			
			filterer_thread.join();

			for(String file_name_item : file_name_list){
				fs_window.addResult(file_name_item);
			}
		} catch(InterruptedException int_e){
			int_e.printStackTrace();
		}
	}

	private void search_path(){
		try{
			Files.walkFileTree(Paths.get(this.search_path), new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
					try{
						file_queue.put(new DataElement(file.toString()));
					} catch(InterruptedException int_e){
						int_e.printStackTrace();
					}

					return FileVisitResult.CONTINUE;
				}
			});

			file_queue.add(new TerminateElement());
		} catch(IOException ioe){
			fs_window.showError(ioe.getClass().getName() + ": " + ioe.getMessage());
		}
	}

	private void filter_file(){
		try{
			String file_name = "";
			QueueElement deq_element = file_queue.take();

			while((deq_element = file_queue.take()) instanceof DataElement){
				file_name = deq_element.get_file_name();
				
				if(file_name.contains(search_filter)){
					System.out.println(file_name);
					file_name_list.add(file_name);
				}
			}
		} catch(InterruptedException int_e){
			int_e.printStackTrace();
		}
	}
}