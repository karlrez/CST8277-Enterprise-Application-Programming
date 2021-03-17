/********************************************************************************************************
 * File: GenerateRandomCustomerRecords.java Course materials (21W) CST 8277
 * 
 * @date January 16, 2021
 * @author Shariar (Shawn) Emami
 * @date 2020 09
 * @author (original) Mike Norman
 */
package jdbccmd;

import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdbccmd.LoggingOutputStream.LogLevel;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParameterException;
import uk.co.jemos.podam.api.ClassInfoStrategy;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Helper class that generates random Customers and writes them to the database
 */
@Command(description = "Generate random people", name = "jdbccmd.GenerateRandomCustomerRecords")
public class GenerateRandomCustomerRecords {
	// get the current class type, in this case
	// "jdbccmd.GenerateRandomCustomerRecords"
	protected static final Class<?> MY_KLASSNAME = MethodHandles.lookup().lookupClass();
	// create a logger based on the type of the current class
	private static final Logger logger = LoggerFactory.getLogger(MY_KLASSNAME);

	// error message to be customized for CMD display
	protected static final String CMDLINE_PARSING_ERROR_MSG = "cmdLine parsing error: {}";
	// message to be customized for time taken to complete the current task.
	protected static final String ELAPSED_TIME_MSG = "Elapsed time = {} ms";
	// empty the table before adding new content to it.
	private static final String TRUNC_PERSON = "TRUNCATE TABLE databank.person";
	// insert statement for person table
	protected static final String INSERT_PERSON = "INSERT INTO databank.person(first_name,last_name,email,phone,created)VALUES(?,?,?,?,?)";

	public static void main(String[] args) {
		// create an instance of CmdLineOptions class which has the variables we need
		// from CMD.
		CmdLineOptions cmdLineOptions = new CmdLineOptions();
		// pass the instance of CmdLineOptions class to picocli.CommandLine to
		// automatically pass CMD arguments.
		CommandLine cmdLine = new CommandLine(cmdLineOptions);
		// pass the name of the class which is operating CommandLine instance.
		cmdLine.setCommandName(MY_KLASSNAME.getName());
		try {
			// parse incoming arguments
			cmdLine.parseArgs(args);
		} catch (ParameterException e) {
			// let user know the parsing has failed.
			logger.error(CMDLINE_PARSING_ERROR_MSG, e.getLocalizedMessage());
			logCmdLineUsage(e.getCommandLine(), LogLevel.ERROR);
			System.exit(-1);
		}
		if (cmdLineOptions.helpRequested) {
			// display help
			logCmdLineUsage(cmdLine, LogLevel.INFO);
		} else {
			// generate data for DB
			generateCustomers(cmdLineOptions.jdbcUrl, cmdLineOptions.username, cmdLineOptions.password,
					cmdLineOptions.count);
		}
	}

	public static void generateCustomers(String jdbcUrl, String username, String password, int genCount) {
		Instant startTime = Instant.now();

		// create a properties object to store the password and username
		Properties dbProps = new Properties();
		dbProps.put("user", username);
		dbProps.put("password", password);

		// TODO complete the try with resource.
		// TODO create a new connection using l and the properties object above.
		// TODO create a PreparedStatement for TRUNC_PERSON.
		// TODO create a PreparedStatement for INSERT_PERSON. when we insert we also
		// want the ability
		// to get the generated id back prepareStatement will need another argument
		// Statement.RETURN_GENERATED_KEYS.
		// TODO don't forget to execute the truncate statement first.
		try {
			Connection con = DriverManager.getConnection(jdbcUrl, dbProps);
			PreparedStatement pstmtTruncPerson = con.prepareStatement(TRUNC_PERSON);
			PreparedStatement pstmtInsert = con.prepareStatement(INSERT_PERSON, Statement.RETURN_GENERATED_KEYS);
			pstmtTruncPerson.execute();
			
			// PODAM - POjo DAta Mocker
			PodamFactory factory = new PodamFactoryImpl();
			ClassInfoStrategy classInfoStrategy = factory.getClassStrategy();
			// no need to generate primary key (id), database will do that for us
			((DefaultClassInfoStrategy) classInfoStrategy).addExcludedField(Person.class, "id");
			factory.getStrategy().addOrReplaceTypeManufacturer(String.class, new PersonManufacturer());
			for (int cnt = 0, numRandomCustomers = genCount; cnt < numRandomCustomers; cnt++) {
				Person randomCustomer = factory.manufacturePojoWithFullData(Person.class);
				// write randomCustomer to Database
				// setters are chosen based on the data type, setString, setInt, setDouble, etc
				// the number as first argument represent the order of the '?' in the
				// INSERT_PERSON query.
				pstmtInsert.setString(1, randomCustomer.getFirstName());
				pstmtInsert.setString(2, randomCustomer.getLastName());
				pstmtInsert.setString(3, randomCustomer.getEmail());
				pstmtInsert.setString(4, randomCustomer.getPhoneNumber());
				pstmtInsert.setLong(5, randomCustomer.getCreated().getTime());
				// execute the query, return true of successful
				pstmtInsert.execute();
				// get the generated keys from DB as the result of INSERT_PERSON query.
				// the ResultSet is AutoCloseable so it can be placed in a try-with-resource to
				// be auto closed.
				try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
					if (generatedKeys.next()) {// if a key is returned
						int id = generatedKeys.getInt(1);// get the key
						// set the key to the customer and print it
						randomCustomer.setId(id);
						logger.debug("created random customer \r\n\t{}", randomCustomer);
					} else {
						logger.error("could not retrieve generated Pk");
					}
				}
			}
			// catch any exceptions that might have been thrown from Connection,
			// PreparedStatement, and/or ResultSet
		} catch (SQLException e) {
			logger.error("something went wrong inserting new customer, ", e);
		}

		Instant endTime = Instant.now();
		long elapsedTime = Duration.between(startTime, endTime).toMillis();
		logger.info(ELAPSED_TIME_MSG, elapsedTime);
	}

	/**
	 * print the content of the LoggingOutputStream to the logger
	 * 
	 * @param cmdLine - command line instance
	 * @param los     - custom SLF4J instance
	 */
	protected static void logCmdLineUsage(CommandLine cmdLine, LogLevel level) {
		// create a new custom output stream for SLF4J to print to logger.
		LoggingOutputStream los = new LoggingOutputStream(logger, level);
		PrintWriter pw = new PrintWriter(los);
		cmdLine.usage(pw);
		pw.flush();
		los.line();
	}

}