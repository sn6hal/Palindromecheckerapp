package src;
import java.util.Scanner;
public class PalindromeCheckerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string to check if it's a palindrome (ignores case & spaces):");
        String input = scanner.nextLine();
        boolean isPalindrome = isPalindromeIgnoreCaseSpace(input);
        if (isPalindrome) {
            System.out.println("The string is a palindrome (ignoring case & spaces).");
        } else {
            System.out.println("The string is not a palindrome.");
        }
        scanner.close();
    }
    public static boolean isPalindromeIgnoreCaseSpace(String str) {
        String normalized = str.toLowerCase().replaceAll("\\s+", "");
        int left = 0;
        int right = normalized.length() - 1;
        while (left < right) {
            if (normalized.charAt(left) != normalized.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
