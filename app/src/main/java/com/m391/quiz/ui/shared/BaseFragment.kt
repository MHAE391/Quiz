package com.m391.quiz.ui.shared

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.m391.quiz.database.remote.RemoteDatabase

abstract class BaseFragment : Fragment() {
    abstract val viewModel: BaseViewModel
    protected val remoteDatabase = RemoteDatabase()
}