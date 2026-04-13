package com.example.pr1_module5_daniiiiiil.domain.model

data class Note(
    val fileName: String = "",
    val title: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)
