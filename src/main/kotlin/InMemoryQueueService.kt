import com.google.common.base.Ticker
import com.google.common.cache.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class InMemoryQueueService<K, V> : QueueService<QueueServiceRecord<K, V>, QueueServiceResponse<K, V>> {

    private val logger = Logger.getLogger(InMemoryQueueService::class.simpleName)
    private val queue: ConcurrentLinkedQueue<QueueServiceRecord<K, V>> = ConcurrentLinkedQueue()
    private val cache: Cache<K, QueueServiceRecord<K, V>>
    val listener: RemovalListener<K, QueueServiceRecord<K, V>> = RemovalListener {
        @Override
        fun onRemoval(notification: RemovalNotification<K, QueueServiceRecord<K, V>>) {
            if (notification.cause == RemovalCause.EXPIRED) queue.add(notification.value)
        }
    }

    constructor(evictionTime: Long, timeUnit: TimeUnit, ticker: Ticker) {
        cache = CacheBuilder
                .newBuilder()
                .ticker(ticker)
                .expireAfterWrite(evictionTime, timeUnit)
                .ticker(ticker)
                .removalListener(listener)
                .build<K, QueueServiceRecord<K, V>>()
    }

    constructor() {
        cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .removalListener(listener)
                .build<K, QueueServiceRecord<K, V>>()
    }

    override fun push(record: QueueServiceRecord<K, V>): QueueServiceResponse<K, V> {
        try {
            queue.offer(record)
            return QueueServiceResponse(ResponseCode.REQUEST_OK, record)
        } catch (npe: NullPointerException) {
            return QueueServiceResponse(ResponseCode.REQUEST_FAILED, record)
        }
    }

    override fun pull(): QueueServiceResponse<K, V> {
        val record = queue.poll() ?: return QueueServiceResponse(ResponseCode.REQUEST_FAILED, null)
        cache.put(record.key, record)
        return QueueServiceResponse(ResponseCode.REQUEST_OK, record)
    }

    override fun delete(record: QueueServiceRecord<K, V>): QueueServiceResponse<K, V> {
        cache.invalidate(record.key)
        return QueueServiceResponse(ResponseCode.REQUEST_OK, null)
    }

    fun getQueueSize(): Int {
        return queue.size
    }

    fun getCacheSize(): Long {
        cache.cleanUp()
        return cache.size()
    }
}