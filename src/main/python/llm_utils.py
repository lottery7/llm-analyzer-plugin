import enum
import json
import os

import google.generativeai as genai
from pydantic import BaseModel


class CWE(enum.Enum):
    CWE_22 = "CWE-22"
    CWE_78 = "CWE-78"
    CWE_79 = "CWE-79"
    CWE_89 = "CWE-89"
    CWE_90 = "CWE-90"
    CWE_327 = "CWE-327"
    CWE_328 = "CWE-328"
    CWE_330 = "CWE-330"
    CWE_501 = "CWE-501"
    CWE_614 = "CWE-614"
    CWE_643 = "CWE-643"


class Finding(BaseModel):
    cwe: CWE
    start_line_number: int
    end_line_number: int
    description: str


LLMResponse = list[Finding]


prompt_template = """Perform the code analysis and find weaknesses. Use Common Weakness Enumeration (CWE) to identify them.

```java
{code}
```

Answer in valid JSON schema ONLY."""


genai.configure(api_key=os.getenv("GEMINI_API_KEY"))


def get_prompt(path: str) -> str:
    with open(path) as f:
        lines = f.readlines()
    for i in range(len(lines)):
        lines[i] = f"{i + 1}. {lines[i]}"
    code = "".join(lines)
    prompt = prompt_template.format(code=code)
    return prompt


def generate_gemini_response(model: str, prompt: str):
    model = genai.GenerativeModel(model)
    answer = model.generate_content(
        prompt,
        generation_config=genai.GenerationConfig(
            response_mime_type="application/json",
            response_schema=LLMResponse,
        ),
    )
    return answer


def dump_raw_results(dir: str, results: dict):
    with open(os.path.join(dir, "raw-result.json"), "w") as f:
        json.dump(results, f, indent=4, ensure_ascii=False)


def load_raw_results(dir: str) -> dict:
    with open(os.path.join(dir, "raw-result.json")) as f:
        return json.load(f)
