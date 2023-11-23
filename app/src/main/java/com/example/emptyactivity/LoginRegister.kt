package com.example.emptyactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginRegisterScreen(modifier: Modifier = Modifier){
    var optionShown by rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text("Welcome. Click log in to access the entire app. If you don't already have an account, you can create one by clicking register.")
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            Button(onClick = {
                if (optionShown != "Login") {
                    optionShown = "Login"
                }
                else {
                    optionShown = ""
                }
            }) {
                Text("Log in")
            }

            Button(onClick = {
                if (optionShown != "Register") {
                    optionShown = "Register"
                }
                else {
                    optionShown = ""
                }
            }) {
                Text("Register")
            }
        }
        if (optionShown == "Login"){
            Login(modifier.padding(15.dp))
        }
        else if (optionShown == "Register"){
            Register(modifier.padding(15.dp))
        }
    }
}

@Composable
fun Login(modifier: Modifier = Modifier){
    Text("Now you can login")
}

@Composable
fun Register(modifier: Modifier = Modifier){
    Text("Now you can register")
}