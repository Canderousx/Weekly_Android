package com.Weekly.android.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.Weekly.android.api.UserDetailsApi
import com.Weekly.android.api.WeekApi
import com.Weekly.android.model.Currencies
import com.Weekly.android.model.CurrentHomeTab
import com.Weekly.android.model.Expense
import com.Weekly.android.model.ExpensesList
import com.Weekly.android.model.Request.NewExpenseRequest
import com.Weekly.android.model.Request.WeeklyPlanSetupReq
import com.Weekly.android.model.Response.ServerResponse
import com.Weekly.android.model.ServerOperationStatus

import com.Weekly.android.model.User
import com.Weekly.android.model.Week
import com.Weekly.android.service.ValidatorService

import com.Weekly.android.util.ApiConfiguration


class HomeViewModel(
    private val apiConfiguration: ApiConfiguration,

): BaseViewModel(apiConfiguration) {

    private val userDetailsApi = UserDetailsApi(apiConfiguration)
    private val weekApi = WeekApi(apiConfiguration)
    var currentTab by mutableStateOf<CurrentHomeTab>(CurrentHomeTab.CURRENT_WEEK)

    var currentUser by mutableStateOf<User?>(null)
    var currentWeek by mutableStateOf<Week?>(null)
    var currentExpenses by mutableStateOf<ExpensesList?>(null)
    var availableCurrencies by mutableStateOf<Currencies?>(null)
    var expenseToEdit by mutableStateOf<Expense?>(null)
    var confirmationMessage by mutableStateOf("")
    var confirmationOnAgree by mutableStateOf({})
    var confirmationOnDisagree by mutableStateOf({})


    init {
        initData()
    }

    fun weeklyPlanSetup(weeklyPlan: String,currency: String,editMode: Boolean){
        operationStatus = ServerOperationStatus.UNKNOWN
        serverResponse = null
        if(weeklyPlan == "" || weeklyPlan.toDouble() == (currentUser?.weeklyPlan ?: 0.0) || (!editMode && currency == "")){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("Invalid values")
            return
        }
        serverConnection(
            method = { userDetailsApi.setWeeklyPlan(weeklyPlan.toDouble(),currency,editMode)},
            onSuccess = {
                getCurrentUser()
                changeTab(CurrentHomeTab.CURRENT_WEEK)
            }
        )


    }

    fun changeTab(tab: CurrentHomeTab){
        serverResponse = null
        operationStatus = ServerOperationStatus.UNKNOWN
        currentTab = tab;
    }

    fun editExpense(toEdit: Expense){
        expenseToEdit = toEdit
        changeTab(CurrentHomeTab.EDIT_EXPENSE)
    }

    fun deleteExpenseTab(toDelete: Expense){
        expenseToEdit = toDelete
        confirmationOnAgree = {
            deleteExpense(expenseToEdit!!.id)
        }
        confirmationOnDisagree = {
            returnToCurrentWeek()
        }
        confirmationMessage = "Are you sure you want to delete '${expenseToEdit!!.name}' expense?"
        changeTab(CurrentHomeTab.CONFIRMATION_PANEL)
    }

    fun returnToCurrentWeek(){
        expenseToEdit = null
        changeTab(CurrentHomeTab.CURRENT_WEEK)
    }

    private fun getCurrentUser(){
        serverConnection(
            method = { userDetailsApi.getCurrentUser() },
            onSuccess = { user -> currentUser = user },
        )
    }

    private fun initData(){
        getCurrentUser()
        serverConnection(
            method = { weekApi.getCurrentWeek() },
            onSuccess = {
                week -> currentWeek = week
                serverConnection(
                    method = { currentWeek?.let { weekApi.getWeekExpenses(weekId = it.id) } },
                    onSuccess = {
                        expenses -> currentExpenses = expenses }
                )},
        )

        getCurrencies()

    }

    private fun getCurrencies(){
        serverConnection(
            method = {weekApi.getAvailableCurrencies()},
            onSuccess = {
                    currencies -> availableCurrencies = currencies
            }
        )
    }

    private fun deleteExpense(id: String){
        serverConnection(
            method = { weekApi.deleteExpense(id)},
            onSuccess = {
                response -> serverResponse = response
                currentTab = CurrentHomeTab.CURRENT_WEEK
                initData()
            }

        )
    }

    fun newExpense(name:String,amount: String,currency: String,editMode: Boolean){
        serverResponse = null
        operationStatus = ServerOperationStatus.UNKNOWN
        if(!ValidatorService.CostValidator.isPositiveAndDouble(amount)){
            logger.error("Wrong value provided!")
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("Invalid cost provided.")
            return
        }
        if(name.isEmpty() || amount.isEmpty() || currency.isEmpty()){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("All fields required")
            return
        }
        if(editMode && name == expenseToEdit?.name && amount == expenseToEdit!!.amount.toString() && currency == currentUser?.currency ){
            operationStatus = ServerOperationStatus.ERROR
            serverResponse = ServerResponse("No changes detected.")
            return
        }
        operationStatus = ServerOperationStatus.LOADING
        serverConnection(
            method = {
                if(!editMode){
                    weekApi.addNewExpense(NewExpenseRequest(name,amount.toDouble(),currency))
                }else{
                    expenseToEdit?.let {
                        weekApi.editExpense(
                            it.id,
                            NewExpenseRequest(name,amount.toDouble(),currency)
                        )
                    }
                }
            },
            onSuccess = {
                response -> serverResponse = response
                returnToCurrentWeek()
                initData()
            }
        )

    }







}