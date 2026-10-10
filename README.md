# toddlerLM

A small probabilistic language model trained on a corpus of child–caregiver
dialogue, built deliberately at the simplest end of the spectrum: word-level
n-gram counting, no neural networks, allowing its mechanics to be fully inspectable.
The point isn't perfect responses; it's a transparent model used as a vehicle
for learning corpus and computational linguistics. The model is the lens, the
corpus is the subject.

## What it found

The main findings of the project were that inputs and responses are categorised
into 'sub-styles' which were never explicitly given to the model, but regardless
the model conformed to, as they were implicit in the n-gram statistics. 
Also, when encountering an entirely new input, the model falls off a "generalisation
cliff". The coverage collapses at higher n-gram orders. The nature of the project's 
corpus itself (formulaic, lots of rare words) is the reason for the model's behaviour.

## The corpus

Five inductively-derived categories (narrative, information-seeking, emotional
acknowledgement, request/demand, observation), hand-authored under a fixed
writing ruleset. **Note:** the child utterances are real; the caregiver
responses were LLM-generated, not recorded from a parent. If I'd used my own
real responses to these utterances, they likely would have been very far from the 
standard CDS responses! This is fine for studying how the model learns from its 
training text, but means the corpus reflects how an LLM writes a caregiver, not 
how caregivers actually talk.

## Read more

Full write-up: [findings.md](./docs/findings.md) — method, findings per stage, error
analysis, limitations.

## Running it

To run the Scala project, `sbt run` then pick one of the options:
1. Main is the language model, enter a child-like input and receive a caregiver response.
2. PerplexityMain calculates the perplexity and prints to a file.
3. StatisticsMain calculates statistics and Zipfian distribution and prints to files.

To run the Python project: 
```
cd analysis
source venv/bin/activate
python3 <filename>
```
