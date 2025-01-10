def get_classifier_metrics(pred: dict, real: dict) -> dict:
    tp = tn = fp = fn = 0

    for filename in real:
        is_vulnerable = real[filename]["vulnerability"]
        cwe = real[filename]["cwe"]

        found = pred.get(filename, [])

        if cwe in found:
            if is_vulnerable:
                tp += 1
            else:
                fp += 1
        else:
            if is_vulnerable:
                fn += 1
            else:
                tn += 1

    return {
        "true_positive": tp,
        "true_negative": tn,
        "false_positive": fp,
        "false_negative": fn,
        "accuracy": (tp + tn) / (tp + tn + fp + fn),
        "recall": tp / (tp + fn),
        "precision": tp / (tp + fp),
        "F1_score": 2 * tp / (2 * tp + fp + fn),
    }
