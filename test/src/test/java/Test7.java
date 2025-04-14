import java.util.*;

class Node {
    int val;
    Node next;
    public Node(int val) {
        this.val = val;
    }
}

public class Test7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Node dummy = new Node(0);
        Node cur = dummy;
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            cur.next = new Node(x);
            cur = cur.next;
        }

        Node newHead = reverse(dummy.next);
        cur = newHead;
        while (cur != null) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
    }

    static Node reverse(Node head) {
        Node pre = null;
        Node cur = head;
        while (cur != null) {
            Node nxt = cur.next;
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }
}
