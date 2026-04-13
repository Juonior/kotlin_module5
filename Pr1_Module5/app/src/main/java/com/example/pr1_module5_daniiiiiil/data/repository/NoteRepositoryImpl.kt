package com.example.pr1_module5_daniiiiiil.data.repository

import android.content.Context
import com.example.pr1_module5_daniiiiiil.domain.model.Note
import com.example.pr1_module5_daniiiiiil.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

class NoteRepositoryImpl(private val context: Context) : NoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    init { loadAll() }

    private fun loadAll() {
        val files = context.filesDir.listFiles()
            ?.filter { it.isFile && it.extension == "txt" }
            ?: emptyList()
        _notes.value = files.map { parseFile(it) }.sortedByDescending { it.createdAt }
    }

    override fun getAllNotes(): Flow<List<Note>> = _notes.asStateFlow()

    override suspend fun saveNote(note: Note, existingFileName: String?) {
        withContext(Dispatchers.IO) {
            val fileName = existingFileName ?: "${System.currentTimeMillis()}.txt"
            val file = File(context.filesDir, fileName)
            file.writeText("${note.title}\n${note.body}")
            val saved = parseFile(file)
            _notes.value = if (existingFileName == null) {
                listOf(saved) + _notes.value
            } else {
                _notes.value.map { if (it.fileName == fileName) saved else it }
            }
        }
    }

    override suspend fun deleteNote(fileName: String) {
        withContext(Dispatchers.IO) {
            File(context.filesDir, fileName).delete()
            _notes.value = _notes.value.filter { it.fileName != fileName }
        }
    }

    private fun parseFile(file: File): Note {
        val lines = file.readText().lines()
        val title = lines.firstOrNull()?.takeIf { it.isNotBlank() } ?: "Без названия"
        val body = lines.drop(1).joinToString("\n")
        return Note(
            fileName = file.name,
            title = title,
            body = body,
            createdAt = file.lastModified()
        )
    }
}