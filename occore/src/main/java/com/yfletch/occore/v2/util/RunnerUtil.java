package com.yfletch.occore.v2.util;

import java.util.concurrent.Executors;

public class RunnerUtil
{
	/**
	 * Execute the runnable after _ms_ delay, on a new thread
	 */
	public static void delay(long ms, Runnable runnable)
	{
		final var service = Executors.newSingleThreadExecutor();
		service.execute(() -> {
			try
			{
				Thread.sleep(ms);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			runnable.run();
			service.shutdown();
		});
	}
}
