package com.example.wordle.model

data class Wordle(
    val currentTryIndex: Int = 0,
    val notPresentKeys: Set<String> = emptySet(),
    val board: List<List<WordleCell>> =
        List(5) {
            List(5) {
                WordleCell(
                    key = "",
                    isKeyPresent = false,
                    isKeyPlacementCorrect = false,
                )
            }
        }
)

data class WordleCell (
    val key: String,
    val isKeyPresent: Boolean,
    val isKeyPlacementCorrect: Boolean,

)