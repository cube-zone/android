package dev.android.cubestudio.screens

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.android.cubestudio.R
import dev.android.cubestudio.ui.theme.CubeStudioTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.android.cubestudio.scrambleTypes.scrambleThree
import dev.android.cubestudio.scrambleTypes.Scramble
import kotlin.math.floor

val robotoMono = FontFamily(Font(resId = R.font.robotomonomedium))
val poppins = FontFamily(Font(resId = R.font.poppinsmedium))
val poppinsSemiBold = FontFamily(Font(resId = R.font.poppinssemibold))
val azaretMono = FontFamily(Font(resId = R.font.azeretmono))

val scrambleNames = arrayOf(
    "2x2",
    "3x3",
    "4x4",
    "5x5",
    "6x6",
    "7x7",
    "Pyraminx",
    "Sq-1",
    "Megaminx",
    "Skewb",
    "Clock"
)
data class Current(
    var Type: String
)

class TimerObject() {
    var startTime: Long = 1689445680153
    fun startTimer(): Long {
        startTime = System.currentTimeMillis()
        Log.d("DEBUG", startTime.toString())
        return startTime
    }
    fun getCurrentTime(): Long {
        return System.currentTimeMillis() - startTime
    }
}
fun formatTime(time: Float): String {
    val decimal: String = ((floor(time / 10) % 100).toInt().toString())
    val seconds: String = floor((time / 1000)%60).toInt().toString()
    val minutes: String = floor(time / 60000).toInt().toString()
    val min = if (minutes == "0") "" else "$minutes:"
    val sec = if (seconds.length == 1 && minutes != "0") "0$seconds" else seconds
    val dec = if (decimal.length == 1) "0$decimal" else decimal
    return "$min$sec.$dec"
}

@Composable
fun Timer(time: String, modifier: Modifier = Modifier){
    Text(
        text = time,
        fontSize = 55.sp,
        textAlign = TextAlign.Center,
        color = colorResource(R.color.text),
        fontFamily = azaretMono
    )
}

@Composable
fun Scramble(scramble: String, modifier: Modifier = Modifier) {
    Text(
        text = scramble,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = colorResource(id = R.color.text),
        modifier = Modifier.padding(25.dp, 0.dp),
        fontFamily = poppins
    )
}

@Composable
fun LastTimeOptions(plusTwo: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Icon (
                painter = painterResource(id = R.drawable.baseline_add_comment_24),
                contentDescription = null,
                tint = colorResource(id = R.color.primary)
            )
        }
        TextButton(onClick = plusTwo) {
            Text(
                "+2",
                color = colorResource(id = R.color.primary),
                fontSize = 18.sp
            )
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(
                "DNF",
                color = colorResource(id = R.color.primary),
                fontSize = 18.sp
            )
        }
        TextButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = colorResource(id = R.color.primary)
            )
        }
    }
}

/*
@Composable
fun ScrambleTypeDropdown(expanded:Boolean):Boolean {
    var localExpanded = expanded
    println("AKDNWDKAWKDNAWKDAKDWNKJANW")
    DropdownMenu(x
        expanded = localExpanded,
        onDismissRequest = { localExpanded = false; println("dismissed") },
    ) {
        scrambleNames.forEach {scrambleType ->
            DropdownMenuItem(
                text = {Text(scrambleType)},
                onClick = {
                    localExpanded = false
                    currentScrambleType = scrambleType
                },
                contentPadding = PaddingValues(horizontal = 15.dp),
                modifier = Modifier.sizeIn(maxHeight = 35.dp)
            )
        }
    }
    println(localExpanded)
    return localExpanded
}

 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(session: String, paddingValues: PaddingValues, modifier: Modifier = Modifier) {
    val timerObject by remember { mutableStateOf(TimerObject())}
    var time by remember { mutableStateOf(0L) }
    var lastTime by remember { mutableStateOf(0L)}
    var currentlyTiming by remember { mutableStateOf(false) }
    val currentScramble by remember {mutableStateOf( Current(Type = "3x3") )}
    var scramble by remember{ mutableStateOf(Scramble(currentScramble.Type)) }

    val view = LocalView.current
    val windowInsets = remember(view) { ViewCompat.getRootWindowInsets(view) }
    val insetTypes = WindowInsetsCompat.Type.systemBars()
    val insets = windowInsets?.getInsets(insetTypes)

    fun plusTwo() {lastTime += 2000}
    LaunchedEffect(currentlyTiming) {
        while (currentlyTiming) {
            time = timerObject.getCurrentTime()
            lastTime = time
            delay(10)
        }
    }
    CubeStudioTheme() {
        Surface(
            color = colorResource(id = R.color.mainBg),
            onClick = {
                println(currentScramble)
                if (!currentlyTiming) {
                    timerObject.startTimer()
                } else {
                    scramble = Scramble(currentScramble.Type)
                    //code to save solve
                }
                currentlyTiming = !currentlyTiming
            },
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding()-(insets?.top!!/2).dp,
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                if (!currentlyTiming) Row (modifier = Modifier.padding(5.dp, 0.dp)) {
                    //start
                    var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopStart)
                    ) {
                        OutlinedButton(
                            onClick = { scrambleTypeButtonExpanded = true },
                            border = BorderStroke(1.dp, colorResource(id = R.color.primary)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = colorResource(id = R.color.text)
                            ),
                            modifier = Modifier.padding(5.dp, 0.dp),
                            contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(currentScramble.Type)
                                println(currentScramble.Type)
                                Icon(Icons.Default.ArrowDropDown, null, tint = colorResource(id = R.color.text))
                            }
                        }
                        DropdownMenu(
                            expanded = scrambleTypeButtonExpanded,
                            onDismissRequest = { scrambleTypeButtonExpanded = false},
                        ) {
                            scrambleNames.forEach {scrambleType ->
                                DropdownMenuItem(
                                    text = {Text(scrambleType)},
                                    onClick = {
                                        scrambleTypeButtonExpanded = false
                                        currentScramble.Type = scrambleType
                                        scramble = Scramble(scrambleType)
                                    },
                                    contentPadding = PaddingValues(horizontal = 15.dp),
                                    modifier = Modifier.sizeIn(maxHeight = 35.dp)
                                )
                            }
                        }
                    }
                    //end
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scramble(
                        scramble = scramble,
                        Modifier.padding(vertical = 20.dp)
                    )
                    if (!currentlyTiming) {
                        Timer(time = formatTime(time = lastTime.toFloat()))
                    }
                    else {
                        Timer(time = formatTime(time = time.toFloat()))
                    }
                    if (!currentlyTiming)LastTimeOptions(plusTwo = {plusTwo()})
                }
                // TODO Stats
            }
        }
    }
}