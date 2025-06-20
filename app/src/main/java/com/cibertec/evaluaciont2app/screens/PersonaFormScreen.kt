package com.cibertec.evaluaciont2app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.evaluaciont2app.data.model.Persona
import com.cibertec.evaluaciont2app.viewmodel.PersonaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaFormScreen(
    personaId: Int?,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    personaViewModel: PersonaViewModel = viewModel()
) {
        var nombreCompletoInput by remember { mutableStateOf("") }
    var numeroDNI by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf<String?>("") }

    var nombreError by remember { mutableStateOf(false) }
    var dniError by remember { mutableStateOf(false) }
    var direccionError by remember { mutableStateOf(false) }
    var telefonoError by remember { mutableStateOf(false) }

    val isEditing = personaId != null && personaId != -1
    val context = LocalContext.current

    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(personaId) {
        if (isEditing) {
            personaId?.let { id ->
                val personaToEdit = personaViewModel.getPersonaById(id)
                personaToEdit?.let {
                    nombreCompletoInput = "${it.nombres} ${it.apellidos}".trim()
                    numeroDNI = it.numeroDNI
                    direccion = it.direccion
                    telefono = it.telefono
                    distrito = it.distrito
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Persona" else "Registrar Nueva Persona") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Complete el formulario para agregar una persona al padrón",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // nombres completos
            OutlinedTextField(
                value = nombreCompletoInput,
                onValueChange = { newValue ->
                    nombreCompletoInput = newValue
                    nombreError = newValue.trim().isEmpty()
                },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Icono de persona") },
                isError = nombreError,
                supportingText = { if (nombreError) Text("El nombre completo es obligatorio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            // Documento de identidad
            OutlinedTextField(
                value = numeroDNI,
                onValueChange = {
                    numeroDNI = it
                    dniError = it.trim().isEmpty()
                },
                label = { Text("Documento de Identidad") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Icono de documento") },
                isError = dniError,
                supportingText = { if (dniError) Text("El documento de identidad es obligatorio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // direccion
            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    direccionError = it.trim().isEmpty()
                },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = "Icono de ubicación") },
                isError = direccionError,
                supportingText = { if (direccionError) Text("La dirección es obligatoria") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            // telefono
            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    telefonoError = it.trim().isEmpty()
                },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Filled.Call, contentDescription = "Icono de teléfono") },
                isError = telefonoError,
                supportingText = { if (telefonoError) Text("El teléfono es obligatorio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            // distrito (opcional)
            OutlinedTextField(
                value = distrito ?: "",
                onValueChange = { distrito = it },
                label = { Text("Distrito (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // btn cancelar
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Cancelar", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        nombreError = nombreCompletoInput.trim().isEmpty()
                        dniError = numeroDNI.trim().isEmpty()
                        direccionError = direccion.trim().isEmpty()
                        telefonoError = telefono.trim().isEmpty()

                        if (!nombreError && !dniError && !direccionError && !telefonoError) {
                            showConfirmationDialog = true
                        } else {
                            Toast.makeText(context, "Por favor, complete los campos obligatorios.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Icon(Icons.Filled.Description, contentDescription = "Registrar", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Registrar")
                }
            }
        }
    }
    // AlertDialog
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar Registro") },
            text = { Text("¿Está seguro de que desea registrar esta persona?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false

                        val parts = nombreCompletoInput.trim().split(" ", limit = 2)
                        val nombresParaDB = parts.getOrElse(0) { "" }
                        val apellidosParaDB = parts.getOrElse(1) { "" }

                        val persona = Persona(
                            id = if (isEditing) personaId!! else 0,
                            nombres = nombresParaDB,
                            apellidos = apellidosParaDB,
                            numeroDNI = numeroDNI.trim(),
                            direccion = direccion.trim(),
                            telefono = telefono.trim(),
                            distrito = distrito?.trim().let { if (it.isNullOrEmpty()) null else it }
                        )
                        if (isEditing) {
                            personaViewModel.updatePersona(persona)
                        } else {
                            personaViewModel.insertPersona(persona)
                        }
                        onSave()
                    }
                ) {
                    Text("Registrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}