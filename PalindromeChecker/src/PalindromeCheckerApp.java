package src;
import java.util.Scanner;
class PalindromeChecker {
    private String lastChecked;
    public boolean checkPalindrome(String input) {
        this.lastChecked = input;
        String normalized = input.toLowerCase().replaceAll("\\s+", "");
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
    public String getLastChecked() {
        return lastChecked;
    }
}
public class PalindromeCheckerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PalindromeChecker service = new PalindromeChecker();
        System.out.println("Enter a string to check if it's a palindrome:");
        String input = scanner.nextLine();
        boolean isPalindrome = service.checkPalindrome(input);
        if (isPalindrome) {
            System.out.println("The string is a palindrome.");
        } else {
            System.out.println("The string is not a palindrome.");
        }
        System.out.println("Last checked: " + service.getLastChecked());
        scanner.close();
    }
}
