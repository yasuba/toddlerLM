# toddlerLM — Sub-style annotations (first pass, for review)

Reference only — not training data. Assignments inferred from response shape and
corpus position (category headers were removed before pasting, so top-level category
is my inference from content + block ordering, not your original labels).
Lines marked ⚑ are genuinely ambiguous — review these first.
Numbering follows corpus line order.

## Narrative

1. "the duck is crying because he lost his mummy" — Narrative B
2. "when it's tomorrow it will be my birthday" — Narrative B
3. "when I'm bigger bigger I can go on the big slide" — Narrative B
4. "he's my best friend" — Narrative A
5. "his ice cream fell on the ground" — Narrative B
6. "the unicorn isn't changing colours" — Narrative A
7. "the duck is sad" — Narrative A
8. "the baby is hungry" — Narrative A
9. "big penguin is tired" — Narrative A
10. "the dog is barking" — Narrative A
11. "the baby is sleeping" — Narrative A
12. "George doesn't like pancakes" — Narrative A
13. "after you eat me I'll be a spoon" — Narrative B (imaginative)
14. "kangaroos have teeth" — Narrative A ⚑ response shape is A, but input is a general fact, not displaced — arguably O-notice
15. "you had too many vitamins because you burst into a dinosaur" — Narrative B (imaginative)
16. "she had a spiderman birthday cake" — Narrative B
17. "she was crying for her mummy" — Narrative B
18. "I didn't go upstairs because there was no space" — Narrative B
19. "we played in the garden" — Narrative B
20. "the cat needs a sick bowl" — Narrative B ⚑ response is action ("we'll put one next to her") — E-solve shape on a non-emotional input
21. "I'm a chocolate cake because I like chocolate" — Narrative B (imaginative)
22. "actually they didn't put sprinkles on it" — Narrative B ⚑ "actually" marks correction — arguably O-correct
23. "our house is broken" — Narrative A ⚑ input is present-state — arguably O-notice with question extension
24. "Peppa is a race car driver" — Narrative B
25. "he's a big boy too and they both go to school" — Narrative B
26. "I'm in the school one and you're in the nursery one" — Narrative B ⚑ possibly in-frame play talk
27. "you can't go upstairs because you're sleeping" — Narrative B (imaginative, in-frame)
28. "last time I broke my glasses at nursery" — Narrative B
29. "dinosaurs bite things" — Narrative A ⚑ same general-fact issue as 14
30. "at animal day there was a snake" — Narrative A (mirror + question, yes/no rather than wh-)
31. "Willow didn't touch the snake" — Narrative B
32. "my hands are dirty because I picked my nose" — Narrative B ⚑ response is mirror + action (E-solve shape); input is causal self-report
33. "maybe it's from the trees" — Narrative A ⚑ input is speculation — response turns it back, I-reflect-adjacent
34. "it needs to be a party card with lots of balloons" — Narrative A
35. "today Willow had a flower jumper" — Narrative B
36. "it hurts when I draw on my hand" — E-solve ⚑ sits in narrative block but input reports hurt and response is mirror + action
37. "this is a pumping up balloon" — O-share ⚑ in narrative block; response is playful question (O-play-adjacent)
38. "Bluey doesn't have any armpits" — Narrative B
39. "I'll be the tooth fairy because you're asleep" — Narrative B (imaginative)
40. "Peppa is in the secret room because she's on her own" — Narrative B
41. "next time you'll wear a different hair clip but not this one" — Narrative B
42. "Willow has a medal and last time she bringed it" — Narrative B
43. "this is where my nursery is supposed to be but they haven't built it yet" — Narrative B
44. "it's a spinning round slide in the middle of your eye" — Narrative B (imaginative)
45. "I've got loads of money to buy baby a magazine" — Narrative B (imaginative)
46. "he can't go to the doctors because there's no doctors on the road" — Narrative B
47. "I buyed a pink purple one but it isn't arrived yet" — Narrative B
48. "it goes down because it's a bit too steep" — Narrative B ⚑ present-state causal — arguably O-notice
49. "when it's a long day then it'll be Christmas" — Narrative B
50. "we can't fix it because there's no bits" — Narrative B
51. "when we were walking there it was nap time for the lift" — Narrative B
52. "sometimes I get bored and just climb everywhere" — Narrative B
53. "when I was on the airplane and you were giving me a sandwich" — Narrative B
54. "George is still on the toilet because he did a long poo" — Narrative B
55. "Thomas isn't on the toilet now because trains do little poos" — Narrative B
56. "when we lined up in the garden I stood next to Krisshiv" — Narrative B
57. "when we were going home away from Spain because there were dinosaurs in Spain" — Narrative B (imaginative)
58. "when I get him Santi's already there" — Narrative B
59. "Santi's going back again because he doesn't stay" — Narrative B
60. "I think I said to mummy it didn't work because we didn't read the instructions" — Narrative B
61. "Willow fell on the rocks and her knee was dribbling red" — Narrative B
62. "you've got to stand on one foot because the other one is lava" — Narrative B (imaginative, in-frame)
63. "this one is really gooder but this one isn't very good" — O-evaluate ⚑ in narrative block; input is judgement, response questions it
64. "you put the balloon on the car and it brums" — Narrative B ⚑ in-frame play description; response explains rather than extends
65. "I dreamed that Baby wanted Willow's pizza but she gobbled it all up" — Narrative B
66. "while the pizza was cooking I took it out but I wear gloves" — Narrative B
67. "when I was playing with Mia and we were throwing blocks around" — Narrative B
68. "I'm going to keep these because I'm going to grow up" — Narrative B

