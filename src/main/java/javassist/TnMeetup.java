package javassist;

public class TnMeetup {

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
