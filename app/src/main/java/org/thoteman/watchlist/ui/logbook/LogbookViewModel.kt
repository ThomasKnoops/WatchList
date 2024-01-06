package org.thoteman.watchlist.ui.logbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogbookViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is logbook Fragment"
    }
    val text: LiveData<String> = _text
}