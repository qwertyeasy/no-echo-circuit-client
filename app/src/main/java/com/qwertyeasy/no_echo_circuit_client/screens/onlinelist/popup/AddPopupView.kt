package com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.qwertyeasy.no_echo_circuit_client.R
import com.qwertyeasy.no_echo_circuit_client.components.DismissSpacer
import com.qwertyeasy.no_echo_circuit_client.components.HorizontalLine
import com.qwertyeasy.no_echo_circuit_client.components.MultiLineTextField
import com.qwertyeasy.no_echo_circuit_client.components.PixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.prepareBorder
import com.qwertyeasy.no_echo_circuit_client.components.TableTextField
import com.qwertyeasy.no_echo_circuit_client.components.VerticalLine
import com.qwertyeasy.no_echo_circuit_client.data.enums.AddingStatus
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListViewModel
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey

@Composable
fun UserAddingPopup(onlineViewModel: OnlineListViewModel,
                    rootViewModel: RootViewModel, onDismiss: () -> Unit
){
    Column(modifier = Modifier
        .zIndex(1f)
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth()
                .background(LightGrey, shape = RoundedCornerShape(12.dp))
        ) {
            Spacer(modifier = Modifier.weight(0.05f))
            Column(modifier = Modifier.weight(0.85f)) {
                Spacer(Modifier.weight(0.3f))
                PopupInnerTable(onlineViewModel, rootViewModel, Modifier.weight(0.6f))
                Spacer(Modifier.weight(0.2f))
            }
            Spacer(modifier = Modifier.weight(0.05f))
        }
        DismissSpacer(Modifier.weight(0.4f), onDismiss)
    }
}

@Composable
fun PopupInnerTable(onlineViewModel: OnlineListViewModel, rootViewModel: RootViewModel, modifier: Modifier){

    Column(modifier.border(prepareBorder(BlackBack))
    ) {
        PopupTableUpperRow(onlineViewModel, rootViewModel, Modifier.weight(0.4f))
        HorizontalLine(BlackBack)
        AddNickInputBLock(onlineViewModel, Modifier
            .fillMaxWidth()
            .weight(0.25f)
        )
        HorizontalLine(BlackBack)
        PopupTableBottomRow(onlineViewModel, rootViewModel, Modifier.weight(0.4f))
    }
}

@Composable
fun PopupTableUpperRow(onlineViewModel: OnlineListViewModel, rootViewModel: RootViewModel, modifier: Modifier){
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        StatusIcon(Modifier.weight(0.3f), rootViewModel)
        VerticalLine(BlackBack)
        AddDescriptionBlock(onlineViewModel, Modifier.weight(0.7f))
    }
}

@Composable
fun StatusIcon(modifier: Modifier, rootViewModel: RootViewModel){
    val addingStatus by rootViewModel.addStatusIcon.collectAsState()

    Box(modifier, contentAlignment = Alignment.Center){
        val resource = when(addingStatus){
            AddingStatus.ADD_OK -> painterResource(R.drawable.add_ok)
            AddingStatus.ADD_FAIL -> painterResource(R.drawable.add_fail)
            AddingStatus.NONE -> null
        }
        resource?.let {
            Image(
                painter = resource,
                modifier = Modifier.size(60.dp),
                contentDescription = "AddStatusIcon",
                colorFilter = ColorFilter.tint(BlackBack)
            )
        }
    }
}

@Composable
fun AddDescriptionBlock(onlineViewModel: OnlineListViewModel, modifier: Modifier){
    val addDescription by onlineViewModel.addDescription.collectAsState()

    Box(modifier){
        MultiLineTextField(string = addDescription, containerColor = LightGrey, textColor = BlackBack,
            onValueChange = { onlineViewModel.onAddDescriptionChange(it) })
    }
}

@Composable
fun PopupTableBottomRow(
    onlineViewModel: OnlineListViewModel, rootViewModel: RootViewModel, modifier: Modifier
){
    Row(modifier = modifier) {
        PixelTextButton(
            text = "try-add",
            modifier = Modifier.weight(0.8f),
            contentColor = LightGrey,
            containerColor = BlackBack,
            onClick = { onlineViewModel.onAddEnterClicked(rootViewModel) }
        )
        Box(Modifier
            .weight(0.2f)
            .fillMaxSize()
            .clickable(onClick = { onlineViewModel.onAddEnterClicked(rootViewModel) }),
            contentAlignment = Alignment.Center){
            Text("+")
        }
    }
}

@Composable
fun AddNickInputBLock(onlineViewModel: OnlineListViewModel, modifier: Modifier) {
    val addNickname by onlineViewModel.addNickname.collectAsState()

    Box(modifier = modifier) {
        TableTextField(
            addNickname, LightGrey, BlackBack,
            { onlineViewModel.onAddNicknameChange(it) }
        )
    }
}