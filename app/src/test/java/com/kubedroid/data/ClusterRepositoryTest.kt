package com.kubedroid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ClusterRepositoryTest {

    @Test
    fun clusterConfig_createsWithCorrectData() {
        val cluster = ClusterConfig(
            id = "test-id",
            name = "Test Cluster",
            serverUrl = "https://k8s.example.com:6443",
            token = "test-token"
        )

        assertEquals("test-id", cluster.id)
        assertEquals("Test Cluster", cluster.name)
        assertEquals("https://k8s.example.com:6443", cluster.serverUrl)
        assertEquals("test-token", cluster.token)
        assertFalse(cluster.insecureSkipTlsVerify)
    }

    @Test
    fun clusterConfig_defaultValuesAreNull() {
        val cluster = ClusterConfig(
            id = "test-id",
            name = "Test Cluster",
            serverUrl = "https://k8s.example.com:6443"
        )

        assertNull(cluster.certificateAuthority)
        assertNull(cluster.clientCertificate)
        assertNull(cluster.clientKey)
        assertNull(cluster.token)
        assertNull(cluster.username)
        assertNull(cluster.password)
    }

    @Test
    fun clusterConfigList_handlesEmptyList() {
        val configList = ClusterConfigList()
        assertTrue(configList.clusters.isEmpty())
    }

    @Test
    fun clusterConfigList_handlesMultipleClusters() {
        val cluster1 = ClusterConfig(
            id = "1",
            name = "Cluster 1",
            serverUrl = "https://k8s1.example.com:6443"
        )
        val cluster2 = ClusterConfig(
            id = "2",
            name = "Cluster 2",
            serverUrl = "https://k8s2.example.com:6443"
        )

        val configList = ClusterConfigList(clusters = listOf(cluster1, cluster2))
        assertEquals(2, configList.clusters.size)
        assertEquals("Cluster 1", configList.clusters[0].name)
        assertEquals("Cluster 2", configList.clusters[1].name)
    }
}
