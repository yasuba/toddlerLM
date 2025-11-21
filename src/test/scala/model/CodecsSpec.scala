package model

import client.LlmClient.{ChatChoice, ChatMessage, ChatResponse}
import io.circe.parser._
import munit.FunSuite

class CodecsTest extends FunSuite {

  test("ChatResponse decoder should successfully decode valid JSON") {
    val json = """{
                    "id" : "chatcmpl-Cdae8rbqBBwAgjBKLJF2oe3JxaTqx",
                    "object" : "chat.completion",
                    "created" : 1763551696,
                    "model" : "gpt-4o-mini-2024-07-18",
                    "choices" : [
                      {
                        "index" : 0,
                        "message" : {
                          "role" : "assistant",
                          "content" : "happy",
                          "refusal" : null,
                          "annotations" : [
                          ]
                        },
                        "logprobs" : null,
                        "finish_reason" : "stop"
                      }
                    ],
                    "usage" : {
                      "prompt_tokens" : 42,
                      "completion_tokens" : 1,
                      "total_tokens" : 43,
                      "prompt_tokens_details" : {
                        "cached_tokens" : 0,
                        "audio_tokens" : 0
                      },
                      "completion_tokens_details" : {
                        "reasoning_tokens" : 0,
                        "audio_tokens" : 0,
                        "accepted_prediction_tokens" : 0,
                        "rejected_prediction_tokens" : 0
                      }
                    },
                    "service_tier" : "default",
                    "system_fingerprint" : "fp_560af6e559"
                  }"""

    val result = decode[ChatResponse](json)

    assert(result.isRight)
    assertEquals(
      result.toOption.get,
      ChatResponse(List(ChatChoice(ChatMessage("assistant", "happy"))))
    )
  }

  test("ChatResponse decoder should handle multiple choices") {
    val json = """{
      "choices": [
        {
          "message": {
            "role": "assistant",
            "content": "happy"
          }
        },
        {
          "message": {
            "role": "assistant",
            "content": "sad"
          }
        }
      ]
    }"""

    val result = decode[ChatResponse](json)

    assert(result.isRight)
    assertEquals(result.toOption.get.choices.size, 2)
  }

  test("ChatResponse decoder should fail on missing choices field") {
    val json = """{}"""

    val result = decode[ChatResponse](json)

    assert(result.isLeft)
  }

  test("ChatResponse decoder should fail on invalid message structure") {
    val json = """{
      "choices": [
        {
          "message": {
            "role": "assistant"
          }
        }
      ]
    }"""

    val result = decode[ChatResponse](json)

    assert(result.isLeft)
  }
}
