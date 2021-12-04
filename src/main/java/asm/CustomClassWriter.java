package asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class CustomClassWriter extends ClassLoader {

    String className;

    /*
    The ClassReader class parses a compiled class given as a byte array,
    and calls the corresponding visitXxx methods on the ClassVisitor
    instance passed as argument to its accept method. It can be seen as an
    event producer.
    */
    ClassReader reader;
    /*
    The ClassWriter class is a subclass of the ClassVisitor abstract class
    that builds compiled classes directly in binary form. It produces as
    output a byte array containing the compiled class, which can be retrieved
    with the toByteArray method. It can be seen as an event consumer.
    */
    ClassWriter writer;

    /*
    The ClassVisitor class delegates all the method calls it receives to
    another ClassVisitor instance. It can be seen as an event filter.
    */

    CustomClassVisitor customClassVisitor;

    public CustomClassWriter(String className) throws IOException {
        this.className = className;
        reader = new ClassReader(className);
        // https://asm.ow2.io/javadoc/org/objectweb/asm/ClassWriter.html
        writer = new ClassWriter(reader, COMPUTE_FRAMES | COMPUTE_MAXS);
    }

    public byte[] findMethod() {
        customClassVisitor = new CustomClassVisitor(writer);
        reader.accept(customClassVisitor, ClassReader.EXPAND_FRAMES);

        return writer.toByteArray();
    }

    protected Class<?> findClass() throws ClassNotFoundException {
        byte b[] = findMethod();

        File outputFile = new File("TnMeetup.class");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("TnMeetup class created");
        }

        /* name – The expected binary name of the class, or null if not known
            b – The bytes that make up the class data. The bytes in positions off through off+len-1 should have the format of a valid class file as defined by The Java™ Virtual Machine Specification.
            off – The start offset in b of the class data
            len – The length of the class data */
        return defineClass(className, b, 0, b.length);
    }
}

