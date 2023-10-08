package dev.android.cubezone

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.android.cubezone.ui.theme.CubeStudioTheme
import dev.android.cubezone.ui.theme.MainScreen
import dev.android.cubezone.ui.theme.poppins
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.android.cubezone.databases.sessions.SessionDatabase
import dev.android.cubezone.databases.sessions.SessionViewModel
import dev.android.cubezone.databases.solves.SolveDatabase
import dev.android.cubezone.databases.solves.SolveViewModel
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle


object UserPreferencesRepositoryProvider {
    private var instance: UserPreferencesRepository? = null

    fun getInstance(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return instance ?: synchronized(this) {
            instance ?: UserPreferencesRepository(dataStore).also {
                instance = it
            }
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private val solveDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            SolveDatabase::class.java,
            "solves.db"
        ).build()
    }
    private val mainViewModel by viewModels<MainViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(userPreferencesRepository, SavedStateHandle()) as T
                }
            }
        }
    )
    private val solveViewModel by viewModels<SolveViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SolveViewModel(solveDatabase.dao, mainState = mainViewModel.state.value) as T
                }
            }
        }
    )
    private val sessionDatabase by lazy {
        val MIGRATION_1_2 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        Room.databaseBuilder(
            applicationContext,
            SessionDatabase::class.java,
            "session.db",
        ).createFromAsset("database/session.db").addMigrations(MIGRATION_1_2).build()
    }
    private val sessionViewModel by viewModels<SessionViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SessionViewModel(sessionDatabase.dao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferencesRepository = UserPreferencesRepositoryProvider.getInstance(dataStore)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val systemUiController = rememberSystemUiController()

            CubeStudioTheme {
                val solveState by solveViewModel.state.collectAsState()
                val sessionState by sessionViewModel.state.collectAsState()
                val mainState by mainViewModel.state.collectAsState()

                Log.d("color", MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp).toString())
                MainScreen(
                    solveState = solveState,
                    sessionState = sessionState,
                    onSolveEvent = solveViewModel::onSolveEvent,
                    onSessionEvent = sessionViewModel::onSessionEvent,
                    mainViewModel = mainViewModel,
                    mainState = mainState,
                )
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_session_id", mainViewModel.state.value.currentSessionId)
        outState.putString("current_scramble_type", mainViewModel.state.value.currentScrambleType)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredSessionId = savedInstanceState.getInt("current_session_id")
        mainViewModel.updateCurrentSessionId(restoredSessionId)
        val restoredScrambleType = savedInstanceState.getString("current_scramble_type")
        mainViewModel.updateCurrentScrambleType(restoredScrambleType ?: "3x3")
    }
}
