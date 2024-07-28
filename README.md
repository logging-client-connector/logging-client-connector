[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/logging-client-connector/logging-client-connector/blob/main/LICENSE)
[![](https://jitpack.io/v/logging-client-connector/logging-client-connector.svg)](https://jitpack.io/#logging-client-connector/logging-client-connector)
[![codecov](https://codecov.io/gh/logging-client-connector/logging-client-connector/graph/badge.svg?token=1Y0QWR8XGT)](https://codecov.io/gh/logging-client-connector/logging-client-connector)

# Logging Client Connector

Logging Client Connector is a library that adds the possibility to log requests and responses in Spring WebClient.

## Getting Started

### Prerequisites

To use the Logging Client Connector, you need to add the following repository and dependency to your `build.gradle`
file:

```groovy
repositories {
    // ...    
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.logging-client-connector:logging-client-connector:1.0.0'
}
```

### Example Usage

```java
import org.loggingclientconnector.LoggingClientConnector;
import org.springframework.web.reactive.function.client.WebClient;

// Create a LoggingClientConnector instance
var loggingClientConnector = LoggingClientConnector.create();

// Configure the WebClient to use the LoggingClientConnector
var webclient = WebClient.builder()
        .clientConnector(loggingClientConnector)
        .build();
```

### Example Usage with blocklist

```java
import org.loggingclientconnector.LoggingClientConnector;
import org.loggingclientconnector.customizer.Blocklist;

import static org.loggingclientconnector.LogLevel.INFO;

// spring webclient 
import org.springframework.web.reactive.function.client.WebClient;

// Create and configure a LoggingClientConnector instance
LoggingClientConnector loggingClientConnector = LoggingClientConnector.create()
        .configure(configuration -> configuration
                .blocklistConfig(config -> config
                        .requestHeaderBlocklist(Blocklist.of("Authorization"))
                        .requestBlocklist(Blocklist.of("secret"))
                ));

// Configure the WebClient to use the LoggingClientConnector
WebClient webclient = WebClient.builder()
        .clientConnector(loggingClientConnector)
        .build();

// Use the WebClient to make a POST request with sensitive data
var response = webclient.post()
        .uri("http://localhost:8080/protected")
        .bodyValue(Map.of(
                "secret", "this is a secret",
                "test", "that should be logged"))
        .headers(headers -> headers.setBearerAuth("token"))
        .retrieve()
        .bodyToMono(String.class)
        .block();
```

The resulting log will show the sensitive information as [PROTECTED]:

```text
01:16:56.881 [reactor-http-epoll-2] INFO  Request Logger - 
Request: [
  uri: http://localhost:45683/protected
  method: POST
  headers: [Authorization=[PROTECTED], Content-Length=61, Content-Type=application/json]
  body: 
  {
      "secret" : "[PROTECTED]",
      "test" : "that should be logged"
  }
  cookies: []
]
```