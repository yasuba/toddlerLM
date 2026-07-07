### Stage 4

perplexity implemented and validated — seen probe with unique contexts scores exactly 1.0, seen probe with a 
one-to-many context (is it tomorrow) scores ~1.08, confirming the function detects real distributional spread rather 
than just memorisation.

Seen-probe perplexity at n=3 is rarely exactly 1.0. Most probes contain at least one context with multiple corpus 
continuations (e.g. <EOS> let me → rub/have/kiss at 0.33 each; no it isn't → tomorrow/your). The formulaic caregiver 
register produces shared response-openings, so even verbatim-seen inputs carry residual uncertainty. Exact-1.0 
perplexity requires a probe whose every context is unique, which is rare.