# IntelliJ IDEA Plugin: Code Analysis with LLM, Semgrep, and PVS-Studio

This IntelliJ IDEA plugin provides seamless integration with powerful code analyzers:
- **Large Language Models (LLM)**, such as `llama3-70b-8192`
- **Semgrep**
- **PVS-Studio**
-  (+ **CodeQL** in the future)

The plugin allows you to analyze code files directly from the Project View using these tools. It simplifies the setup process by leveraging Docker for running CLI-based analyzers and supports customizable configurations.

## Features
- **Code Analysis Options**: Analyze code with LLM, Semgrep, or PVS-Studio by right-clicking on any file in the Project View and selecting:
  - `Analyze Code -> LLM`
  - `Analyze Code -> Semgrep`
  - `Analyze Code -> PVS-Studio`
- **Docker Integration**: Use Docker to run CLI versions of Semgrep and PVS-Studio.
- **Flexible Configurations**: Easily configure API keys, model settings, and Docker hosts via a JSON configuration file.

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
3. Build and install the plugin:
   - Open the project in IntelliJ IDEA.
   - Set configuration variables (see below)
   - Build the project using the IntelliJ IDEA plugin development tools.

## Usage
1. Click "Run Plugin" in your IDE.
2. In opened testing IDEA environment open any java project.
3. Right-click on a java file in the Project View.
4. Select `Analyze Code` and choose the desired analyzer:
   - `LLM`
   - `Semgrep`
   - `PVS-Studio`
5. View the analysis results in the corresponding files.

## Configuration
The plugin requires a configuration file located at `resources/settings.json` with the following structure:

```json
{
  "apiKey": "<your_api_key>",
  "baseUrl": "<api_base_url>",
  "model": "<LLM_model_name>",
  "dockerHost": "<docker_server_host>"
}
```

On Windows, in Docker Desktop you have to go to `Settings -> General` and select "Expose daemon on tcp://localhost:2375 without TLS" option.

### Parameters
- **`apiKey`**: Your API key for accessing LLM services.
- **`baseUrl`**: The base URL for the LLM API (default: `groq`).
- **`model`**: The LLM model to use (default: `llama3-70b-8192`).
- **`dockerHost`**: The Docker server address for running CLI analyzers.

## Dependencies
- **Docker Desktop** or any other Docker host: Required for running Semgrep and PVS-Studio CLI tools.
- **Docker-Java**: Used for Docker communication.
- **OpenAI4J**: Provides integration with LLM APIs.

## Contributing
Contributions are welcome! Please submit issues or pull requests to improve the plugin.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---
Feel free to reach out if you have questions or suggestions for improvement!

