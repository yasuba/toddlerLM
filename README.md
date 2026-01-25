## toddlerLM — Understanding next-token prediction with toddler speech

A tiny, inspectable language model implemented in Scala, trained on toddler-style utterances, designed to explore tokens, context, and how large language models work under the hood.

Note: This is a toy model for learning purposes.

### Motivation
I wanted to understand what language models really do.
Instead of using an API, I built something small enough to inspect end-to-end.”

toddlerLM helps explore:

- Tokenisation (words, characters, subwords)

- Context windows (n-grams)

- Next-token prediction

- Emergent patterns in small data

### Corpus

The model is trained on a small set of toddler-style utterances. Example lines:

- "I want my mummy."
- "My biscuit's broken."

This corpus is intentionally tiny so you can see exactly how the model behaves.

### How it works

1. **Tokenisation** 
    
    Converts sentences into tokens (currently word-based). 

2. **Context windows** 
   
    For each token, track the previous N tokens (configurable).

3. **Counts & probabilities** 
   
    Counts how often each token follows a context and converts counts to probabilities.

4. **Prediction** 
   
    Given a context, the model outputs the most likely next token(s).

### Usage

`sbt run`

Follow the prompt to type a sentence and see the model’s predicted next token(s).

### Learning outcomes

* Understand what a “token” really is
* Explore simple next-token models (n-grams)
* See why context matters
* Observe “failure modes” of small models before scaling

### Optional enhancements

* Character or subword tokenisers
* Text generation from initial seed token
* Analysis of token probabilities and context coverage
