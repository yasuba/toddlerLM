# toddlerLM

A small probabilistic language model trained on a corpus of child–caregiver
dialogue, built deliberately at the simplest end of the spectrum: word-level
n-gram counting, no neural networks, so every prediction can be traced back
to the counts behind it. The aim isn't good responses; it's a transparent
model that makes visible how a language model memorises, generalises and
fails. The model is the lens, the corpus is the subject.

## What it found

- **Implicit sub-styles.** The model reproduced response sub-styles it was
  never told about, because they were already implicit in the n-gram
  statistics. Adding explicit category tokens didn't reliably help, which
  supports this.
- **A generalisation cliff.** On held-out inputs, coverage collapses as the
  n-gram order rises: 100% at n=2, 59% at n=3, 23% at n=4. Raw perplexity
  falls over the same range, but only because the hard cases drop out, so
  perplexity alone is misleading here.
- **The corpus explains the behaviour.** It is formulaic, with many rare
  words, so most higher-order contexts have only one possible continuation
  and the model recites rather than generalises.

The full write-up, with method, results per stage, error analysis and
limitations, is in [docs/findings.md](./docs/findings.md).

## The corpus

237 input–response pairs in five categories (narrative, information-seeking,
emotional acknowledgement, request/demand, observation), written under a fixed
ruleset.

**Provenance:** the child utterances are real. The caregiver responses, the
category scheme and the category labels were produced with an LLM, not
recorded from a parent or independently annotated. That's fine for studying
how the model learns from its training text, but it means the corpus reflects
how an LLM writes a caregiver, not how caregivers actually talk.

## What's next

A follow-on evaluation, built with [Inspect](https://inspect.aisi.org.uk/),
tests whether large language models can recognise what a toddler is doing
with an utterance and respond as a caregiver would, using human labels from
a written annotation guideline.

## Running it

**Scala** (requires sbt):

```
sbt run
```

Then choose an entry point:

1. `Main`: the language model. Enter a child-like input and get a caregiver
   response.
2. `PerplexityMain`: calculates perplexity and writes the results to a file.
3. `StatisticsMain`: calculates corpus statistics and the Zipf distribution
   and writes them to files.

**Python analysis** (reads the CSVs in `results/` and draws the figures):

```
cd analysis
python3 -m venv venv
source venv/bin/activate
pip install pandas matplotlib
python3 <filename>
```
