package byteBuddy;

import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;

import static net.bytebuddy.jar.asm.Opcodes.GETSTATIC;
import static net.bytebuddy.jar.asm.Opcodes.INVOKEVIRTUAL;

public class CustomMethodVisitor extends MethodVisitor {
    public CustomMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode() {
        //System.out.println("CustomMethodVisitor visitCode");
        addPrintStatement();
        super.visitCode();
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        //System.out.println("CustomMethodVisitor visitFrame");
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitLabel(Label label) {
        //System.out.println("CustomMethodVisitor visitLabel: " + label);
        super.visitLabel(label);
        //addPrintStatement(label);
    }

    public void addPrintStatement() {
        Label l0 = new Label();
        super.visitLabel(l0);
        super.visitLineNumber(0, l0);
        //visitLocalVariable("isTouchedAdded", "Z", null, isTouchedStart, isTouchedEnd, isTouchedVar);
        // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitFieldInsn(int,java.lang.String,java.lang.String,java.lang.String)
        super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitLdcInsn(java.lang.Object)
        super.visitLdcInsn("added printLn by ByteBuddy"); // Ldc: bears an index into the constant pool holding the string
        // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitMethodInsn(int,java.lang.String,java.lang.String,java.lang.String,boolean)
        // INVOKEVIRTUAL - for methods, INVOKESPECIAL - for constructor
        super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        // maybe this is useless...
        //mv.visitMaxs(-1, -1);
        super.visitEnd();
    }
}
