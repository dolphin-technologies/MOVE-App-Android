package io.dolphin.move.android.basedata.network

import io.dolphin.move.android.basedata.network.responses.ApiStatus
import io.dolphin.move.android.domain.State

val ApiStatus?.isInformational: Boolean get() = (this?.code ?: -1L) in 100..199

val ApiStatus?.isRedirect: Boolean get() = (this?.code ?: -1L) in 300..399

val ApiStatus?.isClientError: Boolean get() = (this?.code ?: -1L) in 400..499

val ApiStatus?.isServerError: Boolean get() = (this?.code ?: -1L) in 500..999

val ApiStatus?.isSuccessfull: Boolean get() = (this?.code ?: -1L) == 0L

fun mapToServiceError(code: Long?, message: String?): State.Error {
    return State.Error(
        when (code) {
            USER_NOT_FOUND -> MoveApiError.ServiceError.UserNotFound(
                responseMessage = message,
            )
            USER_EXITS -> MoveApiError.ServiceError.UserExists(
                responseMessage = message,
            )
            WRONG_PASSWORD -> MoveApiError.ServiceError.WrongPassword(
                responseMessage = message,
            )
            PASSWORD_RETRIES_ERROR -> MoveApiError.ServiceError.PasswordRetriesError(
                responseMessage = message,
            )
            GENERAL_INVALID_PARAMETERS -> MoveApiError.ServiceError.GeneralInvalidParameters(
                responseMessage = message,
            )
            REFRESH_TOKEN_INVALID -> MoveApiError.ServiceError.RefreshTokenInvalid(
                responseMessage = message,
            )
            REFRESH_TOKEN_FOR_ANOTHER_USER -> MoveApiError.ServiceError.RefreshTokenForAnotherUser(
                responseMessage = message,
            )
            SIGNED_IN_ANOTHER_DEVICE -> MoveApiError.ServiceError.SignedInAnotherDevice(
                responseMessage = message,
            )
            JWT_EXPIRED -> MoveApiError.ServiceError.JwtExpired(
                responseMessage = message
            )
            RESET_EMAIL_RETRIES_ERROR -> MoveApiError.ServiceError.TooMayEmailRetries(
                responseMessage = message
            )
            else -> MoveApiError.HttpError(code = code, responseMessage = message)
        }
    )
}

fun mapToHttpError(code: Long, message: String?): State.Error {
    return State.Error(MoveApiError.HttpError(code = code, responseMessage = message))
}

fun ApiStatus?.mapToServiceError(): State.Error {
    return mapToServiceError(this?.code, this?.message)
}