## Information seeking

69. "is it tomorrow" — I-direct
70. "is it my birthday" — I-direct
71. "what is that" — I-direct
72. "is it a nursery day" — I-direct
73. "is it the morning" — I-direct
74. "what comes after bedtime" — I-direct
75. "where is the moon" — I-direct
76. "what does red say" — I-direct
77. "what does that say" — I-direct
78. "what did you say" — I-direct
79. "why is it a nursery day" — I-situated
80. "why did you say that" — I-situated
81. "where is my cat bag" — I-direct
82. "do grown ups use straws when they drink their juice" — I-direct
83. "why" — I-reflect
84. "why do you work" — I-reflect
85. "why should we hurry up" — I-reflect
86. "why is it red" — I-reflect
87. "why can't the baby play with this" — I-situated
88. "what is that penguin doing" — I-direct
89. "what is he doing" — I-direct
90. "what is that man saying" — I-direct
91. "why is that man walking there" — I-situated
92. "why have we stopped" — I-situated
93. "are we nearly there yet" — I-direct
94. "why is it broken" — I-situated
95. "is it a bulb" — I-direct
96. "why do you need a poo" — I-situated
97. "why does it say no babies" — I-situated
98. "how do dogs go woof woof" — I-direct (+question extension)
99. "what is the gravity" — I-direct
100. "do fishes wear sun cream" — I-direct
101. "does the sun wear sun cream" — I-direct
102. "is there a pizza sun" — I-direct
103. "do you like scary things" — I-direct (+reciprocal question)
104. "do you remember when I said Krisshiv hit me" — I-direct
105. "can we cuddle them while they're jumping" — I-direct ⚑ question form, but functionally a permission request — response refuses + offers alternative (R-negotiate shape)

## Emotional acknowledgement

106. "I wet the bed" — E-solve
107. "tummy hurts" — E-solve
108. "my toy is broken" — E-solve
109. "my jumper is wet" — E-solve
110. "I fell over" — E-solve (action = checking: "are you hurt?")
111. "I bumped my head" — E-solve
112. "I hurt my finger" — E-solve
113. "I dropped my biscuit" — E-solve
114. "my balloon popped" — E-normalize
115. "my biscuit is broken" — E-normalize
116. "I'm not very well" — E-comfort
117. "I feel poorly" — E-solve
118. "I don't want to go to nursery" — E-normalize
119. "I hate cabbage yuck" — E-solve ⚑ response removes obligation — solve-ish, but borders E-normalize
120. "I don't like green thing" — E-solve
121. "I don't like bedtime" — E-normalize
122. "I don't like daddy" — E-normalize
123. "I don't like dinner yuck" — E-solve ⚑ "try a little bit" is negotiation-flavoured — R-negotiate shape on an emotional input
124. "dinner is yucky" — E-solve ⚑ same as 123
125. "that boy pushed me" — E-normalize
126. "my dog ran away" — E-normalize
127. "I'm sick" — E-normalize
128. "that's scary" — E-comfort
129. "that lion is scary" — E-comfort
130. "I'm sad" — E-comfort
131. "I feel sad" — E-comfort
132. "I'm tired" — E-comfort
133. "I'm excited" — Mirror-only
134. "I feel happy" — Mirror-only
135. "I'm happy" — Mirror-only
136. "I feel better now" — Mirror-only

## Request and demand

137. "cuddle" — R-comply
138. "mummy cuddle" — R-comply
139. "daddy cuddle" — R-comply
140. "I want a cuddle" — R-comply
141. "play with me" — R-comply
142. "help me" — R-comply (+clarifying question)
143. "more please" — R-comply
144. "yes please" — R-comply ⚑ not in the Special list, but feels Special-adjacent (politeness routine)
145. "take my shoes off" — R-comply
146. "change my nappy" — R-comply
147. "do you want to play with me" — R-comply
148. "I want to do sticking and glueing" — R-comply
149. "I want to play" — R-comply
150. "I want to go outside" — R-comply
151. "can I have a biscuit" — R-negotiate
152. "can I have some milk" — R-negotiate
153. "I want a biscuit" — R-negotiate
154. "I want some juice" — R-negotiate
155. "I want frothy milk" — R-negotiate
156. "mummy give me a biscuit" — R-negotiate (manner condition)
157. "I want to go to the toy shop" — R-redirect
158. "I want to go to soft play" — R-redirect
159. "I want a spicy stick" — R-redirect
160. "I want a teddy" — R-redirect
161. "I want a pedal bike" — R-redirect
162. "go away" — R-refuse
163. "stop it" — R-refuse
164. "it's ok" — R-varies ⚑ hard to place — child granting herself permission?
165. "it's ok if I eat chocolate now" — R-negotiate ⚑ refuse + alternative, 3 sentences — R-refuse shape on a food want
166. "I don't like to have a bath now" — R-negotiate
167. "I don't like dinner" — R-negotiate ⚑ near-duplicate of 123/124 which sit in the emotional block — same response, different category?
168. "I want to jump" — R-negotiate (conditioned grant)
169. "I want to run" — R-negotiate (conditioned grant)
170. "I want to go home" — R-redirect ⚑ or deferred comply — "soon" is doing the work
171. "I want to go to bed" — R-comply
172. "again again" — Special
173. "no thank you" — Special
174. "come here" — Special

