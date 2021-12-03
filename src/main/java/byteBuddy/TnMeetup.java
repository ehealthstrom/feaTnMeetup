package byteBuddy;

public class TnMeetup {

    public static int enter, exit;

    public static void main(String[] args) {
        checkFoo("foo");
    }

    public static void checkFoo(String param) {
        if("foo".equals(param)) {
            System.out.println("foo");
        } else {
            System.out.println("bar");
        }
    }
}
