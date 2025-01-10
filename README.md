# IntelliJ IDEA Plugin: LLM-powered Code Analysis

This IntelliJ IDEA plugin provides seamless integration with powerful code analyzers:
- **Large Language Models (LLM)**
- **Semgrep**
- **CodeQL** (only with `--build-mode=none`)

The plugin allows you to analyze code files directly from the Project View using these tools. It simplifies the setup process by leveraging Docker for running CLI-based analyzers and supports customizable configurations.

## Features
- **Code Analysis Options**: Analyze code with LLM, Semgrep, or CodeQL by right-clicking on any file in the Project View and selecting:
  - `Analyze Code -> LLM`
  - `Analyze Code -> Semgrep`
  - `Analyze Code -> CodeQL`
- **Docker Integration**: Use Docker to run CLI versions of code analysis tools.
- **Flexible Configurations**: Easily configure API keys, model settings, and Docker hosts via a JSON configuration file.

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Install required docker images:
   - For CodeQL (**3.79gb**):
   ```
   docker build -t codeql docker/codeql
   ```
   - For Semgrep (**580mb**):
   ```
   docker pull semgrep/semgrep:latest
   ```
   Semgrep image may also be downloaded automatically when you use it in the first time.
3. Build and install the plugin:
   - Open the project in IntelliJ IDEA.
   - Set configuration variables (see below)

## Usage
1. Click "Run Plugin" in your IDE.
2. In opened testing IDEA environment open any java project.
3. Right-click on a java file in the Project View.
4. Select `Analyze Code` and choose the desired analyzer.
5. View the analysis results in the corresponding files.

## Configuration
The plugin requires a configuration file located at `resources/settings.json` with the following structure:

```json
{
  "apiKey": "<your-api-key>",
  "baseUrl": "<api-base-url>",
  "model": "<llm-model-name>",
  "dockerHost": "<docker-server-host>"
}
```

On Windows, in Docker Desktop you have to go to `Settings -> General` and select "Expose daemon on tcp://localhost:2375 without TLS" option.

### Parameters
- **`apiKey`**: Your API key for accessing LLM services.
- **`baseUrl`**: The base URL for the LLM API (default is **Gemini API URL**).
- **`model`**: The LLM model to use (default is **Gemini 2.0 Flash**).
- **`dockerHost`**: The Docker server address for running CLI analyzers. `tcp://localhost:2375` (default) on Windows, `unix:///var/run/docker.sock` on Unix-like.

## Dependencies
- **Docker Desktop** or any other Docker host: Required for running CLI tools.
- **Docker-Java**: Used for Docker communication.
- **OpenAI4J**: Provides integration with LLM APIs.
