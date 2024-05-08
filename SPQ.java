import java.util.Scanner;

// -----------------------------------------------------
// Assignment 3
// Question: 1
// Written by: Jenish Akhed (40270365)
// -----------------------------------------------------

/**
 *  Name(s) and ID(s): Jenish Akhed (40270365)
 *  COMP 6481
 *  Assignment 3
 *  Due Date: 15 April 2024
 * @author Jenish Akhed
 * @version 1.0
 */

/**
 * This class represents a data entry with a key and a value.
 */
class DataEntry {

    private int key;
    private String value;

    /**
     * Constructor to create a DataEntry object with a key and value.
     *
     * @param key The integer key for the data entry.
     * @param value The string value for the data entry.
     */
    public DataEntry(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Getter method to get the key of the DataEntry object.
     *
     * @return The integer key of the data entry.
     */
    public int getKey() {
        return key;
    }

    /**
     * Setter method to set the key of the DataEntry object.
     *
     * @param key The new integer key for the data entry.
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * Getter method to get the value of the DataEntry object.
     *
     * @return The string value of the data entry.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter method to set the value of the DataEntry object.
     *
     * @param value The new string value for the data entry.
     */
    public void setValue(String value) {
        this.value = value;
    }
}

/**
 * This class implements a priority queue using a linked list with a heap structure.
 * It can maintain either a min-heap (lowest key on top) or a max-heap (highest key on top)
 * based on the internal state variable.
 */
class SPQList {
    private String state = "MIN"; // Internal state: "MIN" or "MAX"

    /**
     * This class represents a node in the linked list and heap structure.
     */
    class Node {
        public Node parentNode;
        public DataEntry dataEntry;
        public Node leftNode;
        public Node rightNode;

        /**
         * Constructor to create a Node object with parent, data entry, left child, and right child references.
         *
         * @param parentNode The parent node of this node in the linked list.
         * @param dataEntry The DataEntry object stored in this node.
         * @param leftNode The left child node of this node.
         * @param rightNode The right child node of this node.
         */
        public Node(Node parentNode, DataEntry dataEntry, Node leftNode, Node rightNode) {
            this.parentNode = parentNode;
            this.dataEntry = dataEntry;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }
    }

    /**
     * This class represents a node in a temporary queue used for internal operations.
     */
    class QueueNode {
        public Node node;
        public QueueNode next;

        /**
         * Constructor to create a QueueNode object with a node reference and a next pointer.
         *
         * @param node The node this QueueNode holds a reference to.
         * @param next The next QueueNode in the queue.
         */
        public QueueNode(Node node, QueueNode next) {
            this.node = node;
            this.next = next;
        }
    }

    private Node head;  // Head node of the linked list
    private int size;    // Current size of the priority queue
    private QueueNode queueHead;  // Head node of the temporary queue (used for internal operations)
    private QueueNode queueTail;  // Tail node of the temporary queue (used for internal operations)

    /**
     * Default constructor to initialize an empty SPQList object.
     */
    public SPQList() {
        this.head = null;
        this.size = 0;
        this.queueHead = null;
        this.queueTail = null;
    }

    /**
     * Helper method to enqueue a node onto the temporary queue.
     *
     * @param node The node to be enqueued.
     */
    private void enQueue(Node node){
        if (queueHead == null){
            queueHead = new QueueNode(node, null);
            queueTail = queueHead;
        } else {
            queueTail.next = new QueueNode(node, null);
            queueTail = queueTail.next;
        }
    }

    /**
     * Helper method to dequeue a node from the temporary queue.
     *
     * @return The dequeued node.
     */
    private Node deQueue() {
        QueueNode temp = queueHead;
        queueHead = queueHead.next;
        temp.next = null;
        return temp.node;
    }

    /**
     * Helper method to find the last parent node with both children in the linked list and heap structure.
     * Used for up-heap operations during insertion.
     *
     * @return The last parent node with both children.
     */
    private Node lastParent() {
        enQueue(head);
        QueueNode temp = queueHead;

        while (temp != null){
            if (temp.node.leftNode!=null && temp.node.rightNode!=null){
                Node node = deQueue();
                enQueue(node.leftNode);
                enQueue(node.rightNode);
                temp = queueHead;
            } else {
                queueHead = null;
                break;
            }
        }
        return temp.node;
    }

