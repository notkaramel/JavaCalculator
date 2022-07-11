/**
 * Stack class (String-only Stack)
 * 
 * Required methods:
 * public void push(String arg)
 * public String pop()
 * 
 * @author antoinephan
 */

public class Stack
{
    listNode top;
    int size;

    /**
     * THE CONSTRUCTOR OF STACK
     */
    Stack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Push a String to the Stack
     * @param arg data as a String to push into the Stack
     */
    public void push(String arg) {
        listNode newNode = new listNode(arg);
        newNode.next = this.top;
        this.top = newNode;
        this.size++;
    }

    /**
     * Pop the top element of a Stack     * 
     * @return the top element data (as a String)
     */
    public String pop() {
        if (this.size == 0) // if the Stack is empty
            return null;

        listNode temp = this.top;
        this.top = this.top.next;
        temp.next = null;

        size--;
        return temp.data;
    }
}
