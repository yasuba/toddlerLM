# toddlerLM

toddlerLM is a small probabilistic language model trained on a corpus of child–caregiver dialogue. It is built deliberately at the simplest end of the language-modelling spectrum: word-level tokenisation, n-gram
counting, no neural networks. The point is not to produce strong responses but to use a model whose mechanics are fully inspectable as a vehicle for learning corpus and computational linguistics.

The project sits at the intersection of two traditions. From language modelling, it inherits the standard framework of training, sampling, and evaluation — building probability distributions from counts, generating
text from those distributions, and measuring model quality through perplexity. From corpus linguistics, it inherits a closer attention to the data itself: how the corpus was constructed, what categories of utterance
it contains, how vocabulary is distributed across those categories, and what regularities the writing rules introduced. The model is the lens, the corpus is the subject.

The questions the project is trying to answer are:

- **How does the corpus shape the model's behaviour?**

  A probabilistic LM has no architectural opinions about language — whatever structure appears in its outputs has to come from the training data. By keeping the model simple and the corpus small and hand-curated,
  the relationship between data and behaviour becomes traceable.


- **What does the model implicitly encode about response style, without being told?**

  The corpus has five categories (narrative, information seeking, emotional acknowledgement, request and demand, observation) and sub-styles within each. Categories are never given to the model as labels. The question
  is whether sub-style structure survives in the trigram statistics anyway — and Stage 3 found that it does.


- **Where do the writing rules show up in the model?**

  The corpus was written under a tight ruleset: echo-default responses, consistent recast of child grammatical errors, pronoun flip, contraction policy. These rules create regularities. Some of them — like pronoun
  flip — produce visible token co-occurrence patterns the model learns directly. Others may be invisible.


- **What does this specific corpus reveal about probabilistic models in general?**

  Some findings travel beyond this corpus (e.g. local fluency without global coherence is intrinsic to n-gram models). Others are specific to small, formulaic corpora (e.g. trigram contexts becoming unique fast, so
  the model collapses into a lookup table on seen inputs). Distinguishing the two is part of the work.

The project is structured in six stages. Stage 1 designed the corpus categories and writing rules. Stage 2 wrote and annotated the response pairs. Stage 3 extended the model from next-token prediction to input–response generation, refactored to fixed-order n-gram modelling, and characterised the model's two behavioural regimes (recital on seen inputs, low-order random walk on unseen). Stage 4 covers corpus statistics and quantitative evaluation. Stages 5 and 6 will cover qualitative error analysis and an ablation study respectively.
