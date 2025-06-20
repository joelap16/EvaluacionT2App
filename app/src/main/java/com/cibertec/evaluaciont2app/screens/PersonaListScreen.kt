package com.cibertec.evaluaciont2app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.evaluaciont2app.data.model.Persona
import com.cibertec.evaluaciont2app.viewmodel.PersonaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaListScreen(
    personaViewModel: PersonaViewModel = viewModel(),
    onNavigateToForm: (Int?) -> Unit
) {
    val personas by personaViewModel.personas.collectAsState()
    val searchQuery by personaViewModel.currentQuery.collectAsState()
    val personaCount by personaViewModel.personaCount.collectAsState()
    val snackbarMessage by personaViewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
            personaViewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Padrón de Personas", fontWeight = FontWeight.Bold, fontSize = 32.sp) },
                actions = {
                    Button(
                        onClick = { onNavigateToForm(null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Filled.Person, "Registrar Persona", modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Registrar Persona")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Gestione el registro de personas en el sistema",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { personaViewModel.updateSearchQuery(it) },
                label = { Text("Buscar por nombre o documento...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mostrando ${personas.size} de $personaCount registros",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nombre",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = "Documento",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.5f)
                )
                Spacer(modifier = Modifier.width(96.dp))
            }
            Divider()

            // lista de personas
            if (personas.isEmpty() && searchQuery.isEmpty()) {
                Text(
                    text = "No hay personas registradas aún. ¡Agrega una nueva!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (personas.isEmpty() && searchQuery.isNotEmpty()) {
                Text(
                    text = "No se encontraron personas para '${searchQuery}'.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(personas, key = { it.id }) { persona ->
                        PersonaListItem(
                            persona = persona,
                            onViewClick = {  },
                            onEditClick = { onNavigateToForm(persona.id) },
                            onDeleteClick = { personaViewModel.deletePersona(persona) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun PersonaListItem(
    persona: Persona,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // nombre
        Row(modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Person, contentDescription = "Persona", modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${persona.nombres} ${persona.apellidos}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }

        // documento
        Row(modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Description, contentDescription = "Documento", modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = persona.numeroDNI,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }

        // acciones
        Row(
            modifier = Modifier.width(96.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onViewClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = "Ver detalles",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar persona",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar persona",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}