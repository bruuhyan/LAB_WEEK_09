package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

//Previously we extend AppCompatActivity,
//now we extend ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Here, we use setContent instead of setContentView
        setContent {
            //Here, we wrap our content with the theme
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    //We use MaterialTheme.colorScheme.background to get the background color
                    //and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Here, we call the Home composable
                    val list = listOf("Tanu", "Tina", "Tono")
                    Home(list)
                }
            }
        }
    }
}

//Here, instead of defining it in an XML file,
//we create a composable function called Home

@Composable
fun Home(
    //Here, we define a parameter called items
    items: List<String>,
) {
    //Here, we use LazyColumn to lazily display a list of items
    LazyColumn {
        //Here, we use item to display an item inside the LazyColumn
        item {
            Column(
                //Modifier.padding(16.dp) is used to add padding to the Column
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                //Alignment.CenterHorizontally is used to align the Column horizontally
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.enter_item))
                //Here, we use TextField to display a text input field
                TextField(
                    //Set the value of the input field
                    value = "",
                    //Set the keyboard type of the input field
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    //Set what happens when the value of the input field changes
                    onValueChange = { }
                )
                //Here, we use Button to display a button
                //the onClick parameter is used to set what happens when the button is clicked
                Button(onClick = { }) {
                    //Set the text of the button
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }
        //Here, we use items to display a list of items inside the LazyColumn
        items(items) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)
            }
        }
    }
}

//Preview as in modul example
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(listOf("Tanu", "Tina", "Tono"))
}
