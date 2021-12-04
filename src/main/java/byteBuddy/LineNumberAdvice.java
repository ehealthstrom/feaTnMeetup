package byteBuddy;

import net.bytebuddy.asm.Advice;
import java.lang.reflect.Method;

// Advice: https://javadoc.io/static/net.bytebuddy/byte-buddy/1.8.16/net/bytebuddy/asm/Advice.html
public class LineNumberAdvice {

    @Advice.OnMethodEnter
    private static void enter(@Advice.Origin Method origin) {
        // An element in a stack trace, as returned by Throwable.getStackTrace(). Each element represents a single stack frame. All stack frames except for the one at the top of the stack represent a method invocation. The frame at the top of the stack represents the execution point at which the stack trace was generated.
        // Typically, this is the point at which the throwable corresponding to the stack trace was created.
        // https://docs.oracle.com/javase/7/docs/api/java/lang/StackTraceElement.html
        StackTraceElement top = new Throwable().getStackTrace()[0];
        System.out.println("MethodName: " + origin.getName() + " enter with lineNumber: " + top.getLineNumber());
        TnMeetup.enter++;
    }

    @Advice.OnMethodExit
    private static void exit(@Advice.Origin Method origin) {
        StackTraceElement top = new Throwable().getStackTrace()[0];
        System.out.println("MethodName: " + origin.getName() + " exit with lineNumber: " + top.getLineNumber());
        TnMeetup.exit++;
    }
}
