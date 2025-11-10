package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.net.URLDecoder
import java.net.URLEncoder

// Data model
data class Student(var name: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

@Composable
fun App(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Home { jsonString ->
                // Simpan JSON di savedStateHandle (bukan lewat URL)
                navController.currentBackStackEntry?.savedStateHandle?.set("listData", jsonString)
                navController.navigate("resultContent")
            }
        }
        composable("resultContent") {
            // Ambil JSON dari savedStateHandle
            val json = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("listData")
                ?: ""
            ResultContent(listJson = json)
        }
    }
}


@Composable
fun Home(navigateFromHomeToResult: (String) -> Unit) {
    val listData = remember { mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono")) }
    var inputField = remember { mutableStateOf(Student("")) }

    HomeContent(
        listData = listData,
        inputField = inputField.value,
        onInputValueChange = { input -> inputField.value = inputField.value.copy(name = input) },
        onButtonClick = {
            // Prevent adding empty string (Assignment 1)
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        navigateFromHomeToResult = {
            // Convert listData to JSON using Moshi and send to ResultContent
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter = moshi.adapter<List<Student>>(type)
            val json = adapter.toJson(listData.toList())
            navigateFromHomeToResult(json)
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = { onInputValueChange(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

@Composable
fun ResultContent(listJson: String) {
    // Parse JSON into List<Student> using Moshi
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = moshi.adapter<List<Student>>(type)
    val list: List<Student> = try {
        if (listJson.isBlank()) emptyList() else adapter.fromJson(listJson) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    // Display parsed list using LazyColumn
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            OnBackgroundTitleText(text = "Result (parsed JSON)")
        }
        items(list) { student ->
            Column(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = student.name)
            }
        }
        if (list.isEmpty()) {
            item {
                OnBackgroundItemText(text = "No items to display")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    LAB_WEEK_09Theme {
        // Preview - shows HomeContent sample. For simplicity preview omitted nav.
    }
}
