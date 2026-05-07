package com.qwertyeasy.no_echo_circuit_client

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val TronicaFont = FontFamily(
    Font(R.font.tronica)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = TronicaFont,
    ),
    headlineMedium = TextStyle(
        fontFamily = TronicaFont,
    ),
    labelLarge = TextStyle(
        fontFamily = TronicaFont
    ),
    bodyLarge = TextStyle(
        fontFamily = TronicaFont,
        fontSize = 36.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        textAlign = TextAlign.Center
    )
)