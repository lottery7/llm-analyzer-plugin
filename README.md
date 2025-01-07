# IntelliJ IDEA Plugin: LLM-powered Code Analysis

This IntelliJ IDEA plugin provides seamless integration with powerful code analyzers:
- **Large Language Models (LLM)**
- **Semgrep**
- **PVS-Studio**
- **CodeQL** (only with `--build-mode=none`)

The plugin allows you to analyze code files directly from the Project View using these tools. It simplifies the setup process by leveraging Docker for running CLI-based analyzers and supports customizable configurations.

## Features
- **Code Analysis Options**: Analyze code with LLM, Semgrep, or PVS-Studio by right-clicking on any file in the Project View and selecting:
  - `Analyze Code -> LLM`
  - `Analyze Code -> Semgrep`
  - `Analyze Code -> PVS-Studio`
  - `Analyze Code -> CodeQL`
- **Docker Integration**: Use Docker to run CLI versions of code analysis tools.
- **Flexible Configurations**: Easily configure API keys, model settings, and Docker hosts via a JSON configuration file.

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Create all required docker images:
   ```
   # Go to docker folder
   cd docker

   # Go to the image folder
   cd <image-name>

   # Build the image
   docker build -t <image-name> .
   ```
   Repeat it for every folder. You can see that we don't have semgrep folder - it is because semgrep image will be downloaded automatically.

3. Build and install the plugin:
   - Open the project in IntelliJ IDEA.
   - Set configuration variables (see below)
   - Build the project using the IntelliJ IDEA plugin development tools.

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
  "model": "<LLM-model-name>",
  "dockerHost": "<docker-server-host>",
  "pvsStudioLicense": {
     "userName": "<license-username>",
     "licenseKey": "<license-key>"
   }
}
```

On Windows, in Docker Desktop you have to go to `Settings -> General` and select "Expose daemon on tcp://localhost:2375 without TLS" option.

### Parameters
- **`apiKey`**: Your API key for accessing LLM services.
- **`baseUrl`**: The base URL for the LLM API (default: `groq`).
- **`model`**: The LLM model to use (default: `llama3-70b-8192`).
- **`dockerHost`**: The Docker server address for running CLI analyzers. `tcp://localhost:2375` (default) on Windows, `unix:///var/run/docker.sock` on Unix-like.
- **`pvsStudioLicense`**: Your license for PVS-Studio. Default is a free license which requires you to add comment to the start of each scanned source file. You can see it in **resources/pvs-studio-copyright.txt**.

## Dependencies
- **Docker Desktop** or any other Docker host: Required for running CLI tools.
- **Docker-Java**: Used for Docker communication.
- **OpenAI4J**: Provides integration with LLM APIs.
