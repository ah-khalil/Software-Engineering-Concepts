package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.*;

public class FSFilter{
	private FSWindow fs_window;
	private BlockingQueue<QueueElement> file_queue;
	private String search_path, search_filter;
	private Thread searcher_thread, filterer_thread;

	public FSFilter(String search_path, String search_filter, FSWindow fs_window){
		this.fs_window = fs_window;
		this.search_path = search_path;
		this.search_filter = search_filter;
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

		searcher_thread.start();
		filterer_thread.start();
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

			while(deq_element instanceof DataElement){
				file_name = deq_element.get_file_name();
				
				if(file_name.contains(search_filter))
					fs_window.addResult(file_name);
			}
		} catch(InterruptedException int_e){
			int_e.printStackTrace();
		}
	}
}