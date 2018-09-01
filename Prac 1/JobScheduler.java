import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
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

	public static void main(String[] args) throws IOException{
		Timer task = new Timer();
		JobScheduler job_sch = new JobScheduler();

		job_sch.job_arrlist = new ArrayList<Job>();
		job_sch.log_arrlist = new ArrayList<String>();
		job_sch.sui_arrlist = new ArrayList<String>();

		job_sch.init_stream();
		job_sch.run_ui();
		job_sch.run_logger();

		if (job_sch.JOB_TEST)
			job_sch.job_arrlist.add(new JobInstance(job_sch.JOB_TEST_IV));

		task.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				Job job;
				String output;

				for(int i = 0; i < job_sch.job_arrlist.size(); i++){
					job = job_sch.job_arrlist.get(i);

					if((output = job.get_output()) != null){
						System.out.println("Getting Output from Job: " + job.get_name());

						synchronized(job_sch.mutex){
							job_sch.log_arrlist.add(output);
							job_sch.sui_arrlist.add(output);
						}
					}

					System.out.println((System.currentTimeMillis() / 1000) % job_sch.JOB_TEST_IV);

					if((System.currentTimeMillis() / 1000) % job.get_interval() == 0){
						new Thread(job).start();
					}
				}
			}
		}, 0, 1000);
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
								sui_arrlist.remove(i);
								// System.out.println("In Run_UI: " + sui_arrlist.size());
								// System.out.println("In Run_UI: " + sui_arrlist.remove(i));
								//print_ui();
							}
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

				while(true){
					try{
						System.out.println("Size of Logger Array List: " + log_arrlist.size());
						synchronized(mutex){
							for(int i = 0; i < log_arrlist.size(); i++){
								buff_w.write(log_arrlist.remove(i));
							}
							
							buff_w.flush();
						}

						Thread.sleep(500);
					} catch(InterruptedException int_e){
						int_e.printStackTrace();
					} catch(IOException ioe){
						ioe.printStackTrace();
					} finally{
						// close_stream();
					}
				}
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
			fos.close();
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