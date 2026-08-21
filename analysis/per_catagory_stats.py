import pandas as pd
import re

df = pd.read_csv("../src/main/resources/statistics-corpus.csv")

df["pair_text"] = df["input"] + " " + df["response"]

def tokenize(text):
    text = text.lower()
    text = re.sub(r"[.?!,]", " ", text)   # strip sentence punctuation; keep apostrophes
    return text.split()

def category_stats(group):
    tokens = []
    for text in group["pair_text"]:
        tokens.extend(tokenize(text))
    types = set(tokens)
    freq = pd.Series(tokens).value_counts()
    hapax = (freq == 1).sum()
    return pd.Series({
        "pairs": len(group),
        "tokens": len(tokens),
        "types": len(types),
        "ttr": len(types) / len(tokens),
        "hapax": hapax,
        "hapax_prop": hapax / len(types),
    })

result = df.groupby("category").apply(category_stats, include_groups=False)
print(result)