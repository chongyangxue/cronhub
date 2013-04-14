package com.cronhub.cron4j;

import org.junit.Test;

import it.sauronsoftware.cron4j.Scheduler;

public class testCron4j {
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Scheduler s = new Scheduler();
			// Schedule a once-a-minute task.
			s.schedule("*/1 * * * *", new Runnable() {
				public void run() {
					System.out.println("Another minute ticked away...");
				}
			});
			s.start();
		}
	};
	
	public static void main(String[] args) {
		testCron4j cron = new testCron4j();
		Thread thread = new Thread(cron.runnable);
		thread.setDaemon(true);
		thread.start();
		try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
}