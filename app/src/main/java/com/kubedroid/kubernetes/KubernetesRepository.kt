package com.kubedroid.kubernetes

import io.fabric8.kubernetes.api.model.ContainerStatus
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.api.model.apps.StatefulSet
import io.fabric8.kubernetes.api.model.apps.DaemonSet
import io.fabric8.kubernetes.client.KubernetesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class KubernetesRepository(private val clientManager: KubernetesClientManager) {

    suspend fun getNamespaces(): Result<List<Namespace>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val namespaces = client.namespaces().list().items
            Result.success(namespaces)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPods(namespace: String? = null): Result<List<Pod>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val pods = if (namespace != null) {
                client.pods().inNamespace(namespace).list().items
            } else {
                client.pods().inAnyNamespace().list().items
            }
            Result.success(pods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDeployments(namespace: String? = null): Result<List<Deployment>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val deployments = if (namespace != null) {
                client.apps().deployments().inNamespace(namespace).list().items
            } else {
                client.apps().deployments().inAnyNamespace().list().items
            }
            Result.success(deployments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getServices(namespace: String? = null): Result<List<Service>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val services = if (namespace != null) {
                client.services().inNamespace(namespace).list().items
            } else {
                client.services().inAnyNamespace().list().items
            }
            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStatefulSets(namespace: String? = null): Result<List<StatefulSet>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val statefulSets = if (namespace != null) {
                client.apps().statefulSets().inNamespace(namespace).list().items
            } else {
                client.apps().statefulSets().inAnyNamespace().list().items
            }
            Result.success(statefulSets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDaemonSets(namespace: String? = null): Result<List<DaemonSet>> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val daemonSets = if (namespace != null) {
                client.apps().daemonSets().inNamespace(namespace).list().items
            } else {
                client.apps().daemonSets().inAnyNamespace().list().items
            }
            Result.success(daemonSets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPodLogs(namespace: String, podName: String, containerName: String? = null): Result<String> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            val logRequest = client.pods().inNamespace(namespace).withName(podName)
            val logs = if (containerName != null) {
                logRequest.inContainer(containerName).log
            } else {
                logRequest.log
            }
            Result.success(logs ?: "No logs available")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePod(namespace: String, podName: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            client.pods().inNamespace(namespace).withName(podName).delete()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDeployment(namespace: String, deploymentName: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            client.apps().deployments().inNamespace(namespace).withName(deploymentName).delete()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteService(namespace: String, serviceName: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val client = clientManager.getCurrentClient()
                ?: return@withContext Result.failure(Exception("Not connected to cluster"))

            client.services().inNamespace(namespace).withName(serviceName).delete()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
