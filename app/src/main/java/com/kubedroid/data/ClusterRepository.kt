package com.kubedroid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "clusters")

class ClusterRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val clustersKey = stringPreferencesKey("clusters_json")

    val clustersFlow: Flow<List<ClusterConfig>> = context.dataStore.data
        .map { preferences ->
            preferences[clustersKey]?.let { jsonString ->
                json.decodeFromString<ClusterConfigList>(jsonString).clusters
            } ?: emptyList()
        }

    suspend fun addCluster(cluster: ClusterConfig) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[clustersKey]?.let { jsonString ->
                json.decodeFromString<ClusterConfigList>(jsonString).clusters
            } ?: emptyList()

            val newList = currentList + cluster.copy(id = UUID.randomUUID().toString())
            preferences[clustersKey] = json.encodeToString(ClusterConfigList(newList))
        }
    }

    suspend fun removeCluster(clusterId: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[clustersKey]?.let { jsonString ->
                json.decodeFromString<ClusterConfigList>(jsonString).clusters
            } ?: emptyList()

            val newList = currentList.filterNot { it.id == clusterId }
            preferences[clustersKey] = json.encodeToString(ClusterConfigList(newList))
        }
    }

    suspend fun updateCluster(cluster: ClusterConfig) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[clustersKey]?.let { jsonString ->
                json.decodeFromString<ClusterConfigList>(jsonString).clusters
            } ?: emptyList()

            val newList = currentList.map { if (it.id == cluster.id) cluster else it }
            preferences[clustersKey] = json.encodeToString(ClusterConfigList(newList))
        }
    }
}
