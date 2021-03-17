/*****************************************************************
 * Course materials (21W) CST 8277
 * 
 * @author (original) Mike Norman
 */
package com.algonquincollege.cst8277.week3.labexercise2;

public interface ImAnInterfaceWithNewDefaultMethods {

	/* public abstract */ String generateReport( ReportType reportType, String reportData);

	//Java 9 private interface methods can be static. 
	//Private methods on interfaces are not inherited by sub-interfaces or implementations.
	private static String privateCommonReportFormatting( ReportType reportType, String reportData) {
		// some formatting that is common to all reports, no matter the reportType
		return reportData.trim();
	}

	// all Java classes that implement ImAnInterfaceWithNewDefaultMethods
	// get the following default implementation of doReport
	default /* public */ String doReport( String reportCode, String rawReportData) {
		if ( null == reportCode) {
			throw new NullPointerException( "report type cannot be null");
		}
		// by default, reports are generated from cached data
		ReportType reportType = ReportType.Cached_Report;
		return generateReport( reportType, privateCommonReportFormatting( reportType, rawReportData));
	}
}