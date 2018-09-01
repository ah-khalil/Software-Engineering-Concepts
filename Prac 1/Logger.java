import java.io.*;
import java.util.Timer;
import java.util.ArrayList;

public class Logger implements Runnable{
	
	private volatile ArrayList<String> log_list;

	public Logger() throws IOException{
		this.log_list = new ArrayList<String>();
		this.fos = new FileWriter("cron.log");
		this.buff_w = new BufferedWriter(fos);
	}



	public void close_stream(){

	}
}