    /**
     * Helper method to find the last node in the linked list and heap structure.
     * Used for down-heap operations during removal and toggling state.
     *
     * @return The last node in the list.
     */
    private Node lastNode(){
        enQueue(head);
        QueueNode temp = queueHead;
        Node node = null;
        while (temp != null){
            node = deQueue();
            if (temp.node.leftNode!=null){
                enQueue(node.leftNode);
            }
            if (temp.node.rightNode!=null){
                enQueue(node.rightNode);
            }
            temp = queueHead;
        }
        return node;
    }

    /**
     * Helper method to find a specific DataEntry object within the linked list and heap structure.
     *
     * @param dataEntry The DataEntry object to search for.
     * @return The node containing the matching DataEntry object, or null if not found.
     */
    private Node find(DataEntry dataEntry){
        enQueue(head);
        QueueNode temp = queueHead;
        while (temp != null){
            Node node = deQueue();
            if (node.dataEntry.getKey() == dataEntry.getKey() && node.dataEntry.getValue().equals(dataEntry.getValue())){
                return node;
            } else {
                if (temp.node.leftNode!=null){
                    enQueue(node.leftNode);
                }
                if (temp.node.rightNode!=null){
                    enQueue(node.rightNode);
                }
                temp = queueHead;
            }
        }
        return null;
    }

    /**
     * Helper method to perform up-heap operations during insertion based on the current state (min-heap or max-heap).
     *
     * @param node The node to be up-heaped.
     */
    private void upHeap(Node node){
        boolean compareKey = false;
        while (node.parentNode!=null){
            if (state.equals("MIN")) {
                compareKey = node.parentNode.dataEntry.getKey() > node.dataEntry.getKey();
            } else if (state.equals("MAX")) {
                compareKey = node.parentNode.dataEntry.getKey() < node.dataEntry.getKey();
            }

            if (!compareKey) {
                break;
            }

            DataEntry temp = node.dataEntry;
            node.dataEntry = node.parentNode.dataEntry;
            node.parentNode.dataEntry = temp;
            node = node.parentNode;
        }
    }

    /**
     * Public method to insert a new DataEntry object into the priority queue.
     *
     * @param key The integer key for the new data entry.
     * @param value The string value for the new data entry.
     */
    public void insert(int key, String value){
        DataEntry dataEntry = new DataEntry(key, value);
        if (head == null) {
            head = new Node(null, dataEntry, null, null);
            size++;
        } else {
            Node lastParent = lastParent();
            Node temp = null;

            if (lastParent.leftNode == null) {
                lastParent.leftNode = new Node(lastParent, dataEntry, null, null);
                temp = lastParent.leftNode;
            } else if (lastParent.rightNode == null) {
                lastParent.rightNode = new Node(lastParent, dataEntry, null, null);
                temp = lastParent.rightNode;
            }

            size++;
            upHeap(temp);
        }
    }

    /**
     * Helper method to perform down-heap operations during removal based on the current state (min-heap or max-heap).
     *
     * @param node The node to be down-heaped.
     */
    private void downHeap(Node node){
        boolean compareKey;
        Node childKey = null;
        while (node.rightNode!=null || node.leftNode!=null){
            if (node.rightNode == null){
                childKey = node.leftNode;
            } else if (node.leftNode == null) {
                childKey = node.rightNode;
            } else{
                if (state.equals("MIN")) {
                    childKey = node.leftNode.dataEntry.getKey() < node.rightNode.dataEntry.getKey() ? node.leftNode : node.rightNode;
                } else if (state.equals("MAX")) {
                    childKey = node.leftNode.dataEntry.getKey() > node.rightNode.dataEntry.getKey() ? node.leftNode : node.rightNode;
                }
            }

            if (state.equals("MIN")) {
                compareKey = childKey.dataEntry.getKey() < node.dataEntry.getKey();
            } else {
                compareKey = childKey.dataEntry.getKey() > node.dataEntry.getKey();
            }

            if (!compareKey) {
                break;
            }

            DataEntry temp = node.dataEntry;
            node.dataEntry = childKey.dataEntry;
            childKey.dataEntry = temp;
            node = childKey;
        }
    }

    /**
     * Public method to remove the top element (highest or lowest priority based on state) from the priority queue.
     *
     * @return The DataEntry object removed from the top of the queue, or null if the queue is empty.
     */
    public DataEntry removeTop(){
        if (head == null){
            System.out.println("Empty List");
            return null;
        } else {
            DataEntry temp = head.dataEntry;
            Node lastNode = lastNode();
            head.dataEntry = lastNode.dataEntry;
            lastNode.dataEntry = temp;

            if (lastNode.parentNode == null) {
                head = null;
                size--;
                return temp;
            }else if (lastNode.parentNode.rightNode != null){
                lastNode.parentNode.rightNode = null;
            } else {
                lastNode.parentNode.leftNode = null;
            }
            lastNode.parentNode = null;

            downHeap(head);
            size--;
            return temp;
        }
    }

