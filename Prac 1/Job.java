import java.io.*;

public abstract class Job implements Runnable{
	protected String name;
	protected long interval;
	protected long last_run_time = System.currentTimeMillis() / 1000;

	public Job(long interval){
		this.interval = interval;
	}

	public long get_interval(){
		return this.interval;
	}

	public long get_last_run_time(){
		return this.last_run_time;
	}
	public abstract void run();

	public abstract String get_name();
	public abstract String get_output();
}