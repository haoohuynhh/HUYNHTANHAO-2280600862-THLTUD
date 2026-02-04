package huynhtanhao.hutech.haohuynh.validators;

import huynhtanhao.hutech.haohuynh.services.UserService;
import huynhtanhao.hutech.haohuynh.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true;
        }
        return userService.findByUsername(username).isEmpty();
    }
}
