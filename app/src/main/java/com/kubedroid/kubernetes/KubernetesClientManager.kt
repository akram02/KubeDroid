package com.kubedroid.kubernetes

import com.kubedroid.data.ClusterConfig
import io.fabric8.kubernetes.client.Config
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

class KubernetesClientManager {
    private var currentClient: KubernetesClient? = null
    private var currentCluster: ClusterConfig? = null

    suspend fun connectToCluster(clusterConfig: ClusterConfig): Result<KubernetesClient> = withContext(Dispatchers.IO) {
        try {
            // Close existing client if any
            currentClient?.close()

            val config = buildConfig(clusterConfig)
            val client = KubernetesClientBuilder()
                .withConfig(config)
                .build()

            // Test connection
            client.namespaces().list()

            currentClient = client
            currentCluster = clusterConfig
            Result.success(client)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentClient(): KubernetesClient? = currentClient

    fun getCurrentCluster(): ClusterConfig? = currentCluster

    fun disconnect() {
        currentClient?.close()
        currentClient = null
        currentCluster = null
    }

    private fun buildConfig(clusterConfig: ClusterConfig): Config {
        val configBuilder = ConfigBuilder()
            .withMasterUrl(clusterConfig.serverUrl)
            .withTrustCerts(clusterConfig.insecureSkipTlsVerify)

        // Set authentication
        when {
            !clusterConfig.token.isNullOrBlank() -> {
                configBuilder.withOauthToken(clusterConfig.token)
            }
            !clusterConfig.clientCertificate.isNullOrBlank() && !clusterConfig.clientKey.isNullOrBlank() -> {
                configBuilder
                    .withClientCertData(clusterConfig.clientCertificate)
                    .withClientKeyData(clusterConfig.clientKey)
            }
            !clusterConfig.username.isNullOrBlank() && !clusterConfig.password.isNullOrBlank() -> {
                configBuilder
                    .withUsername(clusterConfig.username)
                    .withPassword(clusterConfig.password)
            }
        }

        // Set CA certificate if provided
        if (!clusterConfig.certificateAuthority.isNullOrBlank()) {
            configBuilder.withCaCertData(clusterConfig.certificateAuthority)
        }

        return configBuilder.build()
    }
}
