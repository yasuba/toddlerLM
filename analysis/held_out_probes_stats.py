import pandas as pd

results = pd.read_csv("../results/heldout_results.csv")   # has probe, order, perplexity, oov, etc.
categories = pd.read_csv("../results/heldout_categorised.csv")  # your category,input,response file

# keep only n=2 rows
r2 = results[results["order"] == 2].copy()

# # join category on probe text — the probe column in results must match the input column in categories
# merged = r2.merge(categories, left_on="probe", right_on="input", how="left")
#
# # group OOV by category
# print(merged.groupby("category")["oovCount"].agg(["mean", "sum", "count"]))


r2["oov_rate"] = r2["oovCount"] / r2["goldLength"]
merged = r2.merge(categories, left_on="probe", right_on="input", how="left")
print(merged.groupby("category")["oov_rate"].agg(["mean", "count"]))
