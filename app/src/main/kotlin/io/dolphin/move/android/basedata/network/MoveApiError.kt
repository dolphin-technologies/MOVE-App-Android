package io.dolphin.move.android.basedata.network

/**
 * Base class for errors that can be received via HTTP requests
 */
sealed class MoveApiError : Throwable() {
    abstract val code: Long?
    abstract val responseMessage: String?

    /**
     * Common class for HTTP errors, not related to the server errors
     *
     * @property code HTTP error code
     * @property responseMessage response message (if present)
     */
    data class HttpError(
        override val code: Long? = null,
        override val responseMessage: String? = null,
    ) : MoveApiError()

    /**
     * Describes different states of the server errors
     */
    sealed class ServiceError : MoveApiError() {
        data class UserNotFound(
            override val code: Long = USER_NOT_FOUND,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class UserExists(
            override val code: Long? = USER_EXITS,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class WrongPassword(
            override val code: Long? = WRONG_PASSWORD,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class PasswordRetriesError(
            override val code: Long? = PASSWORD_RETRIES_ERROR,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class GeneralInvalidParameters(
            override val code: Long? = GENERAL_INVALID_PARAMETERS,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class RefreshTokenInvalid(
            override val code: Long? = REFRESH_TOKEN_INVALID,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class RefreshTokenForAnotherUser(
            override val code: Long? = REFRESH_TOKEN_FOR_ANOTHER_USER,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class SignedInAnotherDevice(
            override val code: Long? = SIGNED_IN_ANOTHER_DEVICE,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class JwtExpired(
            override val code: Long? = JWT_EXPIRED,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class TooMayEmailRetries(
            override val code: Long? = RESET_EMAIL_RETRIES_ERROR,
            override val responseMessage: String? = null,
        ) : ServiceError()

        data class Unknown(
            override val code: Long? = null,
            override val responseMessage: String? = null,
        ) : ServiceError()
    }
}