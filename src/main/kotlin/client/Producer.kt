package client

import service.InMemoryQueueService
import service.QueueServiceRecord
import service.QueueServiceResponse
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class Producer<K, V> {
    fun send(queue: InMemoryQueueService<K, V>, key: K, value: V): Future<QueueServiceResponse<K, V>> {
        return CompletableFuture
                .supplyAsync { queue.push(QueueServiceRecord(key, value)) }
                .thenApply { response -> response }
    }
}