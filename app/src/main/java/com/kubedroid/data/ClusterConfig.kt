package com.kubedroid.data

import kotlinx.serialization.Serializable

@Serializable
data class ClusterConfig(
    val id: String,
    val name: String,
    val serverUrl: String,
    val certificateAuthority: String? = null,
    val clientCertificate: String? = null,
    val clientKey: String? = null,
    val token: String? = null,
    val username: String? = null,
    val password: String? = null,
    val insecureSkipTlsVerify: Boolean = false,
    val currentContext: Boolean = false
)

@Serializable
data class ClusterConfigList(
    val clusters: List<ClusterConfig> = emptyList()
)
