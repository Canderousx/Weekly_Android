import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Weekly.android.model.CurrentHomeTab

@Composable
fun MenuBar(currentTab: CurrentHomeTab, changeTab: (CurrentHomeTab) -> Unit,content: @Composable () -> Unit){
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


