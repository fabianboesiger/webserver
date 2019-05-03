package jobs;

public class PreciseIntervalJob extends Thread {
	
	private int loopTime;
	private boolean paused;
	private long nextLoop;
	Runnable job;
	
	public PreciseIntervalJob(Runnable job, int loopTime) {
		this.loopTime = loopTime;
		this.job = job;
		
		nextLoop =  System.currentTimeMillis();
		paused = false;
		
		start();
	}
	
	@Override
	public void run() {
		
		while(!paused) {
			job.run();
			nextLoop += loopTime;
			try {
				sleep(Math.max(0, nextLoop - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}