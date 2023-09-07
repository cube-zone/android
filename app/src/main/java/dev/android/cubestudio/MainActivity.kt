package dev.android.cubestudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.compose.CubeStudioTheme
import dev.android.cubestudio.ui.theme.MainScreen
import dev.android.cubestudio.ui.theme.poppins
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.android.cubestudio.databases.sessions.SessionDatabase
import dev.android.cubestudio.databases.sessions.SessionViewModel
import dev.android.cubestudio.databases.solves.SolveDatabase
import dev.android.cubestudio.databases.solves.SolveViewModel

val myTypography = Typography(
    labelMedium = TextStyle(
        fontFamily = poppins,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = poppins,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
)

class MainActivity : ComponentActivity() {
    private val solveDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            SolveDatabase::class.java,
            "solves.db"
        ).build()
    }
    private val solveViewModel by viewModels<SolveViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SolveViewModel(solveDatabase.dao) as T
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

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val systemUiController = rememberSystemUiController()

            CubeStudioTheme {
                val solveState by solveViewModel.state.collectAsState()
                val sessionState by sessionViewModel.state.collectAsState()
                MainScreen(
                    solveState = solveState,
                    sessionState = sessionState,
                    onSolveEvent = solveViewModel::onSolveEvent,
                    onSessionEvent = sessionViewModel::onSessionEvent
                )
            }
        }
    }
}