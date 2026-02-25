public class PalindromeCheckerApp {
    static void main() {
        String str = "Madam";
        boolean isPalindrome = true;
        for (int i = 0; i < str.length()/2; i++){
            if(str.charAt(i) != str.charAt(str.length() - 1-i){
                isPalindrome = false;
                break;
            }
        }
        if (isPalindrome){
            System.out.println(str + "is a Palindrome ");
        } else{
            System.out.println(str + "not a Palindrome ");
        }

    }
}