    /**
     * Public method to remove a specific DataEntry object from the priority queue.
     *
     * @param dataEntry The DataEntry object to be removed.
     * @return The DataEntry object that was removed, or null if not found.
     */
    public DataEntry remove(DataEntry dataEntry){
        if (head == null){
            System.out.println("Empty List");
            return null;
        } else {
            Node entry;
            if (find(dataEntry) != null){
                entry = find(dataEntry);
            } else {
                return null;
            }
            Node lastNode = lastNode();
            // Swap the node to be removed with the last node
            DataEntry temp = entry.dataEntry;
            entry.dataEntry = lastNode.dataEntry;
            lastNode.dataEntry = temp;

            // Update parent and child references for the swapped node (entry)
            if (lastNode.parentNode == null) {
                head = null;
                size--;
                return temp;
            }else if (lastNode.parentNode.rightNode != null){
                lastNode.parentNode.rightNode = null;
            } else {
                lastNode.parentNode.leftNode = null;
            }
            // Detach the removed node from the linked list structure
            lastNode.parentNode = null;
            upHeap(entry);
            downHeap(entry);
            size--;
            return temp;
        }
    }

    /**
     * Public method to replace the key of a specific DataEntry object within the priority queue.
     *
     * @param dataEntry The DataEntry object whose key needs to be replaced.
     * @param keyToReplace The new key value to be used.
     * @return The old key value of the DataEntry if found, -1 otherwise.
     */
    public int replaceKey(DataEntry dataEntry, int keyToReplace){
        if (head == null) {
            System.out.println("Empty List");
            return -1;
        } else {
            Node entry;
            if (find(dataEntry) != null){
                entry = find(dataEntry);
            } else {
                return -1;
            }
            int key = entry.dataEntry.getKey();
            entry.dataEntry.setKey(keyToReplace);
            upHeap(entry);
            downHeap(entry);
            return key;
        }
    }

    /**
     * Public method to replace the value of a specific DataEntry object within the priority queue.
     *
     * @param dataEntry The DataEntry object whose value needs to be replaced.
     * @param valueToReplace The new value string to be used.
     * @return The old value string of the DataEntry if found, null otherwise.
     */
    public String replaceValue(DataEntry dataEntry, String valueToReplace){
        if (head == null) {
            System.out.println("Empty List");
            return null;
        } else {
            Node entry;
            if (find(dataEntry) != null){
                entry = find(dataEntry);
            } else {
                return null;
            }
            String value = entry.dataEntry.getValue();
            entry.dataEntry.setValue(valueToReplace);
            return value;
        }
    }

    /**
     * Public method to toggle the state of the priority queue between min-heap and max-heap behavior.
     * This method performs a bottom-up reheapify operation to ensure the heap property holds after the state change.
     */
    public void toggle(){
        state = state.equals("MIN") ? "MAX" : "MIN";

        if (head != null) {
            reheapify(head);
        }
    }

    /**
     * Helper method to perform a bottom-up reheapify operation on the entire priority queue.
     * This method is used after the state of the queue is toggled between min-heap and max-heap.
     *
     * @param node The root node of the heap (used for initial call).
     */
    private void reheapify(Node node) {
        if (node == null) return;

        queueHead = null;
        enQueue(node);
        QueueNode temp = queueHead;

        while (temp != null) {
            Node current = deQueue();

            // Reheapify left subtree (process all children of the current node)
            Node left = current.leftNode;
            while (left != null) {
                enQueue(left);
                left = left.leftNode;
            }

            // Reheapify right subtree (process all children of the current node)
            Node right = current.rightNode;
            while (right != null) {
                enQueue(right);
                right = right.rightNode;
            }

            // Perform down-heap or up-heap operation
            upHeap(current);
            temp = queueHead;
        }
    }

    /**
     * Public method to return the DataEntry object at the top of the queue (highest or lowest priority based on state).
     *
     * @return The DataEntry object at the top of the queue, or null if the queue is empty.
     */
    public DataEntry top(){
        if (head!=null){
            return head.dataEntry;
        } else {
            return null;
        }
    }

