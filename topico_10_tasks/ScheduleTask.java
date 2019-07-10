package topico_10_tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * EXERCICIO 5 SLIDE 27
 * 
 * Fa ̧ca um programa que possibilite agendar uma tarefa para ser executadaem um hor ́ario espec ́ıfico.
 */

public class ScheduleTask {
	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 7);
		cal.set(Calendar.MINUTE, 46);

		long diffSecs = (cal.getTime().getTime() - new Date().getTime()) / 1000;
		ScheduledFuture future = executor.schedule(new Runnable(){
		
			@Override
			public void run() {
				System.out.println("BEEP " + new Date().toString());
			}
		}, diffSecs, TimeUnit.SECONDS);
		try {
			future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		executor.shutdown();
	}
}