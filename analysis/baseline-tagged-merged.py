import pandas as pd
import re

baseline = pd.read_csv("../results/heldout_results.csv")
tagged   = pd.read_csv("../results/catgorised-heldout_results.csv")

cat_tokens = ["<NARR>", "<INFO>", "<EMO>", "<REQ>", "<OBS>"]
def strip_cat(probe):
    for t in cat_tokens:
        probe = probe.replace(t, "")
    return probe.strip()

def normalise(probe):
    for t in cat_tokens:
        probe = probe.replace(t, "")
    return re.sub(r"\s+", " ", probe).strip()

tagged["probe_clean"]   = tagged["probe"].apply(normalise)
baseline["probe_clean"] = baseline["probe"].apply(normalise)  # same function both sides

b2 = baseline[baseline["order"] == 2][["probe_clean", "perplexity"]].rename(columns={"perplexity": "baseline_ppl"})
t2 = tagged[tagged["order"] == 2][["probe_clean", "perplexity"]].rename(columns={"perplexity": "tagged_ppl"})

merged = b2.merge(t2, on="probe_clean", how="inner")
merged["delta"] = merged["tagged_ppl"] - merged["baseline_ppl"]   # negative = category improved with tagging

cats = pd.read_csv("../results/heldout_categorised.csv")  # category, input, ...
cats["probe_clean"] = cats["input"].str.strip()
merged = merged.merge(cats[["probe_clean", "category"]], on="probe_clean", how="left")

print(len(merged))

print(merged.groupby("category")["delta"].agg(["mean", "count"]))
