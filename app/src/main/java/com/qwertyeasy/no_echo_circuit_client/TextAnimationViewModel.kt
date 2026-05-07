package com.qwertyeasy.no_echo_circuit_client

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class TextAnimationViewModel: ViewModel() {

    val animationDelay = 200L
    val cursorDelay = 100L

    val width = 25
    private val height = 20
    private val length = width*height

    private val name1 = ".NO-ECHO.#."
    private val name2 = ".CIRCUIT."

    private val startName1 = width * 8 + 3
    private val startName2 = width * 9 + 10
    private val startName1End = startName1 + name1.length
    private val startName2End = startName2 + name2.length

    private val widthIndex = 13
    private val symbols = "ABCDEFGHYJKLMNOPQRSTUVWXYZ"
    private val chars = "01"

    private val animationThreshold = 15

    private val indexSet = mutableSetOf<Int>()

    val matrixChars = prepareArray()
    val displayText = mutableStateOf(AnnotatedString(""))

    init{
        startAnimate()
    }

    private fun prepareArray(): CharArray{
        val array = CharArray(length, { '.' })
        addRandomStrings(array)
        addRandomSymbols(array)
        addName(array)
        return array
    }

    private fun addRandomStrings(array: CharArray){
        val linesCount = Random.nextInt(3, 5)
        for (i in 1..linesCount){
            for (j in widthIndex..< width){
                array[i * width + j] = symbols.elementAt(Random.nextInt(26))
            }
        }
        for (j in widthIndex+4..< width){
            array[(linesCount+1) * width + j] = symbols.elementAt(Random.nextInt(26))
        }
        for (j in 0..< 4){
            array[(linesCount+2) * width + j] = symbols.elementAt(Random.nextInt(26))
        }
    }

    private fun addRandomSymbols(array: CharArray){
        (0..Random.nextInt(10, 18)).forEach { _ ->
            array[Random.nextInt(length)] = chars[Random.nextInt(2)]
        }
    }

    private fun addName(array: CharArray){
        for(i in 0..< name1.length){
            val index = startName1 + i
            array[index] = name1.elementAt(i)
        }
        for(i in 0..< name2.length){
            val index = startName2 + i
            array[index] = name2.elementAt(i)
        }
    }

    var removeIndexes = 0

    private fun startAnimate(){
        viewModelScope.launch {
            while(true){
                if(removeIndexes > 0){
                    val indexToRemove = indexSet.elementAt(Random.nextInt(indexSet.size))
                    indexSet.remove(indexToRemove)

                    matrixChars[indexToRemove] = ' '
                    rebuildString()
                    delay(cursorDelay)
                    matrixChars[indexToRemove] = '.'
                    rebuildString()
                }
                if(indexSet.size < animationThreshold) {
                    val index = Random.nextInt(length)
                    if(isChangeable(index)){
                        indexSet.add(index)

                        matrixChars[index] = ' '
                        rebuildString()
                        delay(cursorDelay)
                        matrixChars[index] = chars[Random.nextInt(2)]
                        rebuildString()
                    }
                } else {
                    removeIndexes = Random.nextInt(1,5)
                }
                delay(animationDelay)
            }
        }
    }

    private fun isChangeable(index: Int): Boolean{
        val remain = index % width
        if(index in startName1..startName1End ||
           index in startName2..startName2End ||
           remain < 1 || remain > width-2) {
            return false
        }
        return true
    }

    private fun rebuildString(){
        val builder = AnnotatedString.Builder()
            for(i in matrixChars.indices){
                if(matrixChars[i] == ' '){
                    builder.pushStyle(SpanStyle(background = BlackBack))
                    builder.append(matrixChars[i])
                    builder.pop()
                    continue
                }
                builder.append(matrixChars[i])
                if((i+1) % width == 0){
                    builder.append("\n")
                }
            }
        displayText.value = builder.toAnnotatedString()
    }
}