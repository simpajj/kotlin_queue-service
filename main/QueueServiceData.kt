data class QueueServiceRecord<out K, out V>(val key: K, val value: V)
data class QueueServiceResponse<out K, out V>(val responseCode: ResponseCode, val record: QueueServiceRecord<K, V>)

enum class ResponseCode(val code: Int) {
    REQUEST_OK(200),
    REQUEST_FAILED(201)
}

// TODO: benchmark with jmh
// TODO: histogram