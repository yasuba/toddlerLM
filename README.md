### Project: Sentiment Analyzer

Goal: Create a simple Spark application that classifies a child's sentences as either "happy" or "sad."

#### Step 1: Dataset

Data format: A CSV file, each line has the text followed by a label ("happy" or "sad").

Examples:

- "I love my toy", happy
- "My dog ran away", sad
- "It's sunny outside", happy
- "I fell down and scraped my knee", sad
- "My ice cream fell on the ground", sad
- "Mummy gave me a biscuit", happy

#### Step 2: Features

- Tokenizer: To split each sentence into individual words
- StopWordsRemover: To remove common words like "the," "a," "is," etc.
- HashingTF: To convert the cleaned words into numerical feature vectors

#### Step 3: Train the machine learning model

- NaiveBayes: The multinomial Naive Bayes classifier is a simple but effective algorithm for text classification.
- Pipeline integration: Put the tokenizer, stop words remover, and HashingTF steps into a single Pipeline.

#### Step 4: Make predictions

- Running the app will output a command line prompt to ask how the user is doing today.
- The Spark pipeline will predict the sentiment of the user input.
- TODO: Based on the sentiment prediction, an appropriate message will be returned.
