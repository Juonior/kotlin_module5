package com.example.pr1_module5_daniiiiiil.domain.repository

import com.example.pr1_module5_daniiiiiil.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun saveNote(note: Note, existingFileName: String? = null)
    suspend fun deleteNote(fileName: String)
}