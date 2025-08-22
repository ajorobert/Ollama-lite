# Ollama-lite
Ollama Android Client

A simple Android client app to connect with a local or remote Ollama server (running in LAN, Termux, or on a PC). Built using Kotlin + Jetpack Compose, with clean architecture and modular design.


---

Features (Planned)

Connect to Ollama server via configurable base URL (default: http://localhost:11434).

Send chat prompts and receive responses.

Support for streaming (chunked JSON) and non-streaming modes.

Display conversation history in a chat-style UI.

Settings screen to configure server IP, port, and default model.

Error handling (network issues, server errors).

Future: Offline storage (Room DB) for chat history.



---

Tech Stack

Language: Kotlin

UI: Jetpack Compose

Networking: Retrofit + OkHttp (streaming support enabled)

DI: Hilt

JSON: Moshi or kotlinx.serialization

Async: Coroutines + Flow



---

Architecture

Presentation Layer: Jetpack Compose UI + ViewModel

Domain Layer: UseCases for sending prompts & receiving responses

Data Layer: Repository using Retrofit for Ollama API



---

Ollama API Basics

Ollama exposes an HTTP API at http://<host>:11434/api.

Generate (non-streaming)

POST /api/generate
Content-Type: application/json

{
  "model": "llama2",
  "prompt": "Hello world",
  "stream": false
}

Response:

{
  "model": "llama2",
  "created_at": "2023-08-21T15:04:05Z",
  "response": "Hello back!",
  "done": true
}

Generate (streaming)

POST /api/generate
{
  "model": "llama2",
  "prompt": "Hello world",
  "stream": true
}

Response stream (NDJSON):

{"response":"Hello"}
{"response":" back!"}
{"done":true}


---

Setup & Development

Prerequisites

Android Studio Ladybug (or later)

JDK 17+

Ollama server running on LAN (Ubuntu, Termux, or PC)


Build & Run

1. Clone this repo.


2. Open in Android Studio.


3. Add server IP in local.properties (e.g. ollama.baseUrl=http://192.168.1.50:11434).


4. Run on device/emulator.


---

Creating a Release

This project includes a `release.sh` script to automate the process of building the APK and creating a GitHub release.

**Prerequisites:**

*   **GitHub CLI (`gh`):** You must have the GitHub CLI installed. You can find installation instructions at [cli.github.com](https://cli.github.com).
*   **GitHub Token:** You must have a GitHub Personal Access Token with `repo` scope. This token should be exported as an environment variable named `GITHUB_TOKEN`.
    ```bash
    export GITHUB_TOKEN=your_github_token_here
    ```

**Usage:**

To create a new release, run the script with a version tag:

```bash
./release.sh v1.0.0
```

The script will then build the debug APK, create a new release on GitHub with the specified tag, and upload the APK as a release asset.


---

Example Retrofit Interface

interface OllamaApi {
    @POST("/api/generate")
    suspend fun generate(@Body request: GenerateRequest): Response<GenerateResponse>

    @Streaming
    @POST("/api/generate")
    fun streamGenerate(@Body request: GenerateRequest): Flow<String>
}


---

Roadmap

[x] Basic chat UI

[x] Non-streaming prompt/response

[ ] Streaming support (Flow collector)

[ ] Configurable server settings

[ ] Persistent chat history

[ ] Theming + Markdown rendering



---

License

MIT

