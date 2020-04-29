package com.example.ucas;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.itranswarp.compiler.JavaStringCompiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author pqc
 */
public class TransUtils {

    public static void getJsonFromPB() {
        String dir = System.getProperty("user.dir");
        String srcDir = dir + "/src/main/file";
        String DST_DIR = dir + "/src/main/java";

        File file1 = new File(srcDir+"/PBSchema.proto");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file1));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(stringBuilder.toString());
        String shell = "protoc -I=" + srcDir + " --java_out=" + DST_DIR + " " + srcDir + "/PBSchema.proto";
        try {
            Runtime.getRuntime().exec(shell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Class<?> classBook = null;
        try {
            File file = new File(DST_DIR + "/com/example/ucas/PBSchema.java");
            if (file.exists()) {
                JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
                int status = javac.run(null, null, null, "-d", System.getProperty("user.dir") + "/target/classes", DST_DIR + "/com/example/ucas/PBSchema.java");
                if (status == 0) {
                    ClassLoader.getSystemClassLoader().loadClass("com.example.ucas.PBSchema");
                    classBook = Class.forName("com.example.ucas.PBSchema").getClasses()[0];
                    Method methodBook = classBook.getDeclaredMethod("parseFrom", InputStream.class);
                    methodBook.setAccessible(true);
                    Message message = (Message) methodBook.invoke(classBook, new FileInputStream(srcDir + "/PBData"));
                    String json = JsonFormat.printToString(message);
                    ResultMessage.Result.Builder  resultMessage = ResultMessage.Result.newBuilder();
                    resultMessage.setCode(200);
                    resultMessage.setMessage("success");
                    resultMessage.setData(json);
                    ByteString a = resultMessage.build().toByteString();
                    ResultMessage.Result  resultMessage2 = ResultMessage.Result.parseFrom(a);
                    System.out.println(a);
                }

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
