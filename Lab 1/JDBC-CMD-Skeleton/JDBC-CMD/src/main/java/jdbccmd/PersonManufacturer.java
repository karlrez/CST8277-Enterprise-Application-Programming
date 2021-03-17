/****************************************************************
 * File: LoggingOutputStream.java Course materials (21W) CST 8277
 * 
 * @date January 16, 2021
 * @author Shariar (Shawn) Emami
 * @date 2020 09
 * @author (original) Mike Norman
 */
package jdbccmd;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.StringTypeManufacturerImpl;

/**
 * This class used the podam (POJO Data Mocker) library. Idea is to provide the needed data and this class randomize
 * data for the given Object.
 * 
 * @author Shariar (Shawn) Emami
 * @version Jan 18, 2021
 */
public class PersonManufacturer extends StringTypeManufacturerImpl {
	private static final Logger logger = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass());

	//name of the fields in the Person Class
	private static final String PHONENUMBER_FIELD = "phoneNumber";
	private static final String FIRSTNAME_FIELD = "firstName";
	private static final String LASTNAME_FIELD = "lastName";
	private static final String EMAIL_FIELD = "email";
	//name of the files with data in them
	protected static final String POOL_OF_FIRSTNAMES = "firstnamePool.txt";
	protected static final String POOL_OF_LASTNAMES = "lastnamePool.txt";
	//list of usefull digits and letters
	protected static final String ALPHA_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	protected static final String DIGITS = "1234567890";
	//random generator which is less predictable than Random class
	//https://stackoverflow.com/a/11052736/764951
	protected static SecureRandom rnd = new SecureRandom();
	//list of loaded data pool
	protected static List< String> poolOfFirstnames = new ArrayList<>();
	protected static List< String> poolOfLastnames = new ArrayList<>();

	//load the pool of data into the list
	static {
		ClassLoader theClassLoader = MethodHandles.lookup().lookupClass().getClassLoader();
		try ( InputStream firstnamePoolInputStream = theClassLoader.getResourceAsStream( POOL_OF_FIRSTNAMES);
				InputStream lastnamePoolInputStream = theClassLoader.getResourceAsStream( POOL_OF_LASTNAMES);
				Scanner firstnamePoolScanner = new Scanner( firstnamePoolInputStream);
				Scanner lasstnamePoolScanner = new Scanner( lastnamePoolInputStream);) {
			while ( firstnamePoolScanner.hasNext()) {
				poolOfFirstnames.add( firstnamePoolScanner.nextLine());
			}
			while ( lasstnamePoolScanner.hasNext()) {
				poolOfLastnames.add( lasstnamePoolScanner.nextLine());
			}
		} catch ( IOException e) {
			logger.error( "something went wrong building name pools: {}", e.getLocalizedMessage());
		}
	}

	//depending on the attribute name, create random data for that field and return it.
	@Override
	public String getType( DataProviderStrategy strategy, AttributeMetadata attributeMetadata,
			Map< String, Type> genericTypesArgumentsMap) {
		String stringType = "";
		if ( EMAIL_FIELD.equals( attributeMetadata.getAttributeName())) {
			//create random customer email
			StringBuilder sb = new StringBuilder();
			while ( sb.length() < 3) {
				int index = (int) ( rnd.nextFloat() * ALPHA_LETTERS.length());
				sb.append( ALPHA_LETTERS.charAt( index));
			}
			while ( sb.length() < 8) {
				int index = (int) ( rnd.nextFloat() * DIGITS.length());
				sb.append( DIGITS.charAt( index));
			}
			sb.append( "@gmail.com");
			stringType = sb.toString();
		} else if ( FIRSTNAME_FIELD.equals( attributeMetadata.getAttributeName())) {
			stringType = poolOfFirstnames.get( rnd.nextInt( poolOfFirstnames.size()));
		} else if ( LASTNAME_FIELD.equals( attributeMetadata.getAttributeName())) {
			stringType = poolOfLastnames.get( rnd.nextInt( poolOfLastnames.size()));
		} else if ( PHONENUMBER_FIELD.equals( attributeMetadata.getAttributeName())) {
			int npa = rnd.nextInt( 643) + 100;
			int extension = rnd.nextInt( 9000) + 1000;
			stringType = String.format( "613%03d%04d", npa, extension);
		} else {
			stringType = super.getType( strategy, attributeMetadata, genericTypesArgumentsMap);
		}
		return stringType;
	}

}