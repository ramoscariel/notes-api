package com.example.notes_api.repository;

import com.example.notes_api.entity.Note;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface NoteRepository extends CrudRepository<Note, String> {
    List<Note> findByName(String name);
}