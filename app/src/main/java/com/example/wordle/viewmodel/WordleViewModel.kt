package com.example.wordle.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle.model.Wordle
import com.example.wordle.model.WordleCell

class WordleViewModel : ViewModel() {

    private val _uiState = MutableLiveData<Wordle>()
    val uiState: LiveData<Wordle>
        get() = _uiState

    private val _count = MutableLiveData<Int>(0)
    val count: LiveData<Int> = _count

    private val _answer = MutableLiveData<String>("QUICK")
    val answer: LiveData<String> = _answer

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

        val list = board[row].map { element -> element.key }
        val lastAlphaIndex = lastAlphaIndex(list)
        // row is empty
        if (lastAlphaIndex != 4) {
            return
        }

        // row is filled
        val candidate = list.joinToString(separator = "")
        println("list: $list");
        println("list candidate: $candidate")

        // is not a word
        var lst = notPresentKeys.toMutableList()
        if (candidate !in setOf("IRATE", "COULD", "WOULD", "QUICK", "PLANT", "COUNT", "WHEAT", "QUAKE", "QUEEN")) {
            return
        }

        // add each char not in answer to notPresentKeys
        candidate.forEach { c ->
            if (c !in answer.value!!) {
                lst.add(c.toString())
            }
        }

        val newBoard = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                        j, element ->
                    // same position
                    if (element.key == answer.value!!.get(j).toString()) {
                        WordleCell(element.key, isKeyPresent = true, isKeyPlacementCorrect = true)
                    } else {
                        WordleCell(element.key, isKeyPresent = element.key in answer.value!!, isKeyPlacementCorrect = false)
                    }
                }

            }
        }

        // tick row
        row += 1


        _uiState.value = Wordle(
            row,
            lst.toSet(),
            newBoard,
        )

    }

    fun onDelKeyPress() {
        val board = _uiState.value!!.board
        val notPresentKeys = _uiState.value!!.notPresentKeys
        var row = uiState.value!!.currentTryIndex

        val list = board[row].map {
            element -> element.key
        }

        val lastAlphaIndex = lastAlphaIndex(list)
        // row is empty
        if (lastAlphaIndex == -1) {
            return
        }

        val newBoard: List<List<WordleCell>> = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                        j, element ->
                    if (j == lastAlphaIndex) {
                        WordleCell(
                            key = "",
                            isKeyPresent = false,
                            isKeyPlacementCorrect = false,
                        )
                    } else {
                        element.copy()
                    }
                }

            }
        }
        _uiState.value = Wordle(
            row,
            notPresentKeys.toSet(),
            newBoard,
        )

    }

    fun onKeyPress(key: String): Unit {
        val board = _uiState.value!!.board
        val notPresentKeys = _uiState.value!!.notPresentKeys
        var row = uiState.value!!.currentTryIndex

        val list = board[row].map { element -> element.key }

        val lastAlphaIndex = lastAlphaIndex(list)
        // row is filled
        if (lastAlphaIndex == 4) {
            return
        }

        // first empty
        var col: Int = list.indexOf("")


        val newBoard: List<List<WordleCell>> = board.mapIndexed { index, list ->
            if (index != row) {
                list
            } else {
                list.mapIndexed {
                    j, element ->
                        if (j == col) {
                            WordleCell(key = key, isKeyPresent =  false, isKeyPlacementCorrect = false)
                        } else {
                            element.copy()
                        }
                }

            }
        }

        val newNotPresentKeys = notPresentKeys.toMutableSet()

        println("new board: $newBoard; try: $row")

        _uiState.value = Wordle(
            row,
            notPresentKeys.toSet(),
            newBoard,
        )

        _count.value = count.value!! + 1


    }


}