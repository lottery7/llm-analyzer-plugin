import json
import os
import random
import shutil
import xml.etree.ElementTree as ET

from constants import *


def parse_test_metadata(xml_path):
    try:
        tree = ET.parse(xml_path)
        root = tree.getroot()

        return {
            "benchmark_version": root.findtext("benchmark-version"),
            "category": root.findtext("category"),
            "test_number": int(root.findtext("test-number")),
            "vulnerability": root.findtext("vulnerability").lower() == "true",
            "cwe": int(root.findtext("cwe")),
        }

    except ET.ParseError as e:
        print(f"XML parse error: {e}")
        return None

    except FileNotFoundError:
        print(f'"{xml_path}" not found')
        return None


def load_vulnerabilities_from(path: str):
    with open(os.path.join(path, "vulnerabilities.json")) as f:
        return json.load(f)


def load_all_vulnerabilities():
    return load_vulnerabilities_from(os.path.join(PROCESSED_DIR, "all"))


def get_all_cwe():
    with open(os.path.join(ALL_PROCESSED_DIR, "info.json")) as f:
        cwe = json.load(f).keys()
    return list(map(int, cwe))


def dump_vulnerabilities_of(path: str):
    all_vulnerabilities = load_all_vulnerabilities()
    vulnerabilities = {}

    for filename in os.listdir(os.path.join(path, "files")):
        vulnerabilities[filename] = all_vulnerabilities[filename]

    with open(os.path.join(path, "vulnerabilities.json"), "w") as f:
        json.dump(vulnerabilities, f, indent=4, ensure_ascii=False)


def dump_info_of(path: str):
    all_vulnerabilities = load_all_vulnerabilities()
    info = {}

    for filename in os.listdir(os.path.join(path, "files")):
        is_vulnerable = all_vulnerabilities[filename]["vulnerability"]
        cwe = all_vulnerabilities[filename]["cwe"]

        if cwe not in info:
            info[cwe] = {
                "vulnerable": 0,
                "not_vulnerable": 0,
            }

        info[cwe][("" if is_vulnerable else "not_") + "vulnerable"] += 1

    with open(os.path.join(path, "info.json"), "w") as f:
        json.dump(info, f, indent=4, ensure_ascii=False)


def clear_processed_data():
    try:
        shutil.rmtree(ALL_PROCESSED_DIR)
    except FileNotFoundError as e:
        print(e)


def process_raw_data():
    clear_processed_data()

    vulnerabilities = {}

    os.makedirs(ALL_PROCESSED_FILES_DIR)

    for filename in sorted(os.listdir(RAW_DIR)):
        path = os.path.join(RAW_DIR, filename)

        if filename.endswith(".xml"):
            data = parse_test_metadata(path)
            filename = filename[:-4] + ".java"
            vulnerabilities[filename] = data

        else:
            with open(path) as src:
                with open(os.path.join(ALL_PROCESSED_FILES_DIR, filename), "w") as dst:
                    dst.writelines(src.readlines()[19:])

    with open(ALL_VULNERABILITIES_FILE, "w") as f:
        json.dump(vulnerabilities, f, indent=4, ensure_ascii=False)


def copy_random(count: int, exclude: list[str] = None):
    if exclude is None:
        exclude = []

    dst_dir = os.path.join(PROCESSED_DIR, str(count))
    os.makedirs(os.path.join(dst_dir, "files"))

    filenames = os.listdir(ALL_PROCESSED_FILES_DIR)

    for filename in exclude:
        try:
            filenames.remove(filename)
        except ValueError:
            print(filename, "is absent, skip")

    for filename in random.choices(filenames, k=count):
        shutil.copyfile(
            os.path.join(ALL_PROCESSED_FILES_DIR, filename),
            os.path.join(dst_dir, "files", filename),
        )

    dump_vulnerabilities_of(dst_dir)


def main():
    pass


if __name__ == "__main__":
    main()
