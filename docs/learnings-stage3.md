### Stage 3
#### Setup

- Replaced single child utterance corpus with child-caregiver response pairs. These formed 5 categories: narrative, information seeking, emotional acknowledgement, request and demand, and observation.
- Format chosen: <input tokens> <SEP> <response tokens> <END>, with <EOS> marking internal sentence boundaries within responses. <SEP> terminates the input side; no <EOS> before <SEP>.
- Tokenisation handles lowercasing, apostrophes preserved inside words, sentence-boundary <EOS>, response-final <END>. <SEP> filtered from prediction distributions as a control token — it should never be emitted mid-response.
- During token generation, break out of the loop if the generated word is <END> and do not return this marker.
- During sentence generation, slice at <SEP> and return only the response.

#### Initial probe set run (variable-order, pre-refactor)

Tested the model against the probe set (9 utterances, 8 seen, 1 unseen).
The results showed that generation is too tightly coupled to the user input because the current implementation sets the n-gram order to equal the seed length. When the seed length equals or exceeds the depth at which contexts become unique in the corpus, the model behaves as a lookup table — the long context is always found in training, so backoff never triggers.
The model has two regimes: on seen inputs, contexts are unique at high n, so generation recites the training response (greedy ≡ sampling). On unseen inputs, no high-n contexts are found, the model backs off to low n where contexts have many continuations (greedy ≠ sampling, output is a Markov walk). There is little middle ground because contexts become unique fast at this corpus size.
It should be noted that the v1 of this project (a next-token predictor) did not use real sampling. When it selected a next token randomly from the distribution, it ignored the probabilities. In most LMs, the convention is to use 'multinomial sampling'.
Before moving on, I planned to:

- Use multinomial sampling.
- Refactor to ensure the model uses a fixed n-gram order (the conventional approach — model capacity is a property of the model, not of the user's input).
- Increase the size of the probe set to include more unseen utterances.



#### Conceptual unlock

- <SEP> is not a switch the model flips. It is just a token whose right-context distribution happens to encode the response-given-input mapping, because that's what training data put there. Conversation in a probabilistic model is emergent from the format and from choosing where to start sampling — there is no architectural conversational component.

#### Post-refactor results at n=3 (fixed-order trigram)
Following refactoring, I chose a fixed n=3 order. This has changed the model's responses significantly. The outputs recorded in test1.v3 show:
The model exhibits within-sub-style recombination. When sampling diverges from the gold response, it tends to drift into another response in the same sub-style rather than across sub-styles. Examples:

- tummy hurts → E-solve to E-solve (correct compose)
- I don't want to go to nursery → E-normalize to E-normalize (different topic, same register)
- why do you work → I-reflect to I-direct (shared opener that's a)
- do a picture... → backed-off into request register (R-redirect + R-comply)

This is evidence that sub-style structure is implicitly encoded in trigram statistics via the writing rules, without explicit category labels. Predicts Week 6 ablation will show category tokens reinforce rather than introduce this structure.
Multi-sentence composition emerges via the <EOS> boundary — after <EOS>, the trigram context resets to one that carries no topical memory, so the model picks up a fragment from a different training pair. The first observed example: the duck is crying because he lost his mummy → the duck is sad <EOS> why is he barking. Demonstrates local fluency without global coherence — a defining property of n-gram models.

#### Results at n=2
Testing the model with a 2-gram order, the responses were less deterministic than with 3-gram, but there was one response created with sampling that was most credible. The probe i don't want to go to nursery produced last time you broke your glasses at nursery <EOS> some days are hard. This feels like a conversation, when in fact the model produced a pragmatically coherent two-sentence response by accident — the bigram path happened to route through a topically-relevant fragment.

#### Results at n=4
Using 4-gram made the responses more deterministic. The responses for the unseen probe were interesting: the greedy one made most sense — you want me to play <EOS> yes let's play — which could plausibly follow a request to do a picture. But with sampling, although the response correctly acknowledged a request was made, it did not stick with the topic and assumed a negative emotion, when none was implied: you want me to go away <EOS> you're cross with me <EOS> i'm going to stay here.

#### Cross-order synthesis
Sampling behaviour at different n-gram orders doesn't follow a simple "higher order = more constrained" pattern. At n=3, when sampling picks something other than the top-probability token, the response tends to drift to a similar response in the same sub-style. At n=4, the same kind of sampling pick can produce a response in a different sub-style (e.g. the unseen probe drifted from R-comply to R-refuse). Possible explanation: at n=4, contexts are more unique, so each context's distribution has fewer alternatives and they may be very different from the top choice. Picking a non-top alternative at n=4 is therefore a bigger jump than picking one at n=3, where distributions have more spread and the alternatives sit closer to the top choice. Worth testing more systematically — would predict that the variance of response category under sampling is higher at n=4 than at n=3.
Sampling and greedy converge wherever distributions are deltas — most of the time at n≥3. They diverge meaningfully only when the model is in a region of distributional spread, which is mostly at n=2 or during backoff. This is also why v1's broken "sampling" implementation went unnoticed for so long: even with correct multinomial sampling, there's not much room for it to matter at trigram order on this corpus.

### Open questions going into Stage 4

- Smoothing. Still on raw MLE plus stupid backoff. Kneser-Ney is the conventional next step.
- Evaluation. Have probe outputs but no perplexity numbers — no quantitative way to compare model versions yet.
- Probe set design. Mostly-seen probes test recital, not generalisation. Held-out / near-miss probes flagged but not yet built.