    /**
     * Public method to check if the priority queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    public boolean isEmpty(){
        return head == null;
    }

    /**
     * Public method to return the current size of the priority queue (number of elements).
     *
     * @return The size of the queue.
     */
    public int getSize(){
        return size;
    }

    /**
     * Public method to get the current state of the priority queue (either "MIN" or "MAX").
     *
     * @return The current state of the queue.
     */
    public String getState() {
        return state;
    }

    /**
     * Public method to set the state of the priority queue (either "MIN" or "MAX").
     *
     * @param state The new state to be set ("MIN" or "MAX").
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Public method to display the contents of the priority queue in a tree-like format.
     * This method uses a recursive approach to traverse the linked list structure and print the key-value pairs of each node.
     *
     */
    public void displayHeap() {
        if (head == null){
            System.out.println("Empty List");
            return;
        }
        printHeap(head, 0);
    }

    /**
     * Helper method used for displaying the contents of the priority queue in a tree-like format.
     * This method recursively traverses the linked list structure, adding spaces for indentation and printing the key-value pairs.
     *
     * @param node The current node in the traversal.
     * @param space The amount of space for indentation (increases with depth).
     */
    private void printHeap(Node node, int space) {
        if (node == null) {
            return;
        }
        space += 10; // Increase indentation for each level

        printHeap(node.rightNode, space); // Recursively print right subtree
        System.out.println(); // New line for each level

        // Add spaces for indentation based on the current level
        for (int i = 10; i < space; i++) {
            System.out.print(" ");
        }
        // Print the key-value pair of the current node
        System.out.println("(" + node.dataEntry.getKey() + "," + node.dataEntry.getValue() + ")");

        printHeap(node.leftNode, space); // Recursively print left subtree
    }

}

/**
 * Public class representing a Smart Priority Queue (SPQ) data structure.
 * This class likely implements a priority queue using a heap data structure.
 * The priority queue can be configured to behave as either a min-heap (lowest priority at the top)
 * or a max-heap (highest priority at the top) using a state variable.
 */
public class SPQ {
    public static void main(String[] args) {
        // Scanner object for reading user input from the console
        Scanner scanner = new Scanner(System.in);
        // Create an instance of the SPQ class to manage the priority queue operations
        SPQList spqList = new SPQList();
        // Variables for storing user-provided data (key and value)
        DataEntry dataEntry;
        String value;
        int key;

        System.out.print("CHOOSE '1' FOR MENU DRIVEN USER INTERFACE OR '2' FOR REPRESENTATIVE EXAMPLES: ");
        int choose = scanner.nextInt();

        if (choose == 1){
        // Welcome message and default state information
        System.out.println("WELCOME TO SMART PRIORITY QUEUE");
        System.out.println("Default state of queue MIN selected");
        do {
            // Menu displaying available operations for the SPQ
            System.out.println("\n---------MENU---------");
            System.out.println("1. INSERT the values in Priority QUEUE");
            System.out.println("2. Return the TOP entry of the Priority QUEUE");
            System.out.println("3. DISPLAY Priority QUEUE");
            System.out.println("4. Remove TOP of the Priority QUEUE");
            System.out.println("5. toggle() the state of Priority QUEUE");
            System.out.println("6. Removes entry object e from the priority Priority QUEUE");
            System.out.println("7. Replace entry e’s key to k");
            System.out.println("8. Replace entry e’s value to v");
            System.out.println("9. STATE of Priority QUEUE");
            System.out.println("10. SIZE of Priority QUEUE");
            System.out.println("11. CHECK priority queue empty or not");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            // Read user's choice for the operation they want to perform
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    // Insert operation: allows user to enter multiple key-value pairs
                    // -1 key signifies the end of input
                    System.out.println("Enter your data in form of key value (3 ABY) or -1 to exit:");
                    key = scanner.nextInt();
                    while (key != -1) {
                        value = scanner.nextLine(); // Consume the newline character from previous input
                        spqList.insert(key, value); // Insert the key-value pair into the queue
                        key = scanner.nextInt();
                    }
                    System.out.println("Data entry successful");
                    break;
                case 2:
                    // Get and display the top element (highest/lowest priority based on state)
                    DataEntry top = spqList.top();
                    if (top != null){
                        System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
                    } else {
                        System.out.println("Empty List");
                    }
                    break;
                case 3:
                    // Display the contents of the queue in a tree-like format
                    spqList.displayHeap();
                    break;
                case 4:
                    // Remove and display the top element (highest/lowest priority based on state)
                    DataEntry removeTop = spqList.removeTop();
                    if (removeTop != null){
                        System.out.println("Removed the following data from top: ("+removeTop.getKey()+","+removeTop.getValue()+")");
                    } else {
                        System.out.println("Cannot remove data from empty list.");
                    }
                    break;
                case 5:
                    // Toggle state (MIN/MAX) and display queue before/after
                    System.out.println(spqList.getState()+ " before the toggle()");
                    spqList.displayHeap();
                    spqList.toggle();
                    System.out.println(spqList.getState()+ " after the toggle()");
                    spqList.displayHeap();
                    break;
                case 6:
                    // Remove a specific entry by key-value pair
                    System.out.print("Enter key and value pair to be removed from QUEUE: ");
                    key = scanner.nextInt();
                    value = scanner.nextLine();
                    dataEntry = new DataEntry(key, value);
                    dataEntry = spqList.remove(dataEntry);
                    if (dataEntry != null){
                        System.out.println("Removed ("+dataEntry.getKey()+","+dataEntry.getValue()+") successfully");
                    } else {
                        System.out.println("No Entry Found!");
                    }
                    break;
                case 7:
                    // Replace the key of an existing entry
                    System.out.print("Enter key and value pair of entry from QUEUE: ");
                    key = scanner.nextInt();
                    value = scanner.nextLine();
                    System.out.print("Enter new key to replace: ");
                    int keyToReplace = scanner.nextInt();
                    dataEntry = new DataEntry(key, value);
                    int oldKey = spqList.replaceKey(dataEntry, keyToReplace);
                    if (oldKey != -1){
                        System.out.println("Key replaced from "+oldKey+" to "+keyToReplace+" successfully");
                    } else {
                        System.out.println("No Entry Found!");
                    }
                    break;
                case 8:
                    // Replace the value of an existing entry
                    System.out.print("Enter key and value pair of entry from QUEUE: ");
                    key = scanner.nextInt();
                    value = scanner.nextLine();
                    System.out.print("Enter new value to replace: ");
                    String valueToReplace = scanner.nextLine();
                    dataEntry = new DataEntry(key, value);
                    String oldValue = spqList.replaceValue(dataEntry, valueToReplace);
                    if (oldValue != null){
                        System.out.println("Value replaced from"+oldValue+" to "+valueToReplace+" successfully");
                    } else {
                        System.out.println("No Entry Found!");
                    }
                    break;
                case 9:
                    // Get and display the current state (MIN/MAX)
                    String state = spqList.getState();
                    System.out.println("State of Priority Queue is: "+state);
                    break;
                case 10:
                    // Get and display the current number of elements
                    int size = spqList.getSize();
                    System.out.println("Current number of entries in queue are: "+size);
                    break;
                case 11:
                    // Check if the queue is empty
                    boolean empty = spqList.isEmpty();
                    System.out.print("Queue is ");
                    if (empty){
                        System.out.println("Empty");
                    } else {
                        System.out.println("Not Empty");
                    }
                    break;
                case 0:
                    // Exit the program
                    System.out.println("Thank you for using Smart Priority Queue :)");
                    System.exit(0);
                default:
                    // Handle invalid user choices
                    System.out.println("Invalid CHOICE!");
            }
        } while (true); // Loop continues until user exits (choice 0)
        } else {
            System.out.println("Example 1: Inserting elements in Priority QUEUE with key and values");
            spqList.insert(15 ,"K");
            spqList.insert(9 ,"F");
            spqList.insert(5 ,"Q");
            spqList.insert(7 ,"X");
            spqList.insert(16 ,"J");
            spqList.insert(25 ,"C");
            spqList.insert(4 ,"E");
            spqList.insert(14 ,"Z");
            spqList.insert(6 ,"Six");
            spqList.insert(12 ,"S");
            spqList.insert(11 ,"B");
            spqList.insert(20 ,"W");
            spqList.insert(8 ,"A");

            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 2: Top Element of the SPQ");
            DataEntry top = spqList.top();
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }

