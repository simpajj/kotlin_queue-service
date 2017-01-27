package client

import service.InMemoryQueueService
import service.QueueServiceResponse
import service.ResponseCode
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class Consumer<K, V> {
    fun consume(queue: InMemoryQueueService<K, V>): Future<QueueServiceResponse<K, V>> {
        return CompletableFuture
                .supplyAsync { queue.pull() }
                .thenApply { result ->
                    if (result.responseCode == ResponseCode.REQUEST_OK && result.record != null) queue.delete(result.record)
                    result
                }
    }
}