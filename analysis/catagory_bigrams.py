import pandas as pd
import re
from collections import defaultdict

df = pd.read_csv("../src/main/resources/statistics-corpus.csv")

def tokenize(text):
    text = text.lower()
    text = re.sub(r"[.?!,]", " ", text)
    return text.split()

def bigrams(tokens):
    return list(zip(tokens[:-1], tokens[1:]))

bigram_categories = defaultdict(set)   # bigram -> set of categories it appears in
bigram_counts = defaultdict(int)       # bigram -> total occurrences

for _, row in df.iterrows():
    cats = bigrams(tokenize(row["response"]))
    for bg in cats:
        bigram_categories[bg].add(row["category"])
        bigram_counts[bg] += 1

bridges = [
    (bg, len(cats), bigram_counts[bg])
    for bg, cats in bigram_categories.items()
    if len(cats) >= 2
]
# sort by number of categories first, then total frequency
bridges.sort(key=lambda x: (-x[1], -x[2]))

for bg, ncats, count in bridges[:25]:
    print(f"{bg[0]} {bg[1]:12s}  categories={ncats}  count={count}")

