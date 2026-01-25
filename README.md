## toddlerLM — Understanding next-token prediction with toddler speech

`toddlerLM` is a toy next-token language model written in Scala. It explores how language models work under the hood by 
experimenting with tokens, context windows (n-grams), probabilities and text generation.

The corpus is based on real (and lightly augmented) things my toddler has said.

#### What it does

At its core, `toddlerLM` is a next-token predictor: given a sequence of tokens, it predicts the most likely next token 
based on patterns in the training data.

By repeatedly predicting and feeding the output back in, it can also generate text.

This is the same basic mechanism used by large language models like GPT, just in the smallest possible form.

#### How it works

1. Tokenise a small toddler corpus

2. Build n-gram contexts

3. Count and normalise into probabilities

4. Predict next tokens

5. Generate text autoregressively

#### Running it
`sbt run`

Enter a seed phrase, for example:

> I want

And the model will generate a continuation, e.g.:

> I want a cuddle again play with me

#### Unknown contexts

If the model generates a context it has never seen before, it:

1. Backs off to a smaller context

2. If that fails, picks a random known context and continues
