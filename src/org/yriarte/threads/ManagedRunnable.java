package org.yriarte.threads;

public interface ManagedRunnable extends Runnable {
	/** @brief a call requestStop makes the ManagedRunnable 
	 * exit the run function ASAP.
	 */
	public void requestStop();
}
