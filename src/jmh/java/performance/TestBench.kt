package performance

import client.Producer
import org.openjdk.jmh.annotations.*
import service.InMemoryQueueService
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class TestBench {

    @Benchmark
    @Fork(value = 1, warmups = 3)
    fun testProducerSend() {
        val queue = InMemoryQueueService<UUID, String>()
        val producer = Producer<UUID, String>()
        producer.send(queue, UUID.randomUUID(), "hej")
    }
}