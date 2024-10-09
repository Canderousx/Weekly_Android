package com.Weekly.android.view.ui.theme

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.Weekly.android.model.CurrentHomeTab
import com.Weekly.android.model.Response.ServerResponse

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDarkTheme,
    surface = CardDarkTheme,
    onBackground = FontDarkTheme,
    onSurface = FontDarkTheme

)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLightTheme,
    surface = CardLightTheme,
    onBackground = FontLightTheme,
    onSurface = FontLightTheme


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun WeeklyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MyTypography,
        content = content
    )
}

@Composable
fun LoadingComposable(error: ServerResponse?){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF6200EE),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            AnimatedVisibility (visible = error != null, enter = fadeIn(), exit = fadeOut()) {
                Text(text = "Device has no internet connection...Retrying")
            }
        }
    }
}


@Composable
fun MenuBar(currentTab: CurrentHomeTab, changeTab: (CurrentHomeTab) -> Unit, content: @Composable () -> Unit){
    val buttonSize = 45.dp;

    val spacerSize = 5.dp;

    val sidesPadding = 10.dp;

    val activeColor: Color = Color.Cyan

    val inactiveColor: Color = Color.White


    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF111111)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = sidesPadding, end = sidesPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(onClick = {changeTab(CurrentHomeTab.CURRENT_WEEK)}) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            tint = if (currentTab == CurrentHomeTab.CURRENT_WEEK) activeColor else inactiveColor,
                            modifier = Modifier.size(buttonSize),
                            contentDescription = "Current Week"
                        )
                    }
                    Spacer(Modifier.size(spacerSize))
                    IconButton(onClick = {changeTab(CurrentHomeTab.NEW_EXPENSE)}) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            tint = if (currentTab == CurrentHomeTab.NEW_EXPENSE) activeColor else inactiveColor,
                            modifier = Modifier.size(buttonSize),
                            contentDescription = "Add Expense"
                        )
                    }
                    Spacer(Modifier.size(spacerSize))
                    IconButton(onClick = {changeTab(CurrentHomeTab.STATISTICS)}) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            tint = if (currentTab == CurrentHomeTab.STATISTICS) activeColor else inactiveColor,
                            modifier = Modifier.size(buttonSize),
                            contentDescription = "See All Weeks"
                        )
                    }
                }

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}
