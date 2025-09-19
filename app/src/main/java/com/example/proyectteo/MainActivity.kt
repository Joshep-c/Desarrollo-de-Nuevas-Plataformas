package com.example.proyectteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.proyectteo.ui.theme.ProyectTeoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectTeoTheme {
                YapeSimpleApp()
            }
        }
    }
}

@Composable
fun YapeSimpleApp() {
    // Estados básicos (rememberSaveable para mantenerlos en rotaciones)
    var nombre by remember { mutableStateOf("") }
    var cantidadStr by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf(5000.0) } // saldo inicial de ejemplo
    var mensajeEnvio by remember { mutableStateOf("") }

    // parsing seguro de la cantidad
    val cantidad = cantidadStr.toDoubleOrNull() ?: 0.0
    val excedeSaldo = cantidad > saldo || cantidad <= 0.0
    val botonHabilitado = nombre.isNotBlank() && cantidad > 0.0 && !excedeSaldo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo para el nombre del destinatario
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre destinatario", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00A693),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo para la cantidad a yapear
                    OutlinedTextField(
                        value = cantidadStr,
                        onValueChange = { input ->
                            cantidadStr = input.filter { it.isDigit() || it == '.' }
                        },
                        label = { Text("Monto a enviar S/", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00A693),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo que muestra el saldo actual (no editable)
                    OutlinedTextField(
                        value = "S/. ${"%.2f".format(saldo)}",
                        onValueChange = { }, // No editable
                        label = { Text("Monto actual S/", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        readOnly = true,
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                            disabledTextColor = Color.Black.copy(alpha = 0.7f),
                            disabledLabelColor = Color.Gray.copy(alpha = 0.7f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensaje de error si intenta más que el saldo
                    if (cantidadStr.isNotEmpty() && cantidad <= 0.0) {
                        Text(
                            text = "Ingresa una cantidad válida (> 0)",
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else if (cantidadStr.isNotEmpty() && excedeSaldo) {
                        Text(
                            text = "Saldo insuficiente",
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón REGISTRAR
                    Button(
                        onClick = {
                            if (botonHabilitado) {
                                saldo -= cantidad
                                mensajeEnvio = "Se envió: $nombre, S/. ${"%.2f".format(cantidad)} enviado."
                                cantidadStr = ""
                            }
                        },
                        enabled = botonHabilitado,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0), // Azul similar al de la imagen
                            disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            "REGISTRAR",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Mensaje de confirmación del envío
                    if (mensajeEnvio.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF00A693).copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = mensajeEnvio,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = Color(0xFF00695C),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
