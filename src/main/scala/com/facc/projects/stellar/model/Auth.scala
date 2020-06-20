package com.facc.projects.stellar.model

case class AccessToken(createdAt: String,
                       updatedAt: String,
                       id: String,
                       clientId: String,
                       expiresAt: String,
                       token: String,
                       userId: String = "",
                       deletedAt: String = "")

case class AuthResponse(access_token: AccessToken,
                        token_type: String)
