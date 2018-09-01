import java.io.*;
import java.util.ArrayList;

public class SchedulerUI implements Runnable{
	private volatile ArrayList<String> sui_list;

	public SchedulerUI(){
		this.sui_list = new ArrayList<String>();
	}

	public void print_ui(){
		System.out.println("Enter Task: ");
	}

	public void print_message(String message){
		System.out.println(message);
	}

	public void run(){

	}
}