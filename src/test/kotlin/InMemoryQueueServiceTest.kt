import client.Consumer
import client.Producer
import io.kotlintest.specs.FlatSpec
import service.InMemoryQueueService
import service.ResponseCode
import java.util.*

class InMemoryQueueServiceTest : FlatSpec() {

    val producer = Producer<UUID, String>()
    val consumer = Consumer<UUID, String>()
    val key = UUID.randomUUID()
    val queue = InMemoryQueueService<UUID, String>()

    override fun beforeEach() {
        queue.flush()
    }

    init {
        "A producer" should "be able to push a message onto its queue" {
            val response = producer.send(queue, key, "hej")
            response.get().responseCode shouldEqual ResponseCode.REQUEST_OK
            queue.getQueueSize() shouldBe 1
        }

        "A consumer" should "be able to pull a value from its queue" {
            val pResponse = producer.send(queue, key, "hej")
            pResponse.get().responseCode shouldEqual ResponseCode.REQUEST_OK
            val cResponse = consumer.consume(queue)
            val response = cResponse.get()
            response.responseCode shouldEqual ResponseCode.REQUEST_OK
            response.record?.value shouldBe "hej"
        }
    }
}