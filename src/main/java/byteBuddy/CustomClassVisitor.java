package byteBuddy;

import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;

public class CustomClassVisitor extends ClassVisitor {
    public CustomClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if(name.equals("checkFoo")) {
            return new CustomMethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions));
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
