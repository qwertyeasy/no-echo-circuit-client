package com.qwertyeasy.no_echo_circuit_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NoechocircuitclientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoechocircuitclientTheme() {
                // тут нужно использовать Navigation библиотеку, которая управляет состоянием
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val loginViewModel: LoginViewModel = viewModel()
    Scaffold(
        content = { padding ->
            // общий контейнер из 2 блоков
            Column (modifier = Modifier
                .background(LightGrey)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UpperBlock(Modifier.weight(0.45f), padding)
                BottomBlock(loginViewModel, Modifier.weight(0.65f))
            }
        }
    )
}

@Composable
fun UpperBlock(modifier: Modifier, padding: PaddingValues){
    val textViewModel: TextAnimationViewModel = viewModel()
    val displayText by textViewModel.displayText

    Column(
        modifier = modifier
            .padding(0.dp, padding.calculateTopPadding(), 0.dp, 0.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(text = displayText,
            fontSize = 29.sp,
            softWrap = false,
            style = MaterialTheme.typography.bodyLarge.copy(
                shadow = Shadow(
                    color = BlackBack.copy(alpha = 0.7f),
                    offset = Offset(0f, 8f),
                    blurRadius = 10f
                )
            )
        )
    }
}

@Composable
fun BottomBlock(viewModel: LoginViewModel, modifier: Modifier){
    // централизованное управление весами блока ? надо??
    val sideVertSpace = 0.06f
    val vertInnerSpace = 0.85f
    Row(
        modifier = modifier.fillMaxSize()
            .background(BlackBack, shape = RoundedCornerShape(6))
    ) {
        Spacer(modifier = Modifier.weight(sideVertSpace))
        Column(modifier = Modifier.weight(vertInnerSpace)
        ) {
            // внутренние элементы таблички,
            // включая спейсеры сверху и снизу
            Spacer(modifier = Modifier.weight(0.25f))
            val lineWidth = 3.8.dp
            InnerTable(viewModel,
                modifier = Modifier.weight(0.7f)
                    .border(BorderStroke(color = NeonPurple, width = lineWidth)),
                lineWidth)
            Column(modifier = Modifier.weight(0.12f),
                verticalArrangement = Arrangement.Center
            ){
                Text("VER.0.0.1", color = NeonPurple, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
        Spacer(modifier = Modifier.weight(sideVertSpace))
    }
}

@Composable
fun InnerTable(viewModel: LoginViewModel, modifier: Modifier, lineWidth: Dp){

    Column (modifier = modifier){
        TableUpperRow(Modifier.weight(0.4f), lineWidth)

        Spacer(Modifier.height(lineWidth).background(NeonPurple).fillMaxWidth())

        NickInputBlock(viewModel, Modifier.weight(0.2f).fillMaxWidth())

        EnterButton(viewModel, Modifier.weight(0.4f))
    }
}

@Composable
fun TableUpperRow(modifier: Modifier, lineWidth: Dp){
    Row (modifier = modifier){
        Box(modifier = Modifier.weight(0.62f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("type the\nnickname", color = NeonPurple, fontSize = 30.sp) }
        Spacer(Modifier.width(lineWidth).background(NeonPurple).fillMaxHeight())
        Box(modifier = Modifier.weight(0.38f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.eye),
                modifier = Modifier.size(78.dp),
                contentDescription = "LoginScreenIcon",
                colorFilter = ColorFilter.tint(NeonPurple)
            )
        }
    }
}

@Composable
fun NickInputBlock(viewModel: LoginViewModel, modifier: Modifier){
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val nickname by viewModel.nickname.collectAsState()

    TextField(value = nickname, onValueChange = { viewModel.onNicknameChange(it) },
        modifier = modifier.height(IntrinsicSize.Min),
        singleLine = true,
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = NeonPurple,
            focusedContainerColor = BlackBack,
            unfocusedContainerColor = BlackBack
        ),
        visualTransformation = VisualTransformation { text ->
            TransformedText(
                AnnotatedString(text.text.uppercase()),
                OffsetMapping.Identity
            )
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
fun EnterButton(viewModel: LoginViewModel, modifier: Modifier){
    Button(
        onClick = { viewModel.onEnterClicked() },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = BlackBack,
            containerColor = NeonPurple
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text("ENTER→", fontSize = 75.sp, fontWeight = FontWeight.Black)
    }
}
