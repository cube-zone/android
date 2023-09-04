package dev.android.cubestudio.screens

import android.widget.GridLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.compose.CubeStudioTheme
import dev.android.cubestudio.components.EditCommentDialog
import dev.android.cubestudio.components.TimerStats
import dev.android.cubestudio.databases.solves.Solve
import dev.android.cubestudio.databases.solves.SolveEvent
import dev.android.cubestudio.databases.solves.SolveState
import dev.android.cubestudio.scrambleTypes.Scramble
import dev.android.cubestudio.ui.theme.BottomBarScreen
import dev.android.cubestudio.ui.theme.md_theme_dark_background
import dev.android.cubestudio.ui.theme.md_theme_dark_inversePrimary
import dev.android.cubestudio.ui.theme.md_theme_dark_onBackground
import dev.android.cubestudio.ui.theme.md_theme_dark_onPrimary
import dev.android.cubestudio.ui.theme.md_theme_dark_onSecondary
import dev.android.cubestudio.ui.theme.md_theme_dark_onTertiary
import dev.android.cubestudio.ui.theme.md_theme_dark_primaryContainer
import dev.android.cubestudio.ui.theme.md_theme_dark_scrim
import dev.android.cubestudio.ui.theme.md_theme_dark_secondaryContainer
import dev.android.cubestudio.ui.theme.md_theme_dark_surface
import dev.android.cubestudio.ui.theme.md_theme_dark_surfaceVariant
import dev.android.cubestudio.ui.theme.md_theme_light_background
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
        color = if (time != "DNF") colorResource(R.color.text) else colorResource(id = R.color.secondary),
        fontFamily = azaretMono
    )
}

@Composable
fun Scramble(scramble: String, modifier: Modifier = Modifier) {
    Text(
        text = scramble,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(25.dp, 0.dp),
        fontFamily = poppins
    )
}
@Composable
fun ScrambleOptions() {

}
@Composable
fun LastTimeOptions(
    plusTwo: () -> Unit,
    dnf: () -> Unit,
    deleteSolve: () -> Unit,
    addComment: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        TextButton(onClick = addComment) {
            Icon (
                painter = painterResource(id = R.drawable.baseline_add_comment_24),
                contentDescription = null,
            )
        }
        TextButton(onClick = plusTwo) {
            Text(
                "+2",
                fontSize = 18.sp
            )
        }
        TextButton(onClick = dnf) {
            Text(
                "DNF",
                fontSize = 18.sp
            )
        }
        TextButton(onClick = deleteSolve) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun AverageStat(
    name: String,
    currentAvg: String,
    bestAvg: String
) {
    Row {

    }
}
fun calculateAvg(range: Int, solves: List<Solve>): String? {
    if (solves.size >= range) {
        var sum = 0L
        for (i in 0..range) {
            sum += solves[i].time
        }
        return formatTime(sum/range.toFloat())
    } else return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    state:SolveState,
    onEvent: (SolveEvent) -> Unit
) {
    val timerObject by remember { mutableStateOf(TimerObject())}
    var time by remember { mutableStateOf(0L) }
    var lastTime by remember { mutableStateOf(0L)}
    var currentlyTiming by remember { mutableStateOf(false) }
    val currentScramble by remember {mutableStateOf( Current(Type = "3x3") )}
    var scramble by remember{ mutableStateOf(Scramble(currentScramble.Type)) }

    var lastTimePenalisation by remember { mutableStateOf(0) }
    var dnf by remember { mutableStateOf(false) }
    var lastSolveIsDeleted by remember { mutableStateOf(false) }

    var lastSolve by remember { mutableStateOf(if (state.solves.isNotEmpty())state.solves[0] else null)}

    val view = LocalView.current
    val windowInsets = remember(view) { ViewCompat.getRootWindowInsets(view) }
    val insetTypes = WindowInsetsCompat.Type.systemBars()
    val insets = windowInsets?.getInsets(insetTypes)

    fun plusTwo() {
        lastSolve = state.solves[0]
        if (lastSolve != null) {
            lastTimePenalisation += 2000
            onEvent(SolveEvent.PenaliseSolve(lastSolve!!, lastTimePenalisation))
        }
    }
    fun dnf() {
        lastSolve = state.solves[0]
        if (lastSolve != null) {
            dnf = !dnf
            onEvent(SolveEvent.DnfSolve(lastSolve!!, dnf))
        }
    }
    fun deleteSolve() {
        lastSolve = state.solves[0]
        if (lastSolve != null && !lastSolveIsDeleted) {
            onEvent(SolveEvent.DeleteSolve(lastSolve!!))
            lastSolve = null
            lastTime = 0
            lastTimePenalisation = 0
            lastSolveIsDeleted = true
        }
    }
    fun addComment() {
        if (lastSolve != null) {
            onEvent(SolveEvent.ShowEditCommentDialog)
        }
    }

    LaunchedEffect(currentlyTiming) {
        while (currentlyTiming) {
            time = timerObject.getCurrentTime()
            //lastTime = time
            delay(10)
        }
    }
    CubeStudioTheme() {
        Surface(
            onClick = {
                if (!currentlyTiming) {
                    timerObject.startTimer()
                    lastTimePenalisation = 0
                    dnf = false
                    lastSolveIsDeleted = false
                } else {
                    lastTime = time
                    print(lastTime)
                    println(scramble)
                    onEvent(SolveEvent.SetSolve(
                        createdAt = System.currentTimeMillis(),
                        scramble = scramble,
                        sessionId = 0,
                        time = lastTime,
                        id = (state.solves[0].solveId!! + 1)
                    ))
                    scramble = Scramble(currentScramble.Type)
                    onEvent(SolveEvent.SaveSolve)
                    lastSolve = state.solves[0]
                    for (i in (0..5)) println(state.solves[i])
                }
                currentlyTiming = !currentlyTiming
            },
        ) {
            if (state.isEditingComment && lastSolve != null) {
                EditCommentDialog(state = state, onEvent = onEvent, solve = lastSolve!!)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .weight(1f)
                ) {
                    if (!currentlyTiming) Row(modifier = Modifier.padding(5.dp, 0.dp)) {
                        var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            OutlinedButton(
                                onClick = { scrambleTypeButtonExpanded = true },
                                modifier = Modifier.padding(5.dp, 0.dp),
                                contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(currentScramble.Type)
                                    Icon(Icons.Default.ArrowDropDown, null)
                                }
                            }
                            DropdownMenu(
                                expanded = scrambleTypeButtonExpanded,
                                onDismissRequest = { scrambleTypeButtonExpanded = false },
                            ) {
                                scrambleNames.forEach { scrambleType ->
                                    DropdownMenuItem(
                                        text = { Text(scrambleType) },
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
                            Row(verticalAlignment = Alignment.Bottom) {
                                if (!dnf) {
                                    Timer(time = formatTime(time = (lastTime + lastTimePenalisation).toFloat()))
                                    if (lastTimePenalisation != 0) Text(
                                        text = "(+${lastTimePenalisation / 1000})",
                                        modifier = Modifier.padding(5.dp),
                                        color = Color.Red
                                    )
                                } else {
                                    Timer(time = "DNF")
                                }
                            }
                        } else {
                            Timer(time = formatTime(time = time.toFloat()))
                        }
                        if (!currentlyTiming) {
                            LastTimeOptions(
                                plusTwo = { plusTwo() },
                                dnf = { dnf() },
                                deleteSolve = { deleteSolve() },
                                addComment = { addComment() }
                            )
                        }
                    }
                }
                if (!currentlyTiming) TimerStats(state.solves, modifier = Modifier)
            }
        }
    }
}