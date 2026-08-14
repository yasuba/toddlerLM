import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("../results/zipf.csv", names=["rank", "word", "frequency"])

plt.figure(figsize=(7, 5))
plt.scatter(df["rank"], df["frequency"], s=15, color="#2a78d6")

plt.xscale("log")
plt.yscale("log")

plt.xlabel("rank (log)")
plt.ylabel("frequency (log)")
plt.title("toddlerLM vocabulary — Zipf distribution")
plt.savefig("zipf.png", dpi=150, bbox_inches="tight")
print("saved zipf.png")

