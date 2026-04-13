package com.example.pr1_module5_daniiiiiil.domain.usecase

import com.example.pr1_module5_daniiiiiil.domain.model.Note
import com.example.pr1_module5_daniiiiiil.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repo: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repo.getAllNotes()
}

class SaveNoteUseCase(private val repo: NoteRepository) {
    suspend operator fun invoke(title: String, body: String, existingFileName: String? = null) {
        val note = Note(
            title = title.ifBlank { "Без названия" },
            body = body
        )
        repo.saveNote(note, existingFileName)
    }
}

class DeleteNoteUseCase(private val repo: NoteRepository) {
    suspend operator fun invoke(fileName: String) = repo.deleteNote(fileName)
}