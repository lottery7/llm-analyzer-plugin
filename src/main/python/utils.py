import json
import os
import subprocess

from constants import PROCESSED_DIR, RESULTS_DIR


def execute_command_to_file(command: str, output_file: str = "output.txt"):
    try:
        with open(output_file, "w") as output_file:
            process = subprocess.run(
                command,
                shell=True,
                stdout=output_file,
                stderr=output_file,
                text=True,
            )
            return process.returncode
    except Exception as e:
        print(e)
        return -1


def get_results_dir_for(name: str):
    path = os.path.join(RESULTS_DIR, name)
    try:
        os.makedirs(path)
    except:
        pass
    return path


def get_dir_for(name: str):
    return os.path.join(PROCESSED_DIR, name)


def get_files_dir_for(name: str):
    return os.path.join(get_dir_for(name), "files")


def dump_results(dir: str, pred: dict, metrics: dict):
    with open(os.path.join(dir, "results.json"), "w") as f:
        json.dump(pred, f, indent=4, ensure_ascii=False)

    with open(os.path.join(dir, "metrics.json"), "w") as f:
        json.dump(metrics, f, indent=4, ensure_ascii=False)
