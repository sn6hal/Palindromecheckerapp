package src;
import java.util.Scanner;
class ListNode {
    char data;
    ListNode next;
    ListNode(char data) {
        this.data = data;
        this.next = null;
    }
}
public class PalindromeCheckerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string to check if it's a palindrome:");
        String input = scanner.nextLine();
        boolean isPalindrome = isPalindromeLinkedList(input);
        if (isPalindrome) {
            System.out.println("The string is a palindrome.");
        } else {
            System.out.println("The string is not a palindrome.");
        }
        scanner.close();
    }
    public static boolean isPalindromeLinkedList(String str) {
        if (str.isEmpty()) return true;

        ListNode head = buildLinkedList(str);
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode secondHalf = reverseList(slow.next);
        return compareLists(head, secondHalf);
    }
    public static ListNode buildLinkedList(String str) {
        ListNode head = new ListNode(str.charAt(0));
        ListNode current = head;
        for (int i = 1; i < str.length(); i++) {
            current.next = new ListNode(str.charAt(i));
            current = current.next;
        }
        return head;
    }
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null, current = head;
        while (current != null) {
            ListNode nextTemp = current.next;
            current.next = prev;
            prev = current;
            current = nextTemp;
        }
        return prev;
    }
    public static boolean compareLists(ListNode head1, ListNode head2) {
        ListNode p1 = head1, p2 = head2;
        while (p1 != null && p2 != null) {
            if (p1.data != p2.data) return false;
            p1 = p1.next;
            p2 = p2.next;
        }
        return p1 == null && p2 == null;
    }
}
