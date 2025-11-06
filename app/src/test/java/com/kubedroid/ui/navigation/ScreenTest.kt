package com.kubedroid.ui.navigation

import org.junit.Test
import org.junit.Assert.*

class ScreenTest {

    @Test
    fun screen_hasCorrectRoutes() {
        assertEquals("clusters", Screen.Clusters.route)
        assertEquals("workloads", Screen.Workloads.route)
        assertEquals("pods", Screen.Pods.route)
        assertEquals("deployments", Screen.Deployments.route)
        assertEquals("services", Screen.Services.route)
    }

    @Test
    fun clusterDetail_createsCorrectRoute() {
        val clusterId = "test-cluster-123"
        val route = Screen.ClusterDetail.createRoute(clusterId)
        assertEquals("cluster_detail/test-cluster-123", route)
    }

    @Test
    fun podDetail_createsCorrectRoute() {
        val namespace = "default"
        val podName = "nginx-pod"
        val route = Screen.PodDetail.createRoute(namespace, podName)
        assertEquals("pod_detail/default/nginx-pod", route)
    }

    @Test
    fun deploymentDetail_createsCorrectRoute() {
        val namespace = "production"
        val deploymentName = "web-app"
        val route = Screen.DeploymentDetail.createRoute(namespace, deploymentName)
        assertEquals("deployment_detail/production/web-app", route)
    }

    @Test
    fun serviceDetail_createsCorrectRoute() {
        val namespace = "kube-system"
        val serviceName = "kubernetes-dashboard"
        val route = Screen.ServiceDetail.createRoute(namespace, serviceName)
        assertEquals("service_detail/kube-system/kubernetes-dashboard", route)
    }

    @Test
    fun routes_handleSpecialCharacters() {
        val namespace = "my-namespace"
        val podName = "pod-with-dashes-123"
        val route = Screen.PodDetail.createRoute(namespace, podName)
        assertEquals("pod_detail/my-namespace/pod-with-dashes-123", route)
    }
}
