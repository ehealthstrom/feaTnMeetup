package javassist;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavassistMain {

    public static void main(String[] args) throws NotFoundException,
            CannotCompileException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {
        System.out.println("JavassistMain main invoked");

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("javassist.TnMeetup");
        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod method : methods) {
            System.out.println("method name: " + method.getName());
            method.insertAfter("System.out.println(\"Logging using Assist\");");
        }

        // http://www.javassist.org/html/javassist/CtClass.html#toClass()
        // illegal reflective access
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> c = cc.toClass(lookup);
        Object classInstance = c.getDeclaredConstructor().newInstance();

        Method checkFooMethod = c.getMethod("checkFoo", String.class);
        checkFooMethod.setAccessible(true);
        checkFooMethod.invoke(classInstance, "foo");
    }
}
