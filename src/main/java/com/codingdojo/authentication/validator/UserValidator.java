package com.codingdojo.authentication.validator;

import com.codingdojo.authentication.models.User;
import org.springframework.validation.Validator;//Acordarme que me puso otra validacion de jakarta, era esta la que habia que usar
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return User.class.equals(clazz);
    }

   @Override
    public void validate(Object target, Errors errors){
        User user = (User) target;

        if (!user.getPasswordConfirmation().equals(user.getPassword())) {

            errors.rejectValue("passwordConfirmation", "Match");
        }
    }
}
