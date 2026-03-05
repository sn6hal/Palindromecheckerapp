public class PalindromeCheckerApp {
    static void main() {
        String str = "Madam";
        boolean isPalindrome = true;
        int start = 0;
        for (int i = str.length() - 1; i >0; i--){
            if(str.charAt(i) != str.charAt(start)){
                isPalindrome = false;
                break;
            }
            start++;
        }
        if (isPalindrome){
            System.out.println(str + "is a Palindrome ");
        } else{
            System.out.println(str + " is not a Palindrome ");
        }

    }
}
