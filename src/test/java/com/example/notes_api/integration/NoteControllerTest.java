package com.example.notes_api.integration;

import com.example.notes_api.controller.NoteController;
import com.example.notes_api.entity.Note;
import com.example.notes_api.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Note sampleNote;

    @BeforeEach
    void setup() {
        sampleNote = new Note("Sample Title", "Sample Description");
        sampleNote.setId("123");
    }

    @Test
    void testGetAllNotes() throws Exception {
        Mockito.when(noteService.getAllNotes()).thenReturn(List.of(sampleNote));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].name").value("Sample Title"));
    }

    @Test
    void testGetNoteById_Found() throws Exception {
        Mockito.when(noteService.getNoteById("123")).thenReturn(Optional.of(sampleNote));

        mockMvc.perform(get("/api/notes/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Sample Title"));
    }

    @Test
    void testGetNoteById_NotFound() throws Exception {
        Mockito.when(noteService.getNoteById("404")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notes/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNote() throws Exception {
        Mockito.when(noteService.createNote(any(Note.class))).thenReturn(sampleNote);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleNote)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Sample Title"));
    }

    @Test
    void testUpdateNote_Success() throws Exception {
        Note updated = new Note("Updated Title", "Updated Desc");
        updated.setId("123");

        Mockito.when(noteService.updateNote(eq("123"), any(Note.class))).thenReturn(updated);

        mockMvc.perform(put("/api/notes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Title"));
    }

    @Test
    void testUpdateNote_NotFound() throws Exception {
        Mockito.when(noteService.updateNote(eq("404"), any(Note.class)))
                .thenThrow(new RuntimeException("Note not found"));

        mockMvc.perform(put("/api/notes/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleNote)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/api/notes/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/api/notes/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola mundo!"));
    }
}
