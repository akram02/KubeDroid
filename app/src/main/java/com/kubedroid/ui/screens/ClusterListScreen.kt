package com.kubedroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kubedroid.data.ClusterConfig
import com.kubedroid.ui.viewmodel.ClusterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClusterListScreen(
    uiState: ClusterUiState,
    onAddCluster: (ClusterConfig) -> Unit,
    onConnectCluster: (ClusterConfig) -> Unit,
    onRemoveCluster: (String) -> Unit,
    onNavigateToResources: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clusters") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Cluster")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.clusters.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No clusters configured",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Add a cluster to get started",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.clusters) { cluster ->
                            ClusterCard(
                                cluster = cluster,
                                isConnected = cluster.id == uiState.connectedClusterId,
                                onConnect = { onConnectCluster(cluster) },
                                onRemove = { onRemoveCluster(cluster.id) },
                                onNavigateToResources = onNavigateToResources
                            )
                        }
                    }
                }
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
    }

    if (showAddDialog) {
        AddClusterDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { cluster ->
                onAddCluster(cluster)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ClusterCard(
    cluster: ClusterConfig,
    isConnected: Boolean,
    onConnect: () -> Unit,
    onRemove: () -> Unit,
    onNavigateToResources: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isConnected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        cluster.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        cluster.serverUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isConnected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Connected",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isConnected) {
                    Button(onClick = onConnect) {
                        Icon(Icons.Default.Cloud, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connect")
                    }
                } else {
                    Button(onClick = onNavigateToResources) {
                        Icon(Icons.Default.ViewList, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Resources")
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClusterDialog(
    onDismiss: () -> Unit,
    onAdd: (ClusterConfig) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var serverUrl by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var insecureSkipTls by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Cluster") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Cluster Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = serverUrl,
                    onValueChange = { serverUrl = it },
                    label = { Text("Server URL") },
                    placeholder = { Text("https://kubernetes.example.com:6443") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text("Bearer Token (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = insecureSkipTls,
                        onCheckedChange = { insecureSkipTls = it }
                    )
                    Text("Skip TLS Verification (Not Recommended)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdd(
                        ClusterConfig(
                            id = "",
                            name = name,
                            serverUrl = serverUrl,
                            token = token.ifBlank { null },
                            insecureSkipTlsVerify = insecureSkipTls
                        )
                    )
                },
                enabled = name.isNotBlank() && serverUrl.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
