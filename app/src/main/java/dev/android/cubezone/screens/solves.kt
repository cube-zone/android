package dev.android.cubezone.screens

import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.State
import dev.android.cubezone.components.ScrambleSelection
import dev.android.cubezone.components.SessionSelection
import dev.android.cubezone.components.SolvePopUp
import dev.android.cubezone.databases.sessions.Session
import dev.android.cubezone.databases.sessions.SessionEvent
import dev.android.cubezone.databases.sessions.SessionState
import dev.android.cubezone.databases.sessions.SessionViewModel
import dev.android.cubezone.databases.solves.Solve
import dev.android.cubezone.databases.solves.SolveEvent
import dev.android.cubezone.databases.solves.SolveState
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


fun formatDuration(milliseconds: Long): String {
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
    val years = days / 365 // Approximate, not considering leap years

    return when {
        years > 0 -> "$years ${if (years == 1L) "year" else "yrs"}"
        days > 0 -> "$days ${if (days == 1L) "day" else "days"}"
        hours > 0 -> "$hours ${if (hours == 1L) "hour" else "hrs"}"
        minutes > 0 -> "$minutes ${if (minutes == 1L) "min" else "mins"}"
        seconds > 0 -> "$seconds ${if (seconds == 1L) "sec" else "secs"}"
        else -> "now"
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SolvesScreen(
    solveState: SolveState,
    sessionState: SessionState,
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    onSessionEvent: (SessionEvent) -> Unit,
    onSolveEvent: (SolveEvent) -> Unit,
    mainState: State,
) {
    var currentScramble: Current by remember { mutableStateOf(Current(mainState.currentScrambleType)) }
    var currentSession: Session? by remember { mutableStateOf(null) }
    var currentSessionId: Int by remember { mutableStateOf(mainState.currentSessionId) }
    var query: String by remember { mutableStateOf("") }
    var searchBarActive: Boolean by remember { mutableStateOf(false) }
    var searchBarCursorActive by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardIsOpen = WindowInsets.isImeVisible
    var popupSolve: Solve? by remember{ mutableStateOf(null) }
    if(currentScramble.type == null) currentScramble.type = mainState.currentScrambleType
    if(currentSession == null) currentSession = sessionState.sessions.find { it.sessionId == currentSessionId }
    viewModel.updateCurrentScrambleType(currentScramble.type ?: "3x3")
    viewModel.updateCurrentSessionId(currentSession?.sessionId ?: 0)
    var currentSessionSolves: List<Solve> by remember { mutableStateOf(listOf()) }
    if (currentSession != null) {
        currentSessionSolves = solveState.solves.filter { it.sessionId == currentSession!!.sessionId }
    }
    Log.d("RECOMPOSE", "SolvesScreen")
    Column(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        if (solveState.solvePopupIsShown && popupSolve != null) {
            Log.d("DEBUG", "${popupSolve!!.dnf}")
            SolvePopUp(
                solveId = popupSolve!!.solveId?:0,
                onSolveEvent = onSolveEvent,
                solveState = solveState,
                comment = popupSolve!!.comment,
                viewModel = viewModel
            )
        }
        Column {
            var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
            Row(modifier = Modifier.padding(5.dp, 5.dp)) {
                var scrambleTypeButtonExpanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    OutlinedButton(
                        onClick = { scrambleTypeButtonExpanded = true },
                        modifier = Modifier
                            .animateContentSize()
                            .padding(5.dp, 0.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentScramble.type ?: "3x3")
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
                                    currentScramble.type = scrambleType
                                    viewModel.updateCurrentScramble("") //set to empty so it will be updated in timer
                                    if (sessionState.sessions.find { it.scrambleType == scrambleType } != null) {
                                        currentSession = sessionState.sessions.find { it.scrambleType == scrambleType }
                                        viewModel.updateCurrentScrambleType(scrambleType)
                                    } else { //if there are no sessions with this scramble type create a new one
                                        onSessionEvent(
                                            SessionEvent.SetSession(
                                                sessionName = "default",
                                                scrambleType = scrambleType,
                                                createdAt = System.currentTimeMillis(),
                                                lastUsedAt = System.currentTimeMillis().toInt()
                                            )
                                        )
                                        onSessionEvent(SessionEvent.SaveSession)
                                        currentSession = sessionState.sessions.find { it.scrambleType == scrambleType }
                                        Log.d("DEBUG", "currentSession: ${currentSession?.sessionName}")
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 15.dp),
                                modifier = Modifier.sizeIn(maxHeight = 35.dp)
                            )
                        }
                    }
                }
                var selectSessionButtonExpanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    OutlinedButton(
                        onClick = { selectSessionButtonExpanded = true },
                        modifier = Modifier
                            .animateContentSize()
                            .padding(5.dp, 0.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentSession?.sessionName ?: "Not Selected")
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                    DropdownMenu(
                        expanded = selectSessionButtonExpanded,
                        onDismissRequest = { selectSessionButtonExpanded = false },
                    ) {
                        sessionState.sessions.forEach { session ->
                            if (session.scrambleType == mainState.currentScrambleType) {
                                DropdownMenuItem(
                                    text = { Text(session.sessionName) },
                                    onClick = {
                                        currentSession = session
                                        selectSessionButtonExpanded = false
                                        viewModel.updateCurrentSessionId(session.sessionId ?: 0)
                                    },
                                    contentPadding = PaddingValues(horizontal = 15.dp),
                                    modifier = Modifier.sizeIn(maxHeight = 35.dp)
                                )
                            }
                        }
                        DropdownMenuItem(
                            text = { Text("New Session") },
                            trailingIcon = { Icon(Icons.Default.Add, null) },
                            onClick = {
                                onSessionEvent(SessionEvent.ShowAddSessionDialog)
                                selectSessionButtonExpanded = false
                            },
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            modifier = Modifier.sizeIn(maxHeight = 35.dp)
                        )
                    }
                }
                currentSessionId = mainState.currentSessionId
            }
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 0.dp)
            ) {
                if (!searchBarActive) {
                    keyboardController?.hide()
                }
                val view = LocalView.current
                val viewTreeObserver = view.viewTreeObserver
                DisposableEffect(viewTreeObserver) {
                    val listener = ViewTreeObserver.OnGlobalLayoutListener {
                        val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                            ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
                        searchBarActive = isKeyboardOpen
                    }
                    viewTreeObserver.addOnGlobalLayoutListener(listener)
                    onDispose {
                        viewTreeObserver.removeOnGlobalLayoutListener(listener)
                    }
                }
                SearchBar(
                    query = query,
                    onQueryChange = {
                        query = it
                        searchBarActive = true
                    },
                    onSearch = {
                        searchBarActive = false
                    },
                    active = false,
                    onActiveChange = {
                        searchBarActive = true
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query != "") {
                            IconButton(
                                {
                                    query = ""
                                    keyboardController?.hide()
                                    searchBarActive = false
                                }
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        } else null
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .offset(y = (-3).dp),
                    colors = SearchBarDefaults.colors(
                        inputFieldColors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            cursorColor = Color.Transparent,
                            focusedTextColor = Color.Transparent,
                            unfocusedTextColor = Color.Transparent,
                        ),
                    ),
                ) {}
                if (searchBarActive) {
                    LaunchedEffect(searchBarCursorActive) {
                        while (true) {
                            delay(500)
                            searchBarCursorActive = !searchBarCursorActive
                        }
                    }
                }
                if (query != "") {
                    Row(
                        modifier = Modifier
                            .padding(66.dp, 0.dp)
                            .zIndex(2f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = query,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        if (searchBarActive && searchBarCursorActive) {
                            VerticalDivider(
                                Modifier
                                    .height(15.dp)
                                    .padding(0.dp, 0.dp),
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 2.dp
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .then(
                                if (searchBarCursorActive && searchBarActive) Modifier.padding(
                                    64.dp,
                                    0.dp
                                ) else Modifier.padding(66.dp, 0.dp)
                            )
                            .zIndex(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (searchBarActive && searchBarCursorActive) {
                            VerticalDivider(
                                Modifier
                                    .height(15.dp)
                                    .padding(0.dp, 0.dp),
                                color = MaterialTheme.colorScheme.primary,
                                thickness = 2.dp
                            )
                        }
                        Text(
                            text = "Search Comments",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 20.dp),
            tonalElevation = 2.dp,
            shape = MaterialTheme.shapes.large
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.Start,
                state = rememberLazyListState(),
                modifier = Modifier
            ) {
                currentSessionSolves.forEach { solve ->
                    if ((query == "" || solve.comment?.contains(query, ignoreCase = true) == true) && solve.sessionId == (currentSession?.sessionId ?: 0)) {
                        item {
                            Box(modifier = Modifier.clickable {
                                popupSolve = solve
                                viewModel.updateCurrentPopupSolve(solve)
                                onSolveEvent(SolveEvent.ShowSolvePopup)
                                Log.d("DEBUG", "$solve")
                            }) {
                                if (currentSessionSolves.indexOf(solve) % 2 == 0)  {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                                            .height(40.dp),
                                        tonalElevation = 2.dp,
                                    ) {}
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .heightIn(max = 40.dp)
                                        .fillMaxWidth()
                                        .padding(0.dp),
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = if (solve.dnf) "DNF" else {
                                                    formatTime(
                                                        (solve.time + (solve.penalisation
                                                            ?: 0)).toFloat()
                                                    )
                                                },
                                                modifier = Modifier.padding(10.dp),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = if (solve.dnf) Color.Red else Color.White,
                                                lineHeight = 20.sp
                                            )
                                            if (solve.penalisation != null && solve.penalisation != 0) {
                                                Text(
                                                    text = " +${solve.penalisation / 1000}",
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.Red,
                                                )
                                            }
                                        }
                                        if (solve.wasPb && !solve.dnf) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = Color.Yellow,
                                                modifier = Modifier.padding(0.dp)
                                            )
                                        }
                                    }

                                    if (solve.comment != null && solve.comment != "") {
                                        Text(
                                            text = solve.comment ?: "",
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    }
                                    Text(
                                        text = formatDuration(System.currentTimeMillis() - solve.createdAt),
                                        modifier = Modifier.padding(10.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}