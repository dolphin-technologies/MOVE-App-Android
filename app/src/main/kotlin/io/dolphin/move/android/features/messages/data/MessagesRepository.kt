package io.dolphin.move.android.features.messages.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.database.messages.MoveMessage
import io.dolphin.move.android.basedata.local.database.messages.MoveMessagesDao
import io.dolphin.move.android.basedata.local.database.messages.mapToMoveMessage
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.PushRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiGetMessagesResponseData
import io.dolphin.move.android.basedata.network.responses.ApiMessage
import io.dolphin.move.android.basedata.network.responses.ApiUpdateMessagesRequest
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.domain.dto.UpdateMessageDto
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Interface for messages
 */
interface MessagesRepository {

    /**
     * Request for messages. On success returns [State.Data] of [Unit].
     * On failure returns [State.Error] with error code of result.
     *
     * @return [State] of [Unit]
     */
    suspend fun requestMessages(): State<Unit>

    /**
     * Request for messages patch. On success returns [State.Data] of [ApiGetMessagesResponseData].
     * On failure returns [State.Error] with error code of result.
     *
     * @return [State] of [ApiGetMessagesResponseData]
     */
    suspend fun requestUpdateMessage(dto: UpdateMessageDto): State<Unit>

    /**
     * Subscription for messages. Starts with [State.Loading]. On success emits [State.Data] of [List] with [MoveMessage].
     * On failure emits [State.Error] with error code of result.
     *
     * @return [Flow] with [State] of [List] with [MoveMessage]
     */
    fun getMessagesFlow(): Flow<List<MoveMessage>>
}

class MessagesRepositoryImpl @Inject constructor(
    private val messagesApi: PushRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
    private val userStorage: UserStorage,
    private val messagesDao: MoveMessagesDao,
) : BaseRepository(), MessagesRepository {

    override suspend fun requestMessages(): State<Unit> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                messagesApi.apiV1MessagesGet(
                    xAppContractid = requestHelper.getContractId(),
                )
            } catch (e: Exception) {
                return@withContext commonErrorState(e.message)
            }
            val state: State<Unit>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        val deletedIds = response.data.deletedIds.orEmpty()
                        val filteredData = response.data.copy(
                            messages = response.data.messages?.filter { it.id !in deletedIds },
                        )
                        filteredData.messages?.map { it.mapToMoveMessage() }?.let {
                            messagesDao.insertMessagesList(it)
                        }
                        userStorage.saveMessages(filteredData)
                        State.Data(Unit)
                    } else {
                        mapToServiceError(null, message = "Empty data")
                    }
                } else {
                    response?.status.mapToServiceError()
                }
            } else {
                state = rawResponse.mapToError()
            }
            state
        }
    }

    override suspend fun requestUpdateMessage(dto: UpdateMessageDto): State<Unit> {
        val updateList = listOf(
            ApiMessage(
                id = dto.id,
                read = dto.markAsRead,
                deleted = dto.deleted,
            )
        )
        return withContext(ioDispatcher) {
            val rawResponse = try {
                messagesApi.apiV1MessagesPatch(
                    apiUpdateMessagesRequest = ApiUpdateMessagesRequest(
                        messages = updateList,
                    ),
                    xAppContractid = requestHelper.getContractId(),
                )
            } catch (e: Exception) {
                return@withContext commonErrorState(e.message)
            }
            val state: State<Unit>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        val deletedIds = response.data.deletedIds.orEmpty()
                        val filteredData = response.data.copy(
                            messages = response.data.messages?.filter { it.id !in deletedIds },
                        )
                        if (dto.deleted == true) {
                            messagesDao.removeMessage(dto.id)
                        }
                        filteredData.messages?.map { it.mapToMoveMessage() }?.let {
                            messagesDao.insertMessagesList(it)
                        }
                        userStorage.saveMessages(filteredData)
                        State.Data(Unit)
                    } else {
                        mapToServiceError(null, message = "Empty data")
                    }
                } else {
                    response?.status.mapToServiceError()
                }
            } else {
                state = rawResponse.mapToError()
            }
            state
        }
    }

    override fun getMessagesFlow(): Flow<List<MoveMessage>> {
        return messagesDao.getMoves()
    }
}