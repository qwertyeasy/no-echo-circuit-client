package com.qwertyeasy.no_echo_circuit_client.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.R
import com.qwertyeasy.no_echo_circuit_client.components.HorizontalLine
import com.qwertyeasy.no_echo_circuit_client.components.prepareBorder
import com.qwertyeasy.no_echo_circuit_client.components.TableTextField
import com.qwertyeasy.no_echo_circuit_client.components.TitleButton
import com.qwertyeasy.no_echo_circuit_client.components.VerticalLine
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun LoginScreen(rootViewModel: RootViewModel, onSuccessLogin: () -> Unit){
    val loginViewModel: LoginViewModel = viewModel()

    val isLogin by rootViewModel.isLogin.collectAsState()
    if(isLogin){ onSuccessLogin() }

    Scaffold(
        content = { padding ->
            Column (modifier = Modifier
                .background(LightGrey)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UpperBlock(Modifier.weight(0.45f), padding)
                BottomBlock(
                    rootViewModel, loginViewModel, Modifier.weight(0.65f)
                )
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
                ),
                color = BlackBack
            )
        )
    }
}

@Composable
fun BottomBlock(
    rootViewModel: RootViewModel, viewModel: LoginViewModel, modifier: Modifier
){
    Row(
        modifier
            .fillMaxSize()
            .background(BlackBack, shape = RoundedCornerShape(6))
    ) {
        Spacer(Modifier.weight(0.06f))
        Column(Modifier.weight(0.85f)
        ) {
            Spacer(Modifier.weight(0.25f))

            InnerTable(rootViewModel, viewModel,
                modifier = Modifier
                    .weight(0.7f)
                    .border(prepareBorder(color = NeonPurple)))
            Column(modifier = Modifier.weight(0.12f),
                verticalArrangement = Arrangement.Center
            ){
                Text("VER.0.0.1", color = NeonPurple, fontSize = 22.sp)
            }
            Spacer(Modifier.weight(0.2f))
        }
        Spacer(Modifier.weight(0.06f))
    }
}

@Composable
fun InnerTable(
    rootViewModel: RootViewModel, viewModel: LoginViewModel, modifier: Modifier
){
    Column (modifier = modifier){
        TableUpperRow(Modifier.weight(0.4f))

        HorizontalLine(NeonPurple)

        NickInputBlock(viewModel, Modifier
            .weight(0.2f)
            .fillMaxWidth())

        TitleButton("ENTER→", Modifier.weight(0.4f), BlackBack,
            NeonPurple, {
                viewModel.onEnterClicked(rootViewModel)
            }
        )
    }
}

@Composable
fun TableUpperRow(modifier: Modifier){
    Row (modifier = modifier){
        Box(modifier = Modifier
            .weight(0.62f)
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("type the\nnickname", color = NeonPurple, fontSize = 30.sp)
        }
        VerticalLine(NeonPurple)
        Box(modifier = Modifier
            .weight(0.38f)
            .fillMaxSize(),
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
    val nickname by viewModel.nickname.collectAsState()

    Box(modifier = modifier) {
        TableTextField(
            nickname, BlackBack, NeonPurple,
            { viewModel.onNicknameChange(it) }
        )
    }
}