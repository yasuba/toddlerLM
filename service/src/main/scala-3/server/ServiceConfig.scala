package server

import org.http4s.Uri

case class ServiceConfig(
  llmUrl: Uri,
  llmToken: String
)
