/*****************************************************************
 * Course materials (21W) CST 8277
 * 
 * @author (original) Mike Norman
 */
package com.algonquincollege.cst8277.week3.labexercise2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>Description</b></br>
 * Helper class that demonstrates some New features from Java 10
 */
public class WhatIsNewJava10 {
	protected static final Class< ?> MY_KLASSNAME = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LoggerFactory.getLogger( MY_KLASSNAME);

	public static void main( String[] args) {

		//List::of method returns a unmodifiable list
		List< String> l1 = List.of( "one", "two", "three");
		logger.info( "l1 = {}", l1);

		// compiler can figure out l2 is really 'List<String>'
		// use 'var' for local variables
		var l2 = List.of( "four", "five", "six");

		// use 'var' in new 'enhanced' for-loops
		for ( var x : l2) {
			logger.info( "{}", x);
		}
		// use in old for-loops
		for ( var idx = 0; idx < l2.size(); idx++) {
			logger.info( "[index={}]{}", idx, l2.get( idx));
		}

		try ( var writer = new PrintWriter( new File( "welcome.txt"))) {
			writer.println( "welcome");
		} catch ( FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}

		//What happened when you try to call add?
		l1.add( "Test");
	}

}