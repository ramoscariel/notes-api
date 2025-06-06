package com.example.notes_api.service;

import com.example.notes_api.entity.Note;
import com.example.notes_api.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return (List<Note>) noteRepository.findAll();
    }

    public Optional<Note> getNoteById(String id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(String id, Note noteDetails) {
        Optional<Note> existingNote = noteRepository.findById(id);
        if (existingNote.isPresent()) {
            Note note = existingNote.get();
            note.setName(noteDetails.getName());
            note.setDescription(noteDetails.getDescription());
            return noteRepository.save(note);
        }
        throw new RuntimeException("Note not found with id: " + id);
    }

    public void deleteNote(String id) {
        noteRepository.deleteById(id);
    }
}