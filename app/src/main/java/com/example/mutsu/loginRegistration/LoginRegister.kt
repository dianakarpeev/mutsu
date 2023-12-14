package com.example.mutsu.loginRegistration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mutsu.R
import java.util.regex.Pattern

data class User (var username : String, var password : String, var name : String )

@Composable
fun LoginRegisterScreen(
    authViewModel: AuthViewModel = viewModel(factory= AuthViewModelFactory())
) {
    val defaultOption = "Register"
    val currentUser = authViewModel.currentUser().collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (currentUser.value == null){
            AuthenticationForm(
                authViewModel = authViewModel,
                currentUser = currentUser,
                initialFormType = defaultOption
            )
        }
        else {
            SignedInScreen(authViewModel, currentUser)
        }
    }
}

@Composable
fun AuthenticationForm(
    authViewModel: AuthViewModel,
    currentUser: State<Users?>,
    initialFormType: String,
    modifier: Modifier = Modifier
) {
    val goodEmailRegex = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    var formType by rememberSaveable { mutableStateOf(initialFormType) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    Column(
        modifier
            .padding(15.dp)
            .width(325.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val welcome = if (formType == "Login") "Welcome back!" else "Welcome!"
        val introduction = if (formType == "Login") "Enter your credentials to log in."
                           else "Create an account to get started"

        Text(
            text = welcome,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )

        Text(
            text = introduction,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            fontSize = 20.sp
        )

        UserFieldInput(
            label = "Enter your email",
            value = email,
            onValueChange = { email = it },
            isInvalid = email.isNotEmpty() && !goodEmailRegex.matcher(email).matches(),
            errorMessage = "That is not a valid email address. Please try again."
        )

        UserFieldInput(
            label = "Enter your password",
            value = password,
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isInvalid = password.isNotEmpty() && password.length < 8,
            errorMessage = "The password can't be less than 8 characters.",
            isPassword = true
        )

        val toggleText = if (formType == "Login") "Don't have an account? Create one"
                         else "Already have an account? Log in"

        Text(
            text = toggleText,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    formType = if (formType == "Login") "Register" else "Login"
                    email = ""
                    password = ""
                    message = ""
                },
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            fontSize = 14.sp
        )

        if (message.isNotBlank()){
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                fontSize = 14.sp
            )
        }

        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
                when {
                    email.isEmpty() || password.isEmpty() ->
                        message = "Both the email and password fields have to be filled out."

                    formType == "Login" && currentUser.value == null -> {
                        authViewModel.signIn(email, password)
                        message =
                            "The credentials you entered do not match any in our system. Please try again."
                    }

                    formType == "Register" && currentUser.value == null -> {
                        authViewModel.signUp(email, password)
                        message =
                            "Sorry, we couldn't create an account with the credentials you entered. Please try again."
                    }
                }
            }
        ) {
            Text(
                if (formType == "Login") "Sign in" else "Sign up",
                Modifier.padding(20.dp, 0.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Displays a text box for a user to fill. If the input is invalid, displays a error message
 * underneath the text box.
 *
 * @param label Label to display on top of the text box
 * @param value Value the field corresponds to
 * @param onValueChange Actions to take when the value of the text box is changed
 * @param isInvalid Whether the user input is valid or not. If true, displays an error message.
 * @param errorMessage Error message to display if the input is invalid.
 * @param keyboardOptions Keyboard options for specific cases. For example, when you want the user to
 * only be able to input numbers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFieldInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isInvalid: Boolean = false,
    errorMessage: String = "Invalid input",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions(),
    containerColor : Color = MaterialTheme.colorScheme.surface,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = label, fontWeight = FontWeight.Medium) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = 1,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    if (isInvalid) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Composable
fun SignedInScreen(
    authViewModel: AuthViewModel,
    currentUser: State<Users?>
){
    var showPopUp by rememberSaveable { mutableStateOf(false) }
    val roundedCornerRadius = 20.dp
    val buttonCornerRadius = 10.dp
    val spacedBy = 10.dp //between all elements
    val spacerHeight = 15.dp //between text "you are currently signed with.." and buttons
    val padding = 20.dp //padding of column and buttons

    Column(
        Modifier
            .fillMaxHeight()
            .padding(15.dp)
            .width(300.dp),
        verticalArrangement = Arrangement.spacedBy(spacedBy),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spacerHeight))

        Text(
            text = "Great to see you!",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )

        Text(
            text = "You are currently signed with " + currentUser.value!!.email,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontSize = 17.sp
        )

        Spacer(Modifier.height(spacerHeight))

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(roundedCornerRadius)
                )
        ) {
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    shape = RoundedCornerShape(buttonCornerRadius),
                    onClick = { authViewModel.signOut() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        "Sign Out",
                        Modifier.padding(padding, 0.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    shape = RoundedCornerShape(buttonCornerRadius),
                    onClick = { showPopUp = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        "Delete Account",
                        Modifier.padding(padding, 0.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(spacerHeight))

        Image(
            painter = painterResource(id = R.drawable.radish),
            contentDescription = null,
            Modifier.size(size = 230.dp)
        )
    }

    if (showPopUp){
        ConfirmDeleteAccount(
            confirm = authViewModel::delete,
            dismiss = { showPopUp = false }
        )
    }
}

/**
 * Displays a pop up message where users can either confirm or dismiss the deletion of their account.
 *
 * @param confirm Actions to take when the user clicks outside of the popup or the dismiss button
 * @param dismiss Actions to take when the users click on the confirm button
 */
@Composable
fun ConfirmDeleteAccount(
    confirm: () -> Unit,
    dismiss: () -> Unit = {}
){
    AlertDialog(
        icon = { Icons.Filled.Delete },
        title = { Text("Delete account") },
        text = { Text("Are you sure you want to delete your account? You won't be able to undo this action.") },
        onDismissRequest = { dismiss() },
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
            TextButton(onClick = { dismiss() }) {
                Text("Dismiss")
            }
        }
    )
}