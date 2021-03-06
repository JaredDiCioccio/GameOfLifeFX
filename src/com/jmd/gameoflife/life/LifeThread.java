package com.jmd.gameoflife.life;

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
		setDaemon(true); 
	}

	public LifeThread(Life life) {
		this(life, DEFAULT_TICK_DELAY);
	}

	@Override
	public void run() {
		paused = false;
		running = true;
		while (isRunning()) {
			try {
				Thread.sleep(tickDelayInMillis);
			} catch (InterruptedException e) {
				System.out.println("Interrupted...");
				if (!isRunning()) {
					System.out.println("Thread Exiting...");
					break;
				}
				Thread.currentThread().interrupt();
			}

			if (!isPaused()) {
				life.tick();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isPaused() {
		return paused;
	}

	public void quit() {
		running = false;

	}

	public void setPaused(boolean value) {
		this.paused = value;
	}

	public boolean getPaused() {
		return this.paused;
	}

	public void setTickDelay(long ms) {
		tickDelayInMillis = ms;
	}

	public long getTickDelayInMillis() {
		return tickDelayInMillis;
	}

}
