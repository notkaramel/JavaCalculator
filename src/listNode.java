/**
 * listNode object (String-only), to be used in Stack.java & Queue.java
 * @author antoinephan
 */
public class listNode
{
    String data;
    listNode next;

    listNode(String data) // constructing a node with a given data String
    {
        this.data = data;
        this.next = null;
    }
}
