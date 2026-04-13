package com.example.pr1_module5_daniiiiiil.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pr1_module5_daniiiiiil.data.repository.NoteRepositoryImpl
import com.example.pr1_module5_daniiiiiil.domain.model.Note
import com.example.pr1_module5_daniiiiiil.domain.usecase.DeleteNoteUseCase
import com.example.pr1_module5_daniiiiiil.domain.usecase.GetNotesUseCase
import com.example.pr1_module5_daniiiiiil.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryViewModel(
    private val getNotes: GetNotesUseCase,
    private val saveNote: SaveNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    val notes: StateFlow<List<Note>> = getNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    sealed class Screen {
        object List : Screen()
        data class Editor(val note: Note? = null) : Screen()
    }

    private val _screen = MutableStateFlow<Screen>(Screen.List)
    val screen: StateFlow<Screen> = _screen

    fun openNew() { _screen.value = Screen.Editor() }
    fun openEdit(note: Note) { _screen.value = Screen.Editor(note) }
    fun back() { _screen.value = Screen.List }

    fun save(title: String, body: String) {
        val editing = (_screen.value as? Screen.Editor)?.note
        viewModelScope.launch {
            saveNote(title, body, editing?.fileName)
            _screen.value = Screen.List
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch { deleteNote(note.fileName) }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = NoteRepositoryImpl(context)
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(
                GetNotesUseCase(repo),
                SaveNoteUseCase(repo),
                DeleteNoteUseCase(repo)
            ) as T
        }
    }
}