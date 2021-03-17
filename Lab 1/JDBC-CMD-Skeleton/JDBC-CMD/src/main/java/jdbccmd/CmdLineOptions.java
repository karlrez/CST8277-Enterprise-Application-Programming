/********************************************************************************************************
 * File: CmdLineOptions.java Course materials (21W) CST 8277
 * 
 * @date 2020 09
 * @author (original) Mike Norman
 */
package jdbccmd;

import picocli.CommandLine.Option;

/**
 * Helper class that holds annotated member fields that represent cmdLine args
 */
public class CmdLineOptions {

	protected static final String DASH = "-";
	protected static final String DASHDASH = DASH + DASH;
	protected static final String HELP_SHORTOPT = DASH + "h";
	protected static final String HELP_LONGOPT = DASHDASH + "help";
	protected static final String HELP_USAGE = "print this message";

	protected static final String USER_SHORTOPT = DASH + "u";
	protected static final String USER_LONGOPT = DASHDASH + "username";
	protected static final String USER_USAGE = "The DB username (default: ${DEFAULT-VALUE})";
	protected static final String USER_DEFAULT = "sa";

	protected static final String PASSW_SHORTOPT = DASH + "p";
	protected static final String PASSW_LONGOPT = DASHDASH + "password";
	protected static final String PASSW_USAGE = "The DB password (default: ${DEFAULT-VALUE})";
	protected static final String PASSW_DEFAULT = "password";

	protected static final String JDBCURL_OPT = DASH + "url";
	protected static final String JDBCURL_USAGE = "The DB URL";

	protected static final String GENCOUNT_SHORTOPT = DASH + "g";
	protected static final String GENCOUNT_LONGOPT = DASHDASH + "generate-count";
	protected static final String GENCOUNT_USAGE = "number of random customers to be generated (default: ${DEFAULT-VALUE})";

	@Option( names = { HELP_SHORTOPT, HELP_LONGOPT }, usageHelp = true, description = HELP_USAGE)
	public boolean helpRequested = false;

	@Option( names = { JDBCURL_OPT }, required = true, description = JDBCURL_USAGE)
	public String jdbcUrl;

	@Option( names = { USER_SHORTOPT, USER_LONGOPT, }, required = false, description = USER_USAGE)
	public String username = USER_DEFAULT;

	@Option( names = { PASSW_SHORTOPT, PASSW_LONGOPT }, description = PASSW_USAGE)
	public String password = PASSW_DEFAULT;

	@Option( names = { GENCOUNT_SHORTOPT, GENCOUNT_LONGOPT }, description = GENCOUNT_USAGE)
	public int count = 10;
}