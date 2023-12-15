package com.example.mutsu.loginRegistration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

/**
 * Composable function that displays a login/register screen. Once logged in, users can view
 * a "signed in" screen that allows them to delete their account or sign out.
 *
 * @param authViewModel The view model responsible for authentication operations.
 */
@Composable
fun LoginRegisterScreen(
    authViewModel: AuthViewModel = viewModel(factory= AuthViewModelFactory())
) {
    val defaultOption = "Register"
    val currentUser = authViewModel.currentUser().collectAsState()
    val verticalPadding = 10.dp
    val horizontalPadding = 30.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontalPadding, verticalPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (currentUser.value == null){
            AuthenticationForm(
                authViewModel = authViewModel,
                currentUser = currentUser,
                initialFormType = defaultOption,
                modifier = Modifier.fillMaxWidth()
            )
        }
        else {
            SignedInScreen(authViewModel, currentUser)
        }
    }
}

/**
 * Composable function that renders an authentication form for user login or registration.
 *
 * @param authViewModel The view model handling authentication processes.
 * @param currentUser State holding information about the current user.
 * @param initialFormType The initial type of form to display ('Login' or 'Register').
 * @param modifier Optional modifier for configuring the layout.
 */
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

    val spacedBy = 15.dp

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacedBy),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val welcome = if (formType == "Login") "Welcome back!" else "Welcome!"
        val introduction = if (formType == "Login") "Enter your credentials to log in."
                           else "Create an account to get started"

        //Headers
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

        //Form field inputs (email and password)
        UserFieldInput(
            label = "Enter your email",
            value = email,
            onValueChange = { email = it },
            isInvalid = email.isNotEmpty() && !goodEmailRegex.matcher(email).matches(),
            errorMessage = "That is not a valid email address. Please try again.",
            modifier = Modifier.fillMaxWidth()
        )

        UserFieldInput(
            label = "Enter your password",
            value = password,
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isInvalid = password.isNotEmpty() && password.length < 8,
            errorMessage = "The password can't be less than 8 characters.",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        //Clickable text that allows the user to switch between log in and register forms
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

        //Error message after users click on the submission button (i.e. incorrect credentials)
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

        //Submission button
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = Color.White
            ),
            onClick = {
                when {
                    email.isEmpty() || password.isEmpty() ->
                        message = "Both the email and password fields have to be filled out."

                    //Figured out with the help of ChatGPT (checking for success before displaying an error message)
                    formType == "Login" -> {
                        authViewModel.signIn(email, password) { success ->
                            if (!success) {
                                message =
                                    "The credentials you entered do not match any in our system. Please try again."
                            }
                        }
                    }

                    formType == "Register" -> {
                        authViewModel.signUp(email, password) { success ->
                            if (!success) {
                                message =
                                    "Sorry, we couldn't create an account with the credentials you entered. Please try again."
                            }
                        }
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
        modifier = modifier.fillMaxWidth(), // Updated to fill max width
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

/**
 * Composable function displaying the screen for a signed-in user with options for sign-out and account deletion.
 *
 * @param authViewModel The view model managing authentication operations.
 * @param currentUser State holding information about the currently signed-in user.
 */
@Composable
fun SignedInScreen(
    authViewModel: AuthViewModel,
    currentUser: State<Users?>
){
    var showPopUp by rememberSaveable { mutableStateOf(false) }
    val roundedCornerRadius = 20.dp
    val buttonCornerRadius = 10.dp
    val spacerHeight = 15.dp
    val padding = 20.dp
    val imagePadding = 35.dp

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
            .fillMaxWidth() // Updated to fill max width
    ) {
        //Headings
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

        //Button column
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(roundedCornerRadius)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //Sign out button
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

                //Delete account button
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

        //Cute little radish image!
        Image(
            painter = painterResource(id = R.drawable.radish),
            contentDescription = null,
            Modifier
                .padding(imagePadding)
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }

    //Confirm deletion before actually deleting the account
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
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Text("Dismiss")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    )
}