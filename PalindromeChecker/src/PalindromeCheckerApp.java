package src;
import java.util.Scanner;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
public class PalindromeCheckerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = getTestInput(scanner);
        System.out.println("\n=== Performance Comparison (ns) ===");
        testTwoPointer(input);
        testStack(input);
        testDeque(input);
        testRecursive(input);
        scanner.close();
    }
    static void testTwoPointer(String input) {
        long start = System.nanoTime();
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
        boolean result = isPalindromeTwoPointer(normalized);
        long end = System.nanoTime();

        System.out.printf("Two-Pointer: %d ns - %s%n", (end - start), result ? "Palindrome" : "Not");
    }
    static void testStack(String input) {
        long start = System.nanoTime();
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
        boolean result = isPalindromeStack(normalized);
        long end = System.nanoTime();

        System.out.printf("Stack:        %d ns - %s%n", (end - start), result ? "Palindrome" : "Not");
    }
    static void testDeque(String input) {
        long start = System.nanoTime();
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
        boolean result = isPalindromeDeque(normalized);
        long end = System.nanoTime();

        System.out.printf("Deque:        %d ns - %s%n", (end - start), result ? "Palindrome" : "Not");
    }
    static void testRecursive(String input) {
        long start = System.nanoTime();
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
        boolean result = isPalindromeRecursive(normalized, 0, normalized.length() - 1);
        long end = System.nanoTime();

        System.out.printf("Recursive:    %d ns - %s%n", (end - start), result ? "Palindrome" : "Not");
    }
    static boolean isPalindromeTwoPointer(String str) {
        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) return false;
        }
        return true;
    }
    static boolean isPalindromeStack(String str) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) stack.push(str.charAt(i));
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != stack.pop()) return false;
        }
        return true;
    }
    static boolean isPalindromeDeque(String str) {
        Deque<Character> deque = new ArrayDeque<>();
        for (int i = 0; i < str.length(); i++) deque.addLast(str.charAt(i));
        while (deque.size() > 1) {
            if (deque.removeFirst() != deque.removeLast()) return false;
        }
        return true;
    }
    static boolean isPalindromeRecursive(String str, int left, int right) {
        if (left >= right) return true;
        if (str.charAt(left) != str.charAt(right)) return false;
        return isPalindromeRecursive(str, left + 1, right - 1);
    }
    static String getTestInput(Scanner scanner) {
        System.out.println("Enter test string (case-insensitive, spaces ignored):");
        return scanner.nextLine();
    }
}
