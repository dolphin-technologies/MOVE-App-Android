package io.dolphin.move.android.basedata

import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.client.infrastructure.getErrorResponse
import io.dolphin.move.android.basedata.network.mapToHttpError
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.domain.State
import retrofit2.Response

open class BaseRepository {
    protected fun Response<*>.mapToError(): State.Error {
        return try {
            val errorBody = this.getErrorResponse<ApiBaseResponse>()
            errorBody?.status.mapToServiceError()
        } catch (E: Exception) {
            mapToHttpError(
                code = this.code().toLong(),
                message = this.message(),
            )
        }
    }

    protected fun commonErrorState(message: String?): State.Error {
        return State.Error(
            MoveApiError.HttpError(code = -1, responseMessage = message)
        )
    }
}