## Observation

175. "look at this" — O-share
176. "look at this mummy" — O-share
177. "let's show daddy" — O-propose
178. "I can see the moon" — O-notice
179. "it's sunny outside" — O-notice
180. "I like that cat though" — O-like
181. "I love ice cream" — O-like
182. "I like bananas" — O-like
183. "I don't like spiders" — O-like ⚑ negative preference — response normalizes fear, E-adjacent
184. "I like ducks" — O-like
185. "I like chocolate" — O-like
186. "I like biscuits" — O-like
187. "I like chocolate" (duplicate input, different response) — O-like
188. "I like my teddy" — O-like
189. "I like playing" — O-like
190. "I like jumping" — O-like
191. "I like colouring" — O-like
192. "I love mummy" — O-like
193. "I love daddy" — O-like
194. "I love my blanket" — O-like
195. "I love my toy" — O-like
196. "I love my toy" (duplicate input, different response) — O-like
197. "I like my marble run" — O-like
198. "I did it all by my own self" — O-accomplish
199. "I can bounce so high" — O-accomplish
200. "I roar like a lion" — O-accomplish ⚑ performance rather than achievement — O-play-adjacent
201. "I can jump high" — O-accomplish
202. "I can run fast" — O-accomplish
203. "it's too spicy" — O-evaluate ⚑ response is action ("let's get you some water") — E-solve shape
204. "it's too hot yucky" — O-evaluate ⚑ same
205. "mmmm dinner is yummy" — O-evaluate
206. "this is good" — O-evaluate
207. "that's bad" — O-evaluate
208. "this is fun" — O-evaluate
209. "it's too hot" — O-evaluate ⚑ action response
210. "it's too cold" — O-evaluate ⚑ action response
211. "this chocolate is yummy" — O-evaluate
212. "that cake is yummy" — O-evaluate
213. "my shoe is too big" — O-evaluate ⚑ action response
214. "that boy is playing with the marble run" — O-notice
215. "that boy is silly" — O-evaluate
216. "yay we're going to the park" — O-notice ⚑ excited affect — Mirror-only-adjacent, but response extends
217. "mummy helped me" — O-notice
218. "daddy fixed it" — O-notice
219. "my nappy fell down" — O-notice ⚑ action response
220. "the train goes really really fast" — O-notice
221. "my shoe is dirty" — O-notice
222. "my biscuit is all gone" — O-notice
223. "my nappy is full" — O-notice ⚑ action response
224. "woah it's really smelly" — O-notice
225. "woah this is a big poo in my nappy" — O-play
226. "this is a really really big poo" — O-play
227. "you're a poo" — O-play
228. "you're a wee" — O-play
229. "that's silly" — O-evaluate ⚑ O-play-adjacent
230. "I'm silly" — O-play
231. "whooosh" — O-play
232. "let's play catch" — O-propose
233. "let's do something nice" — O-propose
234. "do you want a cuddle" — O-propose ⚑ child offering — response accepts; no clean home in the scheme
235. "actually I don't like the red one it's yucky" — O-correct
236. "actually I don't like red peppers" — O-correct
237. "I did it goodly" — O-accomplish

## Patterns noticed during annotation

- **Mirror + action is not exclusive to E-solve.** It appears across emotional
  (106–113), observation (203, 209, 213, 219, 223), and narrative (20, 32, 36)
  inputs. The response shape tracks "a fixable state exists" rather than
  "the child expressed emotion". If sub-style is meant to be recoverable from
  response form alone, E-solve over-claims this shape.
- **"You can try a little bit" appears under two categories** (123/124 emotional,
  167 request). Identical response, different input framing. Either the
  categories overlap here or one assignment is wrong — worth deciding which.
- **General-fact inputs (14, 29) strain the narrative/observation boundary**:
  not displaced, not present-scene. A "generic statement" type may be hiding here.
- **The block boundaries are inferred.** If your original Week 2 ordering
  differs from my guesses (especially 36–37, 63, 105, 167), the top-level
  category is wrong before the sub-style is.

## Counts (this pass, before your corrections)

Narrative 65 · Information 37 · Emotional 31 · Request 38 · Observation 63 — wait,
that sums to 234; with the three reassigned-out-of-block items (36, 37, 63) counted
in their new homes: Narrative 65, Info 37, Emo 32, Req 38, Obs 65 = 237.
Doc says 224 with Narrative 55 — the 13 added pairs since the doc was written
appear to be mostly narrative. Reconcile when you fix the doc count.
