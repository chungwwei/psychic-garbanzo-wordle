package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordle.model.Wordle
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordle.viewmodel.WordleViewModel

class MainActivity : ComponentActivity() {

    val KEY_ROW_ONE = listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
    val KEY_ROW_TWO = listOf("A", "S", "D","F", "G", "H", "J", "K", "L")
    val KEY_ROW_THREE= listOf("Z", "X", "C","V", "B", "N", "M")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WordleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,

                    ) {
                        BuildBoard()
                        BuildKeyboard(list1=KEY_ROW_ONE, list2=KEY_ROW_TWO, list3=KEY_ROW_THREE);
                    }

                }
            }
        }
    }
}

@Composable
fun BoardCell(char: String, color: Color) {
    Box(
        modifier = Modifier
            .size(width = 72.dp, height = 72.dp)
            .background(color)
            .padding(1.dp)
            .border(width = 2.dp, color = Color.Black),
        contentAlignment = Alignment.Center

    ){
        Text(text = char, textAlign = TextAlign.Center)
    }
}


@Composable
fun BuildBoard(
    worldViewModel: WordleViewModel = viewModel()
) {
    val uiState by worldViewModel.uiState.observeAsState(Wordle())
    val cnt by worldViewModel.count.observeAsState(initial = 0)
    val board = uiState.board
    val notPresentKeys = uiState.notPresentKeys

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("cnt is $cnt")
        board.map {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                it.map {
                    val isKeyPresent = it.isKeyPresent
                    val isPlacementCorrect = it.isKeyPlacementCorrect
                    val color = if (isKeyPresent && isPlacementCorrect) {
                        Color.Green
                    } else if (isKeyPresent && !isPlacementCorrect) {
                        Color.Yellow
                    } else {
                        Color.White
                    }
                    BoardCell(char = it.key, color = color)
                }
            }
        }
    }
}


@Composable
fun KeyButton(
    name: String,
    isGrayOut: Boolean,
    onKeyClick: (String) -> Unit
) {
    val color = if (isGrayOut) {
        Color.Gray
    } else {
        Color.Transparent
    }
    Button(
        modifier = Modifier
            .size(width = 36.dp, height = 48.dp)
            .fillMaxWidth()
            .padding(2.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = {
            onKeyClick(name)
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Text("$name", textAlign = TextAlign.Center)
    }
}

@Composable
fun BuildKeyboard(
    wordleViewModel: WordleViewModel = viewModel(),
    list1: List<String>,
    list2: List<String>,
    list3: List<String>
) {
    val uiState by wordleViewModel.uiState.observeAsState(Wordle())
    val board = uiState.board
    val notPresentKeys = uiState.notPresentKeys
    println("composekeys is $notPresentKeys")
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box{}
            list1.map { it ->
                val isGrayOut = notPresentKeys.contains(it)
                KeyButton(name = it, isGrayOut = isGrayOut, onKeyClick = {
                    wordleViewModel.onKeyPress(it)
                })
            }
            Box{}
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box{}
            list2.map {
                val isGrayOut = notPresentKeys.contains(it)
                KeyButton(name = it, isGrayOut = isGrayOut, onKeyClick = {
                    wordleViewModel.onKeyPress(it)
                })
            }
            Box {}
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box{}
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .padding(2.dp),
                onClick = {
                    wordleViewModel.onEnterKeyPress()
                }

            ){
                Text("Enter")
            }
            list3.map {
                val isGrayOut = notPresentKeys.contains(it)
                KeyButton(name = it, isGrayOut = isGrayOut, onKeyClick = {
                    wordleViewModel.onKeyPress(it)
                })
            }
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .padding(2.dp),
                onClick = {
                    wordleViewModel.onDelKeyPress()
                }){
                Text("Del")
            }
            Box{}
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WordleTheme {
        Column {
//            Greeting("android is kinda hard")
        }

    }
}