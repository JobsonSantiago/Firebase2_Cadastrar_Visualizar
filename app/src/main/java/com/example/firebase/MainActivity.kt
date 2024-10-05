package com.example.firebase

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebase.ui.theme.FirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    App(db)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseTheme {
        Greeting("Android")
    }
}
@Composable
fun App(db : FirebaseFirestore){
    var nome by remember {
        mutableStateOf("")
    }

    var telefone by remember {
        mutableStateOf("")
    }

    val clientes = mutableStateListOf<HashMap<String, String>> ()


    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

        }
        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(text = "App Firebase Firestore")
        }
        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(text = "Jobson Guilherme Alves dos Santos Santiago - 3° DS")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,  // Centraliza horizontalmente
            verticalAlignment = Alignment.CenterVertically  // Centraliza verticalmente
        ) {
            Image(
                painter = painterResource(id = R.drawable.corinthians),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .size(150.dp)  // Adjust the size of the image
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.3f)
            ) {
                Text(text = "Nome:")
            }
            Column(

            ) {
                TextField(
                    value = nome,
                    onValueChange = { nome = it  }
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.3f)
            ) {
                Text(text = "Telefone:")
            }
            Column(

            ) {
                TextField(
                    value = telefone,
                    onValueChange = { telefone = it  }
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

        }
        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center
        ){
            Button(onClick = {

                val pessoas = hashMapOf(
                    "nome" to nome,
                    "telefone" to telefone
                )

                db.collection("Clientes").add(pessoas)
                    .addOnSuccessListener { documentReference->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")}
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                db.collection("Clientes")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val lista = hashMapOf(
                                "nome" to "${document.data.get("nome")}",
                                "telefone" to "${document.data.get("telefone")}"
                            )
                            clientes.add(lista)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }

            }) {
                Text(text = "Cadastrar")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Nome:")
            }
            Column(
                Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Telefone:")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            db.collection ("Clientes")
            Column(

            ) {

                LazyColumn(modifier = Modifier.fillMaxWidth()){
                    items(clientes) { cliente ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column (modifier = Modifier.weight(0.5F)){
                                Text(text = cliente["nome"] ?: "--")
                            }
                            Column (modifier = Modifier.weight(0.5F)){
                                Text(text = cliente["telefone"] ?: "--")
                            }
                        }
                    }
                }
            }
        }
    }
}