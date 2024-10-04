package ru.mixail.libraryonboot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mixail.libraryonboot.models.Person;
import ru.mixail.libraryonboot.service.PeopleService;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        //Посмотреть есть ли человек с таким же fullName
        if (peopleService.getPersonByFullName(person.getFullName()).isPresent());
        errors.rejectValue("fullName", "", "This fullName is already taken");
    }
}
