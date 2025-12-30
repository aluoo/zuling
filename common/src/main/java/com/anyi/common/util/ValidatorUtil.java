package com.anyi.common.util;

import com.anyi.common.advice.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/23
 */
public class ValidatorUtil {
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(false).buildValidatorFactory().getValidator();

    public static <T> void validateBean(T t, Class<?>... groups) {
        ValidatorResult result = new ValidatorResult();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        boolean hasError = violationSet != null && !violationSet.isEmpty();
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        if (result.getHasErrors()) {
            String error = result.getError();
            throw new BusinessException(99999,"非法参数: " + error);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ValidatorResult {
        private Boolean hasErrors;
        private List<ErrorMessage> errors = new ArrayList<>();

        public List<ErrorMessage> getAllErrors() {return errors;}

        public String getError() {
            if (errors == null || errors.isEmpty()) {
                return "";
            }
            ErrorMessage msg = errors.get(0);
            return msg.getMessage();
        }

        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            for (ErrorMessage error : errors) {
                sb.append(error.getPropertyPath()).append(":").append(error.getMessage()).append(" ");
            }
            return sb.toString();
        }

        public void addError(String propertyName, String message) {
            this.errors.add(new ErrorMessage(propertyName, message));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ErrorMessage {
        private String propertyPath;
        private String message;
    }
}