package src;
import java.util.Scanner;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
interface PalindromeStrategy {
    boolean checkPalindrome(String input);
    String getName();
}
class StackStrategy implements PalindromeStrategy {
    @Override
    public boolean checkPalindrome(String str) {
        Stack<Character> stack = new Stack<>();
        String normalized = str.toLowerCase().replaceAll("\\s+", "");

        for (int i = 0; i < normalized.length(); i++) {
            stack.push(normalized.charAt(i));
        }

        for (int i = 0; i < normalized.length(); i++) {
            if (normalized.charAt(i) != stack.pop()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String getName() {
        return "Stack Strategy (LIFO)";
    }
}
class DequeStrategy implements PalindromeStrategy {
    @Override
    public boolean checkPalindrome(String str) {
        Deque<Character> deque = new ArrayDeque<>();
        String normalized = str.toLowerCase().replaceAll("\\s+", "");
        for (int i = 0; i < normalized.length(); i++) {
            deque.addLast(normalized.charAt(i));
        }
        while (deque.size() > 1) {
            if (deque.removeFirst() != deque.removeLast()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String getName() {
        return "Deque Strategy (Double-Ended)";
    }
}
class PalindromeContext {
    private PalindromeStrategy strategy;
    public void setStrategy(PalindromeStrategy strategy) {
        this.strategy = strategy;
    }
    public boolean check(String input) {
        return strategy.checkPalindrome(input);
    }
    public String getStrategyName() {
        return strategy.getName();
    }
}
public class PalindromeCheckerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PalindromeContext context = new PalindromeContext();
        System.out.println("Enter a string to check if it's a palindrome:");
        String input = scanner.nextLine();
        context.setStrategy(new StackStrategy());
        boolean result1 = context.check(input);
        System.out.println("Stack Strategy: " + (result1 ? "Palindrome" : "Not Palindrome"));
        context.setStrategy(new DequeStrategy());
        boolean result2 = context.check(input);
        System.out.println("Deque Strategy: " + (result2 ? "Palindrome" : "Not Palindrome"));
        scanner.close();
    }
}
