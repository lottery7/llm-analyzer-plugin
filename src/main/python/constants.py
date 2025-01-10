import os

BENCHMARK_ROOT = os.path.abspath("./benchmark")

RAW_DIR = os.path.join(BENCHMARK_ROOT, "raw")
PROCESSED_DIR = os.path.join(BENCHMARK_ROOT, "processed")

ALL_PROCESSED_DIR = os.path.join(PROCESSED_DIR, "all")
ALL_PROCESSED_FILES_DIR = os.path.join(PROCESSED_DIR, "all/files")
ALL_VULNERABILITIES_FILE = os.path.join(PROCESSED_DIR, "all/vulnerabilities.json")

RESULTS_DIR = os.path.abspath("./results")

RESOURCES_DIR = os.path.abspath("./resources")
