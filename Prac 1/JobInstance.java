import java.io.*;

public class JobInstance extends Job{
	private String output;

	public JobInstance(long interval){
		super(interval);
		super.name = "JobInstance";
	}

	public void run(){
		this.output = "Aquemini - Outkast";
		super.last_run_time = System.currentTimeMillis() / 1000;
	}

	public String get_name(){
		return super.name;
	}

	public String get_output(){
		String temp_output = null;

		if(this.output != null){
			temp_output = new String(this.output);
			this.output = null;
		}


		return temp_output;
	}
}