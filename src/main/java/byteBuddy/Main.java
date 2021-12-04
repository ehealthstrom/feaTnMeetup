package byteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.pool.TypePool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Main {

    static final String ENTER = "enter";
    static final String EXIT = "exit";

    // TODO change type
    static final boolean isASMVisitorWrapperType = true;

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        ByteBuddyAgent.install();

        Class<?> type;
        if (!isASMVisitorWrapperType) {
            type = new ByteBuddy()
                    .redefine(TnMeetup.class)
                    // to - Returns an ASM visitor wrapper that matches the given matcher and applies this advice to the matched methods.
                    // on - Implements advice where every matched method is advised by the given type's advisory methods.
                    .visit(Advice.to(LineNumberAdvice.class).on(named("checkFoo")))
                    .make()
                    .load(Main.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                    .getLoaded();
        } else {
            type = new ByteBuddy()
                    .redefine(TnMeetup.class)
                    // A class visitor wrapper is used in order to register an intermediate ASM ClassVisitor which is applied to the main type
                    // created by a DynamicType.Builder but not to any AuxiliaryTypes, if any.
                    // https://javadoc.io/doc/net.bytebuddy/byte-buddy/latest/index.html
                    .visit(new AsmVisitorWrapper() {
                        // Defines the flags that are provided to any ClassWriter when writing a class. Typically, this gives opportunity to instruct ASM to compute stack map frames or the size of the local variables array and the operand stack.
                        // If no specific flags are required for applying this wrapper, the given value is to be returned.
                        // https://javadoc.io/doc/net.bytebuddy/byte-buddy/latest/index.html
                        @Override
                        public int mergeWriter(int i) {
                            return i;
                        }

                        @Override
                        public int mergeReader(int i) {
                            return i;
                        }

                        // Applies a ClassVisitorWrapper to the creation of a DynamicType.
                        // https://javadoc.io/doc/net.bytebuddy/byte-buddy/latest/index.html
                        // https://github.com/raphw/byte-buddy/blob/c7f8aabc77438bca00f93e2e4ff645ce3d35f78c/byte-buddy-dep/src/main/java/net/bytebuddy/asm/AsmVisitorWrapper.java

                        /**
                         * @param typeDescription - The instrumented type.
                         * @param classVisitor -  A ClassVisitor to become the new primary class visitor to which the created DynamicType is written to.
                         * @param context - The implementation context of the current instrumentation.
                         * @param typePool - The type pool that was provided for the class creation.
                         * @param fieldList - The instrumented type's fields.
                         * @param methodList - The instrumented type's methods non-ignored declared and virtually inherited methods.
                         * @param i - writerFlags - The ASM ClassWriter flags to consider.
                         * @param i1 - readerFlags - The ASM ClassReader flags to consider.
                         * @return
                         */
                        @Override
                        public ClassVisitor wrap(TypeDescription typeDescription, ClassVisitor classVisitor, Implementation.Context context, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fieldList, MethodList<?> methodList, int i, int i1) {
                            // A new ClassVisitor that usually delegates to the ClassVisitor delivered in the argument.
                            return new CustomClassVisitor(Opcodes.ASM9, classVisitor);
                        }
                    })
                    .make()
                    // Main.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent()
                    .load(Main.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent(ClassReloadingStrategy.Strategy.RETRANSFORMATION))
                    .getLoaded();
        }

        System.out.println("Byte Buddy finished");

        // method with argument, as BAR
        type.getDeclaredMethod("checkFoo", String.class).invoke(type.getDeclaredConstructor().newInstance(), "foo");

        if(!isASMVisitorWrapperType) {
            Field enterField = type.getDeclaredField(ENTER);
            System.out.println("enterField: " + enterField.get(type.getDeclaredConstructor().newInstance()));

            Field exitField = type.getDeclaredField(EXIT);
            System.out.println("exitField: " + exitField.get(type.getDeclaredConstructor().newInstance()));
        }
    }
}