            System.out.println(" ");
            System.out.println("Example 3: Remove Top Element of the SPQ");
            DataEntry removeTop = spqList.removeTop();
            if (removeTop != null){
                System.out.println("Removed the following data from top: ("+removeTop.getKey()+","+removeTop.getValue()+")");
            } else {
                System.out.println("Cannot remove data from empty list.");
            }

            System.out.println(" ");
            System.out.println("Example 4: Remove ANY Element of the SPQ");
            dataEntry = new DataEntry(6, "Six");
            dataEntry = spqList.remove(dataEntry);
            if (dataEntry != null){
                System.out.println("Removed ("+dataEntry.getKey()+","+dataEntry.getValue()+") successfully");
            } else {
                System.out.println("No Entry Found!");
            }
            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 5: Convert Heap State");
            System.out.println(spqList.getState()+ " before the toggle()");
            spqList.toggle();
            System.out.println(spqList.getState()+ " after the toggle()");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 6: Top Element of the new State");
            top = spqList.top();
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }

            System.out.println(" ");
            System.out.println("Example 7: Replace key of top element");
            System.out.print("Top Element(Before replacing key): ");
            top = spqList.top();
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }
            spqList.replaceKey(top,19);
            System.out.print("Top Element(After replacing key): ");
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }
            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 8: Replace value of top element");
            System.out.print("Top Element(Before replacing value): ");
            top = spqList.top();
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }
            spqList.replaceValue(top,"CODE RED");
            System.out.print("Top Element(After replacing value): ");
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }
            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 9: Size of the heap");
            System.out.println("The size is: " + spqList.getSize());

            System.out.println(" ");
            System.out.println("Example 10: Check if SPQ is empty or not");
            System.out.println(spqList.isEmpty());

            System.out.println(" ");
            System.out.println("Example 11: Check the current state of the SPQ");
            System.out.println("Current state of heap is: "+ spqList.getState());

            System.out.println(" ");
            System.out.println("Example 12: Reinsertion of the Elements:");
            spqList.displayHeap();
            spqList.insert(1, "qw");
            spqList.insert(22, "re");
            spqList.insert(10, "io");
            System.out.println("Inserting 3 Elements: (1,qw) , (22,re) , (10,io)");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 13: Top Element after insertion");
            top = spqList.top();
            if (top != null){
                System.out.println("(" + top.getKey() + "," + top.getValue() + ")");
            } else {
                System.out.println("Empty List");
            }

            System.out.println(" ");
            System.out.println("Example 14: Remove multiple elements from top");
            System.out.println("Before Removal");
            spqList.displayHeap();
            spqList.removeTop();
            spqList.removeTop();
            spqList.removeTop();
            System.out.println("After Removal");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 15: Convert Heap State");
            spqList.toggle();
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 16: Replace ANY key");
            dataEntry = new DataEntry(1, "qw");
            int oldKey = spqList.replaceKey(dataEntry, 32);
            if (oldKey != -1){
                System.out.println("Key replaced from "+oldKey+" to 32 successfully");
            } else {
                System.out.println("No Entry Found!");
            }
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 17: Replace ANY value");
            dataEntry = new DataEntry(10, "io");
            String oldValue = spqList.replaceValue(dataEntry, "PPS");
            if (oldValue != null){
                System.out.println("Value replaced from "+oldValue+" to PPS successfully");
            } else {
                System.out.println("No Entry Found!");
            }
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 18: Remove ANY Element of the SPQ");
            dataEntry = new DataEntry(7, "X");
            dataEntry = spqList.remove(dataEntry);
            if (dataEntry != null){
                System.out.println("Removed ("+dataEntry.getKey()+","+dataEntry.getValue()+") successfully");
            } else {
                System.out.println("No Entry Found!");
            }
            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println(spqList.getSize());
            System.out.println("Example 19: Remove all elements");
            while(!spqList.isEmpty()){
                spqList.removeTop();
            }
            System.out.println(spqList.getSize());
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 20: Remove Top Element of the SPQ in Empty List");
            removeTop = spqList.removeTop();
            if (removeTop != null){
                System.out.println("Removed the following data from top: ("+removeTop.getKey()+","+removeTop.getValue()+")");
            } else {
                System.out.println("Cannot remove data from empty list.");
            }

            System.out.println();
            System.out.println("Inserting new Elements!");
            spqList.insert(25 ,"C");
            spqList.insert(4 ,"E");
            spqList.insert(14 ,"Z");
            spqList.insert(16 ,"H");
            spqList.insert(12 ,"S");
            spqList.insert(11 ,"B");
            spqList.insert(20 ,"W");
            spqList.insert(8 ,"A");
            System.out.println("Heap representation of Priority QUEUE");
            spqList.displayHeap();

            System.out.println(" ");
            System.out.println("Example 21: Size after insertion");
            System.out.println(spqList.getSize());
        }
    }
}