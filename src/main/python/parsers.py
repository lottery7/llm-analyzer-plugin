import json
import os
from collections import defaultdict

from bs4 import BeautifulSoup

from constants import RESOURCES_DIR
from process_files import get_all_cwe


def parse_semgrep(path: str) -> dict:
    with open(path) as f:
        raw_results = json.load(f)

    results = {}
    for raw_res in raw_results["results"]:
        filename = raw_res["path"].split("/")[-1]
        cwes = raw_res["extra"]["metadata"]["cwe"]

        for i in range(len(cwes)):
            cwes[i] = int(cwes[i].lstrip("CWE-").split(":")[0])

        if filename not in results:
            results[filename] = []

        for cwe in cwes:
            if cwe not in results[filename]:
                results[filename].append(cwe)

    return results


def parse_codeql_cwe_rules():
    with open(os.path.join(RESOURCES_DIR, "cwe-codeql")) as f:
        html_content = f.read()

    soup = BeautifulSoup(html_content, "html.parser")
    tbody = soup.find("tbody")

    if not tbody:
        raise ValueError("<tbody> not found")

    result = {}

    for row in tbody.find_all("tr"):
        cells = row.find_all("td")
        if len(cells) >= 3:
            cwe_id = int(cells[0].text.strip().split("-")[1])
            codeql_id = cells[2].text.strip()

            if codeql_id not in result:
                result[codeql_id] = []

            result[codeql_id].append(cwe_id)

    return result


def parse_codeql(path: str) -> dict:
    rule2cwe = parse_codeql_cwe_rules()

    with open(path) as f:
        raw_results = json.load(f)

    result = defaultdict(list)

    for run in raw_results.get("runs", []):
        for result_entry in run.get("results", []):
            rule_id = result_entry.get("ruleId")

            for location in result_entry.get("locations", []):
                file_path = (
                    location.get("physicalLocation", {})
                    .get("artifactLocation", {})
                    .get("uri")
                )
                if file_path and rule_id:
                    for cwe in rule2cwe.get(rule_id, []):
                        if cwe not in result[file_path]:
                            result[file_path].append(cwe)

    needed_cwe = get_all_cwe()

    for filename in result:
        result[filename] = list(filter(lambda cwe: cwe in needed_cwe, result[filename]))

    return dict(result)


def parse_llm(path: str) -> dict:
    with open(path) as f:
        raw_result = json.load(f)

    result = {}

    for filename in raw_result:
        for finding in raw_result[filename]:
            if filename not in result:
                result[filename] = []
            result[filename].append(int(finding["cwe"].split("-")[1]))

    return result
