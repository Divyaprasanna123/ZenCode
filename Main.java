import java.util.*;
class Solution {
    public int romanToInt(String s) {
        int total = 0;
        
        for (int i = 0; i < s.length(); i++) {
            int value = getValue(s.charAt(i));
            
            // If next value is greater, subtract current
            if (i < s.length() - 1 && value < getValue(s.charAt(i + 1))) {
                total -= value;
            } else {
                total += value;
            }
        }
        
        return total;
    }
    
    // Helper method to get value of Roman character
    private int getValue(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}
class Main {
    public static void main(String[] args) {
        System.out.println(new Solution().romanToInt("III"));
    }
}
