/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.constraints;

import fr.trendev.bean.MyBeanJSF;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author jsie
 */
public class MyMaxValidator implements ConstraintValidator<MyMax, Long> {

    private long bound;

    @Override
    public void initialize(MyMax constraintAnnotation) {
        bound = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return !(value > MyBeanJSF.bound);
    }

}
