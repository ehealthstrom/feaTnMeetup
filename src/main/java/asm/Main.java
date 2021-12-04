package asm;



import java.io.IOException;

// Add hello ASM println to the 1st label, see result in TnMeetup class
public class Main {
    public static void main(String[] args) throws IOException {

        // FooTryCatch FooTNMeetup
        CustomClassWriter ccw = new CustomClassWriter("TnMeetup");
        try {
            Class<Object> c = (Class<Object>) ccw.findClass();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // release ccw for easy garbage collection
            ccw = null;
        }
    }
}
