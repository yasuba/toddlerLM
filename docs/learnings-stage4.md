### Stage 4 — Quantitative evaluation (Part B) and held-out set

perplexity implemented and validated — seen probe with unique contexts scores exactly 1.0, seen probe with a 
one-to-many context (is it tomorrow) scores ~1.08, confirming the function detects real distributional spread rather 
than just memorisation.

Seen-probe perplexity at n=3 is rarely exactly 1.0. Most probes contain at least one context with multiple corpus 
continuations (e.g. <EOS> let me → rub/have/kiss at 0.33 each; no it isn't → tomorrow/your). The formulaic caregiver 
register produces shared response-openings, so even verbatim-seen inputs carry residual uncertainty. Exact-1.0 
perplexity requires a probe whose every context is unique, which is rare.


probabilityOf answers one question: "what's the probability of this token in this context?" It returns 0.0 when the answer is "none, even at the bigram floor." That's its whole responsibility. 

Held-out coverage collapses from 100% (n=2) to 23% (n=4): at higher order the model has no probability at all for most held-out responses, because longer contexts are more likely to be unseen. Mean out-of-vocabulary tokens per response rise in step (10.7 → 16.2). The apparent fall in mean perplexity across orders is survivorship — only the shortest, most echo-heavy responses remain scoreable at n=4, so the average is taken over an easier and easier subset. This is the generalisation cliff in its starkest form: not gradual degradation but silence.


Held-out probe set built

Collected 22 held-out utterances from your daughter's more recent speech (dated ~5 months after the training corpus), tagged by category, kept in a separate file — never added to training.
Authored a gold response for each, applying the corpus writing rules (echo-default, consistent recast, pronoun flip). The authoring itself tested whether the ruleset generalises: where a response wrote itself, the rules held; where you hesitated, you'd found a gap.
Taxonomy strains surfaced (Week 5 material): R-justified needed a genuinely new sub-style (requests with stated reasons — "we should eat those fruits before they get moldier"); R-instruct (directives at caregiver — "you have to wash your hair") and language-play ("wiggle wiggle jellyfish") were absorbed by existing constructions (you're telling me..., O-play); justification is emerging as a modifier orthogonal to category (appears on requests, preferences, observations); in-frame pretend play → respond from inside the fiction. Also: affect is invisible in transcription — two near-identical dinner inputs had different intent (matter-of-fact vs distressed), distinguishable only by prosody that tokenisation destroys; corrected corpus pairs 123/124 to make yuck a learnable signal.

Perplexity implemented and validated

Built a scoring path separate from generation: probabilityOf (single-token, mirrors findPrediction's backoff, bigram floor, 0.0 for OOV) and responsePerplexity (walk the gold response, accumulate log-probs, exp at the end). Returns (Option[Double], scoredCount, oovCount) — perplexity is None when nothing is scoreable.
Validated: a seen probe with all-unique contexts scores exactly 1.0; is it tomorrow scores ~1.08 because no it isn't has two corpus continuations at 0.5 each. Confirms the function detects real distributional spread, not just memorisation.

Held-out results — the generalisation cliff

Coverage collapses with order: 100% (n=2) → 59% (n=3) → 23% (n=4). At higher order the model has no probability at all for most held-out responses, because longer contexts are more likely unseen. Mean OOV per response rises in step (10.7 → 15.3 → 16.2).
The raw mean perplexity falls with order (16.3 → 3.1 → 2.1) — this is survivorship, not improvement. Hard probes drop out (nothing scoreable), leaving only short, echo-heavy responses whose few surviving tokens sit in memorised local contexts. The falling mean and collapsing coverage are the same phenomenon.
Honest reporting therefore needs three numbers per order — coverage, mean OOV, and perplexity-with-scoredCount — never perplexity alone. On the fixed 5-probe subset scoreable at every order, perplexity is 6.58 → 3.32 → 2.11, but that subset is biased-easy, so coverage is the real story, not perplexity.
Held-out perplexity sits well above seen-probe perplexity (~1.0), confirming no leakage.
Two OOV failure modes visible per-probe: novel vocabulary (jellyfish, ballerinas, moldier) gives roughly constant OOV across orders; novel transitions give OOV that climbs with order.

Seen vs held-out contrast

On seen probes, perplexity falls toward 1.0 as order rises (more unique contexts → less ambiguity → better fit). On held-out, coverage collapses as order rises. Same knob, opposite effect — the n-gram version of the fit/generalisation (bias-variance) tradeoff.

Still outstanding in Week 4 — corpus statistics (Part A)

Context ambiguity by order (fraction of contexts with a single continuation — quantifies the recital threshold met by hand via is it tomorrow).
Vocabulary distribution: TTR, hapax proportion, Zipf plot.
Per-category breakdowns.
Cross-category bigrams (the sub-style bridges).

Convention: what "order" means in this codebase

Throughout the code, order = N means an N-token context (the model conditions on N previous tokens). In standard n-gram terminology this is an (N+1)-gram — e.g. order = 3 is a 3-token context, which is a 4-gram in the literature. The naming comes from ContextBuilder.nGram(n, ...), whose parameter is context size, not n-gram order.

Both the perplexity table and the context-ambiguity table use order with this same context-size meaning, so rows at the same order across the two tables refer to identical underlying contexts and can be read side by side. (This is deliberate: the ambiguity numbers explain the perplexity numbers — a high fraction of deterministic contexts at a given order is why perplexity sits near 1.0 at that order.)

Note: the CSV output originally emitted both an n and an order column with identical values — collapsed to a single order column to avoid ambiguity.

The single highest-branching context in the corpus is <SEP> you (17 continuations at order 2) — the point just after a response commits to a you-acknowledgement but before it selects which kind (want / like / feel / did / can...). This is effectively where response category is chosen. It locates the Week 3 "implicit sub-style structure" finding at a specific context: category routing happens at the <SEP> you → ? branch, after which continuations narrow sharply. It's the model's main generative decision point; almost everywhere else is near-deterministic.


Hapax legomena make up 23% of the vocabulary (141 of 609 types) — nearly a quarter of all word-types appear exactly once. This is the concrete motivation for Kneser-Ney smoothing: for these words, MLE has a single data point and assigns all probability to one continuation with zeros elsewhere, which is precisely the pattern behind the held-out OOV/coverage collapse. KN's type-based discounting is designed for exactly this rare-word tail.

The vocabulary follows Zipf's law broadly, but the head is flatter and fuller than the ideal 1/rank decay: the top ~7 types (you, the, a, is, i, to, it) cluster at comparable high frequencies rather than dropping off sharply. This reflects the formulaic register — pronoun-flip echoes force you/i up, copula templates force is up. Content words also rank unusually high and diagnostically: want (10th) fingerprints the request category, like (9th) the observation category, because (13th) the causal structure in narrative. The frequency ranking encodes the taxonomy. (Note: an empty-string token ranked "1st" at 258 occurrences — a tokenisation artefact from double-spaces — was filtered before plotting.)
