package dev.android.cubestudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.core.view.WindowCompat
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Typography
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import dev.android.cubestudio.R
import dev.android.cubestudio.ui.theme.MainScreen
import dev.android.cubestudio.ui.theme.poppins
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = false
            val secondaryBg = colorResource(R.color.secondaryBg)

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = secondaryBg,
                    darkIcons = useDarkIcons
                )
            }

            MaterialTheme(typography = myTypography) {
                MainScreen()
            }
        }
    }
}