/**
 * An interface defining methods of a Queue Service
 * handling records of an arbitrary type T and
 * responses of an arbitrary type R.
 */
interface QueueService<T, out R> {

    /**
     * Pushes a record onto the queue
     * @record A record of type T to push
     * @return A response of type R
     */
    fun push(record: T): R

    /**
     * Pulls a record from the queue
     * @return A record of type T
     */
    fun pull(): T

    /**
     * Deletes a record from the queue
     * @record A record of type T to delete
     * @return A response of type R
     */
    fun delete(record: T): R

}