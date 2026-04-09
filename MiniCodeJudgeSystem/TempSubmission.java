import java.util.Scanner;

abstract class vehicle{
	abstract void start();
	void fuel() {
		System.out.println("vehicle uses fuel");
	}
}

class car extends vehicle{
	void start() {
		System.out.println("car starts with fuel");
	}
}

// Rename 'Main' to 'TempSubmission' here!
public class TempSubmission {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        car k = new car();
        k.fuel();
        k.start();
    }
}
