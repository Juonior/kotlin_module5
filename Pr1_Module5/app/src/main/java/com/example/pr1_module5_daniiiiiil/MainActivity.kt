package com.example.pr1_module5_daniiiiiil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pr1_module5_daniiiiiil.presentation.ui.theme.EditorScreen
import com.example.pr1_module5_daniiiiiil.presentation.ui.theme.ListScreen
import com.example.pr1_module5_daniiiiiil.presentation.viewmodel.DiaryViewModel

class MainActivity : ComponentActivity() {
    private val vm: DiaryViewModel by viewModels { DiaryViewModel.Factory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val screen by vm.screen.collectAsState()
                when (val s = screen) {
                    is DiaryViewModel.Screen.List   -> ListScreen(vm)
                    is DiaryViewModel.Screen.Editor -> EditorScreen(vm, s.note)
                }
            }
        }
    }
}