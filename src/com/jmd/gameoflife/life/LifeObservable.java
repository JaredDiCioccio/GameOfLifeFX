package com.jmd.gameoflife.life;

public interface LifeObservable {

	public void addObserver(LifeObserver observer);

	public void stopObserving(LifeObserver observer);

}
