package com.jmd.life;

public class LifeThread extends Thread {
	public static final long DEFAULT_TICK_DELAY = 100;
	public static final long FASTEST_TICK_DELAY = 17;
	private Life life;
	private long tickDelayInMillis;

	private boolean paused;
	private boolean running;

	public LifeThread(Life life, long tickDelayInMillis) {
		this.life = life;
		this.tickDelayInMillis = tickDelayInMillis;
	}

	public LifeThread(Life life) {
		this(life, DEFAULT_TICK_DELAY);
	}

	@Override
	public void run() {
		paused = false;
		running = true;
		while (running) {
			try {
				Thread.sleep(tickDelayInMillis);
			} catch (InterruptedException e) {
				System.out.println("Interrupted...");
				if (!running) {
					System.out.println("Thread Exiting...");
					break;
				}
			}

			if (!paused) {
				life.tick();
			}
		}
	}

	public void quit() {
		running = false;

	}

	public void setPaused(boolean value) {
		System.out.println("Setting paused to " + value);
		this.paused = value;
	}

	public boolean getPaused() {
		return this.paused;
	}

	public void setTickDelay(long ms) {
		tickDelayInMillis = ms;
		System.out.println("Set Tick Delay to " + tickDelayInMillis);
	}

	public long getTickDelayInMillis() {
		return tickDelayInMillis;
	}

}
