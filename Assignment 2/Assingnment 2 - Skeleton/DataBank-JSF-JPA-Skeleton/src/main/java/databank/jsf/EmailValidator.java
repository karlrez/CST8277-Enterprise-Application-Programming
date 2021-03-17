/*****************************************************************
 * File: PersonPojo.java Course materials (21W) CST8277
 *
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator( "emailValidator")
public class EmailValidator implements Validator< String> {

	// really really (!) simple email pattern: at-least-1-letter, '@', at-least-1-letter
	private static final Pattern EMAIL_PATTERN = Pattern.compile( "^(.+)@(.+)$");

	@Override
	public void validate( FacesContext context, UIComponent component, String value) throws ValidatorException {

		if ( value == null) {
			FacesMessage msg = new FacesMessage( "Email should not be empty", "Invalid Email format.");
			msg.setSeverity( FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException( msg);
		}
		//TODO 07 - complete the Matching using the EMAIL_PATTERN above.
		//you can use methods matcher and matches from EMAIL_PATTERN.
		//if match fails, create a new FacesMessage(String,String) object.
		//use proper error messages.
		//set the Severity of your FacesMessage to FacesMessage.SEVERITY_ERROR.
		//Finally, throw an exception with the FacesMessage in it.
		//to know what exception should be thrown, look at the signature of this method.
		if (!EMAIL_PATTERN.matcher(value).matches()) {
			FacesMessage msg = new FacesMessage("This is not an email", "Invalid email format");
			throw new ValidatorException(msg);
		}
	}

}