/*****************************************************************
 * File: GenerateNoMemoryLeak.java Course materials (21W) CST 8277
 * 
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * 
 * @date 2020 10
 * @author (original) Mike Norman
 */
package com.algonquincollege.cst8277.week10.labexercise3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * <b>Description</b></br>
 * </br>
 * Class that experiences an Out-of-Memory exception
 *
 */
public class GenerateNoMemoryLeak {

	static BlockingQueue< byte[]> queue = new LinkedBlockingQueue<>();

	public static void main( String[] args) {

		Runnable producer = () -> {
			while ( true) {
				// generates 1Mb of raw (empty) data every 10ms
				queue.offer( new byte[ 1 * 1024 * 1024]);
				try {
					TimeUnit.MILLISECONDS.sleep( 10);
				} catch ( InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable consumer = () -> {
			while ( true) {
				try {
					// Consumers are slower: process every 100ms
					queue.take();
					TimeUnit.MILLISECONDS.sleep( 10);
				} catch ( InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		// give each thread a recognizable name, helps profiling
		//TODO replace "Producer Thread" with your name and student number, ex. "Shawn-Emami-0123456" 
		new Thread( producer, "Karl-Rezansoff-040955782").start();
		new Thread( consumer, "Consumer Thread").start();
	}

}