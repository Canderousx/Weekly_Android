package com.Weekly.android.view
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Weekly.android.R
import com.Weekly.android.model.CurrentHomeTab
import com.Weekly.android.model.Expense
import com.Weekly.android.model.User
import com.Weekly.android.model.Week
import com.Weekly.android.service.FontService
import com.Weekly.android.service.TextFormatter
import com.Weekly.android.view.ui.theme.MenuBar
import com.Weekly.android.view.ui.theme.MyTypography
import com.Weekly.android.view.ui.theme.WeeklyTheme
import com.Weekly.android.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<HomeViewModel>() {
    override val viewModel by viewModel<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeeklyTheme {
                if (viewModel.currentUser != null && viewModel.currentWeek != null) {
                    if(viewModel.currentUser!!.weeklyPlan == 0.0){
                        WeeklyPlanSetup(false) { viewModel.returnToCurrentWeek() }
                    }else{
                        MenuBar(viewModel.currentTab, changeTab = {tab -> viewModel.changeTab(tab)}){
                            Background {
                                when(viewModel.currentTab){
                                    CurrentHomeTab.CURRENT_WEEK -> HomeWindow()
                                    CurrentHomeTab.NEW_EXPENSE -> NewExpenseWindow { viewModel.returnToCurrentWeek() }
                                    CurrentHomeTab.STATISTICS -> StatisticsWindow { viewModel.returnToCurrentWeek() }
                                    CurrentHomeTab.EDIT_EXPENSE -> EditExpense { viewModel.returnToCurrentWeek() }
                                    CurrentHomeTab.CONFIRMATION_PANEL -> ConfirmationPanel(
                                        onBackPressed = {viewModel.returnToCurrentWeek()},
                                        onAgree = {viewModel.confirmationOnAgree()},
                                        onDisagree = {viewModel.confirmationOnDisagree()},
                                        text = viewModel.confirmationMessage
                                    )

                                    CurrentHomeTab.EDIT_WEEKLY_PLAN -> WeeklyPlanSetup(true){ viewModel.returnToCurrentWeek() }
                                }
                            }
                        }

                    }
                }
                ObserveServerStatus()

            }

        }
    }


    @Composable
    override fun OnServerError() {
        super.OnServerError()
    }

    @Composable
    override fun OnSuccess() {

    }

    private fun getExpenseColor(expenses: Double, weeklyPlan: Double): Color{
        if (expenses > weeklyPlan ){
            return Color(0xFF720000)
        }
        return Color(0xFF0B5B00)

    }

    @Composable
    fun Background(content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize()
                .padding(top = 20.dp, start = 15.dp, end = 15.dp, bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                content()
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weekly_black_logo),
                    contentDescription = "White logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WeeklyPlanSetup(editMode: Boolean, onBackPressed: () -> Unit){
            BackHandler {
                if(editMode){
                    onBackPressed()
                }
            }
            var weeklyPlan by rememberSaveable { mutableStateOf( if(editMode) viewModel.currentUser?.weeklyPlan.toString() else "") }
            var selectedCurrency by rememberSaveable { mutableStateOf(if(editMode) viewModel.currentUser?.currency else viewModel.availableCurrencies?.names?.get(0) ?:"")}
            var dropDownExpanded by rememberSaveable { mutableStateOf(false) }
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(if(editMode) "Edit your weekly expense goal" else "How much money you wish to spend every week?")
                TextField(
                    value = weeklyPlan,
                    onValueChange = {weeklyPlan = it},
                    label = { Text(text = "Your weekly expense goal") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.size(15.dp))

                if(!editMode){

                    ExposedDropdownMenuBox(
                        expanded = dropDownExpanded,
                        onExpandedChange = {dropDownExpanded = !dropDownExpanded}

                    ) {
                        selectedCurrency?.let {
                            TextField(
                                value = it,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Currency") },
                                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)},
                                modifier = Modifier
                                    .fillMaxWidth().menuAnchor()
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = dropDownExpanded,
                            onDismissRequest = {dropDownExpanded = false},
                        ) {
                            viewModel.availableCurrencies?.names?.forEachIndexed{ index, currency ->
                                DropdownMenuItem(
                                    text = { Text(text = currency) },
                                    onClick = {selectedCurrency = viewModel.availableCurrencies?.names!![index]
                                        dropDownExpanded = false}
                                )
                            }
                        }
                    }

                }


                Spacer(Modifier.size(15.dp))
                Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF0A3F04)),
                    onClick = {
                        selectedCurrency?.let {
                            viewModel.weeklyPlanSetup(
                                weeklyPlan = weeklyPlan,
                                currency = it,
                                editMode = editMode
                            )
                        }
                    }) {

                    Text(text= if(editMode) "Edit" else "Confirm")

                }
            }

    }

    @Composable
    fun ConfirmationPanel(onBackPressed: () -> Unit,
                          onAgree: () -> Unit,
                          onDisagree: () -> Unit,
                          text: String){
        BackHandler {
            onBackPressed()
        }
        Column(
            modifier = Modifier.background(Color(0xFFDCDCDC))
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(style = TextStyle(textAlign = TextAlign.Center),text= text)
            Row {
                Button(
                    onClick = onAgree,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF066007)
                    )
                ) {
                    Text("Confirm")
                }
                Spacer(Modifier.size(15.dp))
                Button(
                    onClick = onDisagree,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF8D0202)
                    )
                ) {
                    Text("Cancel")
                }
            }

        }



    }




    @Composable
    fun CurrentWeek(currentUser: User,currentWeek: Week) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Week", style = MyTypography.headlineLarge)
            Text(
                "${currentWeek.getStartDate()} - ${currentWeek.getEndDate()}",
                style = MyTypography.headlineMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "${currentWeek.expenses}",
                    style = MyTypography.headlineSmall.copy(color = getExpenseColor(currentWeek.expenses, currentUser.weeklyPlan))
                )
                Text("/${currentUser.weeklyPlan}", style = MyTypography.headlineSmall)
                Text(currentUser.currency, style = MyTypography.headlineSmall)
                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = {
                    viewModel.changeTab(CurrentHomeTab.EDIT_WEEKLY_PLAN)
                },
                    shape = CircleShape,
                    modifier = Modifier.size(15.dp),
                    contentPadding = PaddingValues(1.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change weekly plan",
                        modifier = Modifier.size(15.dp)
                    )
                }


            }
        }
        Spacer(modifier = Modifier.size(15.dp))
        Column(
            modifier = Modifier.background(Color(0xFFE0E0E0))
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if((viewModel.currentExpenses?.expenses?.size ?: 0) == 0){
                Text("No expenses yet")
            }

            LazyColumn {
                items(viewModel.currentExpenses?.expenses ?: emptyList()) { expense ->
                    SingleExpense(expense)
                }
            }
            Spacer(Modifier.size(25.dp))

        }
    }

    @Composable
    fun SingleExpense(expense: Expense){
        Row(modifier = Modifier.padding(16.dp)) {

            Text(TextFormatter.lazyColumnItemName(expense.name))
            Spacer(Modifier.weight(1f))
            Text("${expense.amount} ${viewModel.currentUser?.currency}")
            Spacer(Modifier.size(10.dp))
            Button(onClick = { viewModel.editExpense(expense) },
                shape = CircleShape,
                modifier = Modifier.size(20.dp),
                contentPadding = PaddingValues(1.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF05179F)
                )
            ){
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit an expense",
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(Modifier.size(10.dp))
            Button(
                onClick = { viewModel.deleteExpenseTab(expense) },
                shape = CircleShape,
                modifier = Modifier.size(20.dp),
                contentPadding = PaddingValues(1.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF9F0101)
                )
            ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete an expense",
                    modifier = Modifier.size(15.dp)
                )
            }
        }

    }

    @Composable
    fun EditExpense(
        onBackPressed: () -> Unit
    ){
        BackHandler {
            onBackPressed()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top= 15.dp,bottom=15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewExpenseForm(editMode = true)
        }
    }

    @Composable
    fun DeleteExpense(
    onBackPressed: () -> Unit
    ) {
        BackHandler {
            onBackPressed()
        }
    }

    @Composable
    fun HomeWindow(){
        viewModel.logger.info("CURRENT USER: ${viewModel.currentUser?.username}")
        viewModel.logger.info("CURRENT WEEK: ${viewModel.currentWeek?.expenses}")
        CurrentWeek(viewModel.currentUser!!, viewModel.currentWeek!!)
    }

    @Composable
    fun NewExpenseWindow(onBackPressed: () -> Unit){
        BackHandler {
            onBackPressed()
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            NewExpenseForm(editMode = false)
        }


    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewExpenseForm(editMode: Boolean){
        var expenseName by rememberSaveable { mutableStateOf( if(editMode) viewModel.expenseToEdit?.name ?: "" else "") }
        var expenseAmount by rememberSaveable { mutableStateOf(if(editMode) viewModel.expenseToEdit?.amount.toString() else "") }
        var selectedCurrency by rememberSaveable { mutableStateOf(if(editMode) viewModel.currentUser?.currency ?: "" else viewModel.availableCurrencies?.names?.get(0) ?:"")}
        var dropDownExpanded by rememberSaveable { mutableStateOf(false) }
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            Text(
                if (editMode) "Expense Edit Form" else "New Expense Form"
            )

            TextField(
                value = expenseName,
                onValueChange = {expenseName = it},
                label = { Text(text="What have you bought?") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(15.dp))

            TextField(
                value = expenseAmount,
                onValueChange = {expenseAmount = it},
                label = { Text(text = "Cost") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.size(15.dp))

            ExposedDropdownMenuBox(
                expanded = dropDownExpanded,
                onExpandedChange = {dropDownExpanded = !dropDownExpanded}

            ) {
                TextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)},
                    modifier = Modifier
                        .fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = {dropDownExpanded = false},
                ) {
                    viewModel.availableCurrencies?.names?.forEachIndexed{ index, currency ->
                        DropdownMenuItem(
                            text = { Text(text = currency) },
                            onClick = {selectedCurrency = viewModel.availableCurrencies?.names!![index]
                                dropDownExpanded = false}
                        )
                    }
                }
            }

            Spacer(Modifier.size(15.dp))
            Button(colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF0A3F04)),
                onClick = {
                    viewModel.newExpense(
                        name = expenseName,
                        amount = expenseAmount,
                        currency = selectedCurrency,
                        editMode = editMode
                    )
                }) {

                Text(text= if(editMode) "Edit" else "Add")

            }
        }
    }

    @Composable
    fun SingleWeek(week: Week){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(week.getStartDate()+"-"+week.getEndDate(), style = MyTypography.headlineSmall.copy(fontSize = 15.sp))
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {},

            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        }
    }

    @Composable
    fun StatisticsWindow(onBackPressed: () -> Unit){
        BackHandler {
            onBackPressed()
        }

        Column(
            Modifier.fillMaxSize()
        ) {
            Text("Statistics", style = MyTypography.headlineLarge)
            Spacer(Modifier.size(15.dp))
            Row {
                Text("Average Weekly Expense")
                Spacer(Modifier.weight(1f))
                Text(viewModel.averageWeeklyExpense.toString()+"/${viewModel.currentUser?.weeklyPlan}${viewModel.currentUser?.currency}")
            }
            Row {
                Text("Average Expense")
                Spacer(Modifier.weight(1f))
                Text(viewModel.averageTotalExpense.toString()+viewModel.currentUser?.currency)

            }
            Row {
                Text("Weeks in Weekly")
                Spacer(Modifier.weight(1f))
                Text(viewModel.howManyWeeks.toString())

            }
            Spacer(Modifier.size(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A4900)
                    )
                ) {
                    Text("Export to .xlsx")
                }
            }
            Spacer(Modifier.size(10.dp))

            Column(
                modifier = Modifier.background(Color(0xFFE0E0E0))
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth()
                        .padding(start = 30.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Week")
                    Text("More")
                }
                LazyColumn {
                    items(viewModel.allWeeks?.weeks?: emptyList()) { week ->
                        Spacer(Modifier.size(20.dp))
                        SingleWeek(week)
                    }
                }
                Spacer(Modifier.size(25.dp))

            }



        }

    }

}
