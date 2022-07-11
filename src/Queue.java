/**
 * Queue class (String-only Queue)
 * 
 * Required methods:
 * <ul>
 * <li> <code> public void enqueue(String arg)
 * <li> <code> public String dequeue()
 * <li> <code> public String toString()
 * 
 * @author antoinephan
 */
public class Queue
{
    listNode head;
    listNode tail;
    int size;

    // New queue
    Queue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Enqueue a String on to a queue.
     * <p>
     * UPDATE 220310: Change the head & tail for the logic of a normal queue.
     * Previous implementation was flipped.
     * @param arg the String that needs to be enqueued (data)
     */
    public void enqueue(String arg) {
        listNode newNode = new listNode(arg);
        if (this.size == 0) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }
        this.size++;
    }

    /**
     * Dequeues data (a String) from the queue
     * @return data as String
     */
    public String dequeue() {
        listNode temp = this.head; // using a temporary listNode; temp points at what <del>tail</del> head is
                                   // pointing
        if (this.size == 0) // if the Queue is empty
            return null;

        else if (this.size == 1) // if there is only 1 entry in the queue (head == tail : same address)
        {
            this.head = null; // update head
            this.tail = null; // update tail
        } else {
            this.head = this.head.next;
            temp.next = null;
        }
        this.size--;
        return temp.data;
    }

    /**
     * <p>
     * For this method, I will use a listNode pointer called "current"
     * that iterates the queue and get the data to be concatenated to the
     * output string on the screen. In this way, the current queue
     * will not be affected whenever.
     * 
     * @return output value when printing Queue is called, giving the queue instead
     *         of its address
     */
    @Override
    public String toString() // Overriding Object.toString()
    {
        String output = "";
        listNode current = this.head;
        while (current != null) {
            output += current.data + (" ");
            current = current.next;
        }
        return output;
    }
}
