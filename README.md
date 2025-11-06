# KubeDroid

A Lens alternative for Android - Kubernetes cluster management on the go.

## Overview

KubeDroid is a native Android application that provides a mobile interface for managing Kubernetes clusters. Inspired by Lens IDE, KubeDroid brings essential Kubernetes management capabilities to your Android device.

## Features

- **Multi-Cluster Management**: Add and manage multiple Kubernetes clusters
- **Resource Browsing**: View and manage Kubernetes resources including:
  - Pods
  - Deployments
  - Services
  - Namespaces
- **Real-time Monitoring**: View pod logs in real-time
- **Secure Authentication**: Support for:
  - Bearer tokens
  - Client certificates
  - Username/password authentication
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Offline Storage**: Cluster configurations stored securely on device

## Requirements

- Android 8.0 (API level 26) or higher
- Java 17
- Android Studio Hedgehog (2023.1.1) or later

## Building the Project

### Prerequisites

1. Install Android Studio
2. Install Java 17
3. Install Android SDK (API 34)

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/akram02/KubeDroid.git
   cd KubeDroid
   ```

2. Open the project in Android Studio or build from command line:
   ```bash
   ./gradlew assembleDebug
   ```

3. The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## Installation

### Install on Device

```bash
./gradlew installDebug
```

Or manually install the APK:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

### Adding a Cluster

1. Launch KubeDroid
2. Tap the '+' button on the Clusters screen
3. Enter your cluster details:
   - **Cluster Name**: A friendly name for your cluster
   - **Server URL**: Your Kubernetes API server URL (e.g., `https://kubernetes.example.com:6443`)
   - **Bearer Token**: (Optional) Your authentication token
   - **Skip TLS Verification**: Enable if using self-signed certificates (not recommended for production)

4. Tap "Add" to save the cluster configuration

### Connecting to a Cluster

1. From the Clusters screen, tap "Connect" on your desired cluster
2. Once connected, tap "View Resources" to browse cluster resources

### Viewing Resources

- Navigate through different resource types (Pods, Deployments, Services)
- Tap on any resource to view details
- For pods, view logs by opening the pod detail screen

## Configuration

### Authentication Methods

KubeDroid supports multiple authentication methods:

#### Bearer Token
```kotlin
ClusterConfig(
    name = "My Cluster",
    serverUrl = "https://k8s.example.com:6443",
    token = "your-bearer-token"
)
```

#### Client Certificate
```kotlin
ClusterConfig(
    name = "My Cluster",
    serverUrl = "https://k8s.example.com:6443",
    clientCertificate = "base64-encoded-cert",
    clientKey = "base64-encoded-key"
)
```

#### Username/Password
```kotlin
ClusterConfig(
    name = "My Cluster",
    serverUrl = "https://k8s.example.com:6443",
    username = "admin",
    password = "password"
)
```

## Architecture

KubeDroid follows modern Android development best practices:

- **UI Layer**: Jetpack Compose with Material Design 3
- **State Management**: ViewModel with StateFlow
- **Navigation**: Jetpack Navigation Compose
- **Data Persistence**: DataStore Preferences
- **Kubernetes Client**: Fabric8 Kubernetes Client
- **Concurrency**: Kotlin Coroutines

### Project Structure

```
com.kubedroid/
├── data/                    # Data models and repositories
│   ├── ClusterConfig.kt
│   └── ClusterRepository.kt
├── kubernetes/              # Kubernetes client and operations
│   ├── KubernetesClientManager.kt
│   └── KubernetesRepository.kt
├── ui/
│   ├── navigation/         # Navigation graph
│   ├── screens/            # Compose UI screens
│   ├── theme/              # Material Design theme
│   └── viewmodel/          # ViewModels
└── MainActivity.kt
```

## Dependencies

- **Kubernetes Client**: `io.fabric8:kubernetes-client:6.9.2`
- **Jetpack Compose**: Material Design 3, Navigation
- **Kotlin Coroutines**: Async operations
- **DataStore**: Preferences storage
- **Kotlinx Serialization**: JSON serialization

## Security Considerations

- Store sensitive data (tokens, certificates) securely using Android Keystore in production
- Avoid using `insecureSkipTlsVerify` in production environments
- Consider implementing biometric authentication for app access
- Regularly rotate access tokens and credentials

## Known Limitations

- No support for exec into pods (shell access)
- Limited resource editing capabilities
- No YAML editing
- Metrics and monitoring require additional implementation

## Roadmap

- [ ] Shell access to containers
- [ ] Resource editing with YAML
- [ ] Metrics and performance monitoring
- [ ] Support for more resource types (ConfigMaps, Secrets, etc.)
- [ ] Dark theme support
- [ ] Namespace filtering
- [ ] Real-time resource updates using watchers
- [ ] Export/import cluster configurations

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Inspired by [Lens IDE](https://k8slens.dev/)
- Built with [Fabric8 Kubernetes Client](https://github.com/fabric8io/kubernetes-client)
- UI designed with [Jetpack Compose](https://developer.android.com/jetpack/compose)

## Support

For issues, questions, or contributions, please open an issue on GitHub.

## Disclaimer

This is an open-source project and is not affiliated with or endorsed by Lens, Mirantis, or the Cloud Native Computing Foundation.