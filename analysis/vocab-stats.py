import pandas as pd
import re

df = pd.read_csv("../src/main/resources/statistics-corpus.csv")

df["pair_text"] = df["input"] + " " + df["response"]

def tokenize(text):
    text = text.lower()
    text = re.sub(r"[.?!,]", " ", text)   # strip sentence punctuation; keep apostrophes
    return text.split()

def vocab_stats(group):
    tokens = []
    for text in group["pair_text"]:
        tokens.extend(tokenize(text))
    return len(set(tokens)) / len(tokens)

result = df.groupby("category").apply(vocab_stats, include_groups=False)
print(result)