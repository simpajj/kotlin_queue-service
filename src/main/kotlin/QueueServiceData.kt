data class QueueServiceRecord<out K, out V>(val key: K, val value: V)
data class QueueServiceResponse<out K, out V>(val responseCode: ResponseCode, val record: QueueServiceRecord<K, V>)

enum class ResponseCode(val code: Int) {
    REQUEST_OK(0),
    REQUEST_FAILED(1)
}

// TODO: benchmark with jmh
// TODO: histogram