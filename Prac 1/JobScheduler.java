import java.io.*;
import java.util.Timer;
import java.util.ArrayList;

public class JobScheduler{
	private FileWriter fos = null;
	private BufferedWriter buff_w = null;

	private Object mutex = new Object();

	private ArrayList<Job> job_arrlist;
	private ArrayList<String> log_arrlist;
	private ArrayList<String> sui_arrlist;

	private final boolean JOB_TEST = true;
	private final long JOB_TEST_IV = 2;

	public static void main(String[] args){
		Job job;
		long start_time;
		long prevs_time = -1;
		long currn_time;
		String job_output;
		JobScheduler job_sch = new JobScheduler();

		start_time = System.currentTimeMillis();
		job_sch.job_arrlist = new ArrayList<Job>();
		job_sch.log_arrlist = new ArrayList<String>();
		job_sch.sui_arrlist = new ArrayList<String>();

		job_sch.init_stream();
		job_sch.run_ui();
		job_sch.run_logger();

		if (job_sch.JOB_TEST)
			job_sch.job_arrlist.add(new JobInstance(job_sch.JOB_TEST_IV));

		while(true){
			for(int i = 0; i < job_sch.job_arrlist.size(); i++){
				job = job_sch.job_arrlist.get(i);
				job_output = job.get_output();

				if(job_output != null){
					synchronized(job_sch.mutex){
						job_sch.log_arrlist.add(job_output);
						job_sch.sui_arrlist.add(job_output);
					}
				}

				currn_time = ((System.currentTimeMillis() - start_time) / 1000);

				if(currn_time != prevs_time)
					System.out.println(currn_time);

				prevs_time = currn_time;

				if((System.currentTimeMillis() / 1000) % job.get_interval() == 0){
					job_sch.run_job(job);
				}
			}
		}
	}

	public void run_ui(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				print_ui();

				while(true){
					try{
						synchronized(mutex){
							for(int i = 0; i < sui_arrlist.size(); i++){
								System.out.println(sui_arrlist.size());
								System.out.println(sui_arrlist.remove(i));
							}

							print_ui();
						}

						Thread.sleep(500);
					} catch(InterruptedException int_e){
						int_e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void run_logger(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					synchronized(mutex){
						for(int i = 0; i < log_arrlist.size(); i++){
							System.out.println(log_arrlist.size());
							buff_w.write(log_arrlist.remove(i));
						}
					}

					Thread.sleep(500);
				} catch(InterruptedException int_e){
					int_e.printStackTrace();
				} catch(IOException ioe){
					ioe.printStackTrace();
					close_stream();
				}
			}
		}).start();
	}

	public void run_job(Job job){
		new Thread(new Runnable(){
			@Override
			public void run(){
				job.run();
			}
		}).start();
	}

	public void init_stream(){
		try{
			fos = new FileWriter("cron.log");
			buff_w = new BufferedWriter(fos);
		} catch(IOException ioe){
			ioe.printStackTrace();
			close_stream();
		}
	}

	public void close_stream(){
		try{
			buff_w.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		} finally{
			System.out.println("Stream was unable to be closed");
		}
	}

	public void print_ui(){
		System.out.println("Enter Task: ");
	}
}