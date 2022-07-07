package com.example.wordle.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle.model.Wordle

class WordleViewModel : ViewModel() {

    private val _uiState = MutableLiveData<Wordle>()
    val uiState: LiveData<Wordle>
        get() = _uiState

    private val _count = MutableLiveData<Int>(0)
    val count: LiveData<Int> = _count

    init {
        val initState = Wordle()
        _uiState.value = initState
        _count.value = 0
    }

    private fun generateTargetWord() {


    }

    private fun lastAlphaIndex(list: List<String>): Int {
        var lastAlphaIndex = -1
        var c = 'A'
        while (c <= 'Z') {
            val cand = list.lastIndexOf(c.toString())
            lastAlphaIndex = maxOf(lastAlphaIndex, cand)
            c += 1
        }
        return lastAlphaIndex
    }

    fun onEnterKeyPress() {
        println("Enter key is pressed")
        val board = _uiState.value!!.board
        val notPresentKeys = _uiState.value!!.notPresentKeys
        var row = uiState.value!!.currentTryIndex

        val list = board[row]
        val lastAlphaIndex = lastAlphaIndex(list)
        // row is empty
        if (lastAlphaIndex != 4) {
            return
        }

        // row is filled
        val candidate = list.joinToString(separator = "").lowercase()
        println("list: $list");
        println("list candidate: $candidate")

        // is a word
        if (candidate in setOf("irate", "could", "would", "quick")) {
            // add each char not in answer to notPresentKeys

            // tick row
            row += 1
        }

        val newBoard = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                        j, element ->
                    element
                }

            }
        }

        _uiState.value = Wordle(
            row,
            emptySet<String>().toSet(),
            newBoard,
        )

    }

    fun onDelKeyPress() {
        val board = _uiState.value!!.board
        val notPresentKeys = _uiState.value!!.notPresentKeys
        var row = uiState.value!!.currentTryIndex

        val list = board[row]

        val lastAlphaIndex = lastAlphaIndex(list)
        // row is empty
        if (lastAlphaIndex == -1) {
            return
        }

        val newBoard = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                        j, element ->
                    if (j == lastAlphaIndex) "" else element
                }

            }
        }
        _uiState.value = Wordle(
            row,
            emptySet<String>().toSet(),
            newBoard,
        )

    }

    fun onKeyPress(key: String): Unit {
        val board = _uiState.value!!.board
        val notPresentKeys = _uiState.value!!.notPresentKeys
        var row = uiState.value!!.currentTryIndex

        val list = board[row]

        val lastAlphaIndex = lastAlphaIndex(list)
        // row is filled
        if (lastAlphaIndex == 4) {
            return
        }

        // first empty
        var col: Int = list.indexOf("")


        val newBoard = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                    j, element ->
                        if (j == col) key else element
                }

            }
        }

        val newNotPresentKeys = notPresentKeys.toMutableSet()

        println("new board: $newBoard; try: $row")

        _uiState.value = Wordle(
            row,
            emptySet<String>().toSet(),
            newBoard,
        )

        _count.value = count.value!! + 1


    }


}