package com.example.emptyactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class User ( val username : String, val password : String, val name : String )

@Composable
fun LoginRegisterScreen(modifier: Modifier = Modifier){
    var tempUserData = mutableListOf<User> ()
    tempUserData.add(User("hellokitty", "kitty1234", "katrina"))
    tempUserData.add(User("dance4life", "hip82hop", "devin"))
    tempUserData.add(User("maarrrllliie", "pASSW0Rd", "marls <3"))
    tempUserData.add(User("__ice__tea__", "nine8seven", "trin"))
    tempUserData.add(User("em.meme.em", "3l3vator", "E M E L I O"))

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
            Login(tempUserData, modifier.padding(15.dp))
        }
        else if (optionShown == "Register"){
            Register(tempUserData, modifier.padding(15.dp))
        }
    }
}

@Composable
fun Login(users: MutableList<User>, modifier: Modifier = Modifier){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    Column(modifier.padding(15.dp)){
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter your username: ")}
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter your password: ")}
        )

        Button(onClick = {
            message = ""
            if (username == "" || password == ""){
                message = "You must enter a username and a password to login."
            }
            for (u in users){
                if (u.username == username){
                    if (u.password == password){
                        message = "Welcome $username! You are now logged in."
                        break
                    }
                    else {
                        message = "Incorrect password."
                        break
                    }
                }
            }
            if (message == ""){
                message = "Invalid username."
            }
        }){
            Text("Let's go!")
        }
        Text("$message")
    }
}

@Composable
fun Register(users: List<User>, modifier: Modifier = Modifier){
    Text("Now you can register")
}