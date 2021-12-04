package asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.TraceClassVisitor;
import java.io.PrintWriter;


import static org.objectweb.asm.Opcodes.*;

public class CustomClassVisitor extends ClassVisitor {

    TraceClassVisitor tracer;
    PrintWriter pw = new PrintWriter(System.out);

    public CustomClassVisitor(ClassVisitor cv) {
        super(ASM9, cv);
        this.cv = cv;
        tracer = new TraceClassVisitor(cv, pw);
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/ClassVisitor.html#visitMethod(int,java.lang.String,java.lang.String,java.lang.String,java.lang.String%5B%5D)

    /**
     * @param access     - the method's access flags
     * @param name       - the method's name
     * @param desc       - the method's descriptor
     * @param signature  - the method's signature. May be null if the method parameters, return type and exceptions do not use generic types
     * @param exceptions - the internal names of the method's exception classe
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String desc,
                                     String signature,
                                     String[] exceptions) {

        if (name.equals("checkFoo")) {
            MethodVisitor methodVisitor = tracer.visitMethod(ACC_PUBLIC + ACC_STATIC, name, desc, signature, exceptions);

            return new CustomMethodAdapter(methodVisitor);

        }
        return tracer.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        tracer.visitEnd();
    }

}
