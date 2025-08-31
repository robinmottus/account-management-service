package accountManagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNr, ConstraintValidatorContext context) {
        if (phoneNr == null || phoneNr.isBlank())
        {
            return true;
        }
        String cleanedNr = phoneNr.replaceAll("[\\s\\-()]", "");
        return cleanedNr.matches("^\\+?372[5][0-9]{7}$") || cleanedNr.matches("^[5][0-9]{7}$");
    }
}
