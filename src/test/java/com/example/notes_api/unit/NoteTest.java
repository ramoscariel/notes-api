package com.example.notes_api.unit;

import com.example.notes_api.entity.Note;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testConstructorAndGetters() {
        Note note = new Note("Test Title", "This is a description");

        assertEquals("Test Title", note.getName());
        assertEquals("This is a description", note.getDescription());
        assertNull(note.getId()); // ID should be null before persistence
    }

    @Test
    void testSetters() {
        Note note = new Note();
        note.setId("123");
        note.setName("Title");
        note.setDescription("Desc");

        assertEquals("123", note.getId());
        assertEquals("Title", note.getName());
        assertEquals("Desc", note.getDescription());
    }

    @Test
    void testValidationFailsWhenNameIsBlank() {
        Note note = new Note();
        note.setName("  ");  // Invalid: blank
        note.setDescription("Some description");

        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testValidationPassesWithValidName() {
        Note note = new Note("Valid Name", "Some description");

        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        assertTrue(violations.isEmpty());
    }
}
