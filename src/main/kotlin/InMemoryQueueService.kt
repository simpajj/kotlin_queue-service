import com.google.common.base.Ticker
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

class InMemoryQueueService : QueueService<QueueServiceRecord<UUID, String>, QueueServiceResponse<UUID, String>> {

    private val queue: ConcurrentLinkedQueue<QueueServiceRecord<UUID, String>>

    constructor(evictionTime: Int, timeUnit: TimeUnit, ticker: Ticker) {
        queue = ConcurrentLinkedQueue<QueueServiceRecord<UUID, String>>()
    }

    constructor() {
        queue = ConcurrentLinkedQueue<QueueServiceRecord<UUID, String>>()
    }

    override fun push(record: QueueServiceRecord<UUID, String>): QueueServiceResponse<UUID, String> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pull(): QueueServiceRecord<UUID, String> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(record: QueueServiceRecord<UUID, String>): QueueServiceResponse<UUID, String> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}