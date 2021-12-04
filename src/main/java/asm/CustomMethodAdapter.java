package asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class CustomMethodAdapter extends MethodVisitor {
    private Label startingLabel;

    public CustomMethodAdapter(MethodVisitor mv) {
        super(ASM9, mv);
        startingLabel = new Label();
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitCode()
    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLabel(startingLabel);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;");
        mv.visitLdcInsn("hello ASM");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V", false);

    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        System.out.println("visitLabel invoked: " + label);
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitLineNumber(int,org.objectweb.asm.Label)

    /**
     * @param line  -  a line number. This number refers to the source file from which the class was compiled
     * @param start - the first instruction corresponding to this line number
     *              <p>
     *                                         TODO : Throws - java.lang.IllegalArgumentException - if start has not already been visited by this visitor!!!!!
     */

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        System.out.println("visitLineNumber line: " + line + ", startingLabel: " + start);
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitFrame(int,int,java.lang.Object%5B%5D,int,java.lang.Object%5B%5D)
    // Visits the current state of the local variables and operand stack elements. This method must(*) be called just before any instruction i that follows an unconditional branch instruction
    // INVOKED JUST BEFORE ANY JUMP

    /**
     * @param type     - the type of this stack map frame
     * @param numLocal - the number of local variables in the visited frame
     * @param local    - the local variable types in this frame
     * @param numStack - the number of operand stack elements
     * @param stack    - the operand stack types in this frame
     */
    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, local, numStack, stack);
        System.out.println("visitFrame invoked");
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitJumpInsn(int,org.objectweb.asm.Label)
    // opcodes: https://asm.ow2.io/javadoc/constant-values.html#org.objectweb.asm.Opcodes.IFEQ
    // Visits a jump instruction. A jump instruction is an instruction that may jump to another instruction.

    /**
     * @param opcode - the opcode of the type instruction to be visited. This opcode is either IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     * @param label  - which the jump instruction may jump
     */
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        System.out.println("visitJumpInsn invoked: " + label + ", opcode: " + opcode);
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitLookupSwitchInsn(org.objectweb.asm.Label,int%5B%5D,org.objectweb.asm.Label%5B%5D)
    // Visits a LOOKUPSWITCH instruction...

    /**
     * @param dflt   - beginning of the default handler block.
     * @param keys   - the values of the keys.
     * @param labels - beginnings of the handler blocks. labels[i] is the beginning of the handler block for the keys[i] key.
     */
    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        System.out.println("visitLookupSwitchInsn invoked begin: " + dflt + " labels: " + getLabelsString(labels) + ", keys: " + getKeyString(keys));
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitTableSwitchInsn(int,int,org.objectweb.asm.Label,org.objectweb.asm.Label...)
    // Visits a TABLESWITCH instruction....

    /**
     * @param min    - the minimum key value.
     * @param max    - the maximum key value.
     * @param dflt   - beginning of the default handler block.
     * @param labels - beginnings of the handler blocks. labels[i] is the beginning of the handler block for the min + i key.
     */
    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        System.out.println("visitTableSwitchInsn invoked default: " + dflt + " labels: " + labels);
    }

    // https://asm.ow2.io/javadoc/org/objectweb/asm/MethodVisitor.html#visitTryCatchBlock(org.objectweb.asm.Label,org.objectweb.asm.Label,org.objectweb.asm.Label,java.lang.String)
    // Visits a try catch block....

    /**
     * @param start   - the beginning of the exception handler's scope (inclusive).
     * @param end     - the end of the exception handler's scope (exclusive).
     * @param handler - the beginning of the exception handler's code.
     * @param type    - the internal name of the type of exceptions handled by the handler, or null to catch any exceptions (for "finally" blocks).
     *                <p>
     *                The Handler class is used to represent try catch blocks.
     *                Each handler references three Label objects that define the start and end of the try block, and the start of the catch block.
     */
    //
    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        System.out.println("visitTryCatchBlock invoked with start: " + start + ", end: " + end + ", handler: " + handler + ", type: " + type);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println("visitEnd invoked");
    }

    private String getLabelsString(Label[] labels) {
        String labelsString = "";
        for (Label label : labels) {
            labelsString += " " + label;
        }
        return labelsString;
    }

    private String getKeyString(int[] keys) {
        String keyString = "";
        for (int key : keys) {
            keyString += " " + key;
        }
        return keyString;
    }
}


