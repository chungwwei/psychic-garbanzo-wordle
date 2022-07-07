package com.example.wordle.model

data class Wordle(
    val currentTryIndex: Int = 0,
    val notPresentKeys: Set<String> = emptySet(),
    val board: List<List<String>> = listOf(
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
        listOf("", "", "", "", ""),
    )
)