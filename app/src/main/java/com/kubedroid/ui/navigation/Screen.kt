package com.kubedroid.ui.navigation

sealed class Screen(val route: String) {
    object Clusters : Screen("clusters")
    object ClusterDetail : Screen("cluster_detail/{clusterId}") {
        fun createRoute(clusterId: String) = "cluster_detail/$clusterId"
    }
    object Workloads : Screen("workloads")
    object Pods : Screen("pods")
    object PodDetail : Screen("pod_detail/{namespace}/{podName}") {
        fun createRoute(namespace: String, podName: String) = "pod_detail/$namespace/$podName"
    }
    object Deployments : Screen("deployments")
    object DeploymentDetail : Screen("deployment_detail/{namespace}/{deploymentName}") {
        fun createRoute(namespace: String, deploymentName: String) = "deployment_detail/$namespace/$deploymentName"
    }
    object Services : Screen("services")
    object ServiceDetail : Screen("service_detail/{namespace}/{serviceName}") {
        fun createRoute(namespace: String, serviceName: String) = "service_detail/$namespace/$serviceName"
    }
}
