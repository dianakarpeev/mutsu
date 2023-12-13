package com.example.mutsu.loginRegistration

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import java.util.regex.Pattern

data class User (var username : String, var password : String, var name : String )

@Composable
fun LoginRegisterScreen(
    authViewModel: AuthViewModel = viewModel(factory= AuthViewModelFactory())
) {
    var optionShown by rememberSaveable { mutableStateOf("") }
    var currentUser = authViewModel.currentUser().collectAsState()

    if (currentUser.value == null){
        Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally){
            AuthenticationForm(
                authViewModel = authViewModel,
                currentUser = currentUser,
                formType = optionShown
            )
        }
    }
    else {
        SignedInScreen(modifier = Modifier)
    }
}

fun isNullOrEmpty(input: String?): Boolean {
    return input == null || input.isEmpty()
}

@Composable
fun AuthenticationForm(
    authViewModel: AuthViewModel,
    currentUser: State<Users?>,
    formType: String,
    modifier: Modifier = Modifier
) {
    val goodEmailRegex = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    Column(modifier.padding(15.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email: ") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter your password: ") }
        )

        Text("$message")

        Button(onClick = {
            val emailMatches = goodEmailRegex.matcher(email)

            if (isNullOrEmpty(email) || isNullOrEmpty(password))
                message = "Both the email and password fields have to be filled out."
            else if (!emailMatches.matches())
                message = "That is not a valid email address. Please try again."
            else {
                when (formType) {
                    "Login" -> {
                        authViewModel.signIn(email, password)
                        if (currentUser.value == null)
                            message = "The credentials you entered do not match any in our system. Please try again."
                    }
                    "Register" -> {
                        if (password.length < 8)
                            message = "The password can't be less than 8 characters."
                        else {
                            authViewModel.signUp(email, password)
                            if (currentUser.value == null)
                                message = "Sorry, we couldn't create an account with the credentials you entered. Please try again."
                        }
                    }
                }
            }
        }) {
            Text(if (formType == "Login") "Login here" else "Register here")
        }
    }
}

@Composable
fun SignedInScreen(modifier: Modifier){
    var showPopUp by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.padding(15.dp)){
        Text("Congrats! You are signed in!")
        Row {
            Button(onClick = { authViewModel.signOut() }){
                Text("Sign out")
            }

            Button(onClick = { showPopUp = true }){
                Text("Delete account")
            }

            if (showPopUp){
                ConfirmDeleteAccount(confirm = authViewModel::delete, dismiss = {showPopUp = false})
            }
        }
    }
}

/*
 * got this code and modified it a bit from here:
 * https://developer.android.com/jetpack/compose/components/dialog#alert
 */
@Composable
fun ConfirmDeleteAccount(confirm: () -> Unit, dismiss: () -> Unit = {}){
    AlertDialog(
        icon = {
            Icons.Filled.Delete
        },
        title = {
            Text("Delete account")
        },
        text = {
            Text("Are you sure you want to delete your account? You won't be able to undo this action.")
        },
        onDismissRequest = {
            dismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    confirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}