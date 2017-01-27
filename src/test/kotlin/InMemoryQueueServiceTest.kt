import client.Consumer
import client.Producer
import com.google.common.testing.FakeTicker
import io.kotlintest.specs.FlatSpec
import service.InMemoryQueueService
import service.QueueServiceRecord
import service.ResponseCode
import java.util.*
import java.util.concurrent.TimeUnit

class InMemoryQueueServiceTest : FlatSpec() {

    val producer = Producer<UUID, String>()
    val consumer = Consumer<UUID, String>()
    val key = UUID.randomUUID()
    val queue = InMemoryQueueService<UUID, String>()

    override fun beforeEach() {
        queue.flush()
    }

    init {
        "A producer" should "be able to push a message onto a queue" {
            val response = producer.send(queue, key, "hej")
            response.get().responseCode shouldEqual ResponseCode.REQUEST_OK
            queue.getQueueSize() shouldBe 1
        }

        "A consumer" should "be able to pull a value from a queue" {
            val pResponse = producer.send(queue, key, "hej")
            pResponse.get().responseCode shouldEqual ResponseCode.REQUEST_OK
            val cResponse = consumer.consume(queue)
            val response = cResponse.get()
            response.responseCode shouldEqual ResponseCode.REQUEST_OK
            response.record?.value shouldBe "hej"
        }

        "A consumer" should "receive a failure response when pulling from an empty queue" {
            consumer.consume(queue).get().responseCode shouldEqual ResponseCode.REQUEST_FAILED
        }

        "A queue" should "move a pulled message to the cache" {
            queue.push(QueueServiceRecord(UUID.randomUUID(), "hej"))
            queue.getQueueSize() shouldBe 1

            queue.pull()
            queue.getQueueSize() shouldBe 0
            queue.getCacheSize() shouldBe 1L
        }

        "A queue" should "automatically expire consumed records that were not deleted" {
            val ticker = FakeTicker()
            val queueWithFakeTicker = InMemoryQueueService<UUID, String>(2, TimeUnit.SECONDS, ticker)

            queueWithFakeTicker.push(QueueServiceRecord(UUID.randomUUID(), "hej"))
            queueWithFakeTicker.getQueueSize() shouldBe 1

            queueWithFakeTicker.pull()
            queueWithFakeTicker.getQueueSize() shouldBe 0
            queueWithFakeTicker.getCacheSize() shouldBe 1L

            ticker.advance(2, TimeUnit.SECONDS)
            queueWithFakeTicker.getCacheSize() shouldBe 0L
        }
    }
}