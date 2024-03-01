package com.itkim.inspector.rule.fileWrite;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1041
 * java/io/FileOutputStream write (Ljava/lang/String;)V 1 false # 文件上传的sink # FILE_WRITE
 * java/nio/file/Files write * 1 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files write * 2 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files writeString * 1 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files writeString * 2 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files copy * 2 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files move * 2 false # 文件写入 # FILE_WRITE
 * java/nio/file/Files createDirectories * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createDirectory * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createFile * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createLink * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createSymbolicLink * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createTempDirectory * 1 false # 创建文件 # FILE_WRITE
 * java/nio/file/Files createTempFile (Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path; 1 false # 创建文件 # FILE_WRITE
 * org/springframework/web/multipart/MultipartFile transferTo (Ljava/io/File;)V 1 false # 文件写入 # FILE_WRITE
 * <p>
 * org/apache/tomcat/util/http/fileupload/FileItem write (Ljava/io/File;)V 1 false # 文件上传的sink # FILE_WRITE
 * javax/servlet/http/Part write (Ljava/lang/String;)V 1 false # 文件上传的sink # FILE_WRITE
 * java/io/PrintWriter print (Ljava/lang/String;)V 1 false # 文件写入的sink # FILE_WRITE
 * java/io/RandomAccessFile write ([B)V 1 false # 文件写入的sink # FILE_WRITE
 * java/io/RandomAccessFile writeBytes (Ljava/lang/String;)V 1 false # 文件写入的sink # FILE_WRITE
 * java/io/RandomAccessFile writeChars (Ljava/lang/String;)V 1 false # 文件写入的sink # FILE_WRITE
 * java/io/RandomAccessFile writeUTF (Ljava/lang/String;)V 1 false # 文件写入的sink # FILE_WRITE
 * <p>
 * org/springframework/util/FileCopyUtils copy ([BLjava/io/File;)V 2 false # 文件拷贝 # FILE_WRITE
 * org/springframework/util/FileCopyUtils copy (Ljava/io/InputStream;Ljava/io/OutputStream;)I 2 false # 文件拷贝 # FILE_WRITE
 * org/springframework/util/FileCopyUtils copy ([BLjava/io/OutputStream;)I 2 false # 文件拷贝 # FILE_WRITE
 * <p>
 * java/io/FileWriter append * 1 false # 文件上传的sink # FILE_WRITE
 * java/io/FileWriter write * 1 false # 文件上传的sink # FILE_WRITE
 * java/io/Writer append * 1 false # 需要classpath都加载才能检测到，会检测到很多内容。 # FILE_WRITE
 * java/io/Writer write * 1 false # 需要classpath都加载才能检测到，会检测到很多内容。 # FILE_WRITE
 * java/io/bufferedWriter write * 1 false # 当out字段为FileWriter时会写文件 # FILE_WRITE
 * java/io/OutputStream write * 1 false # 文件写入 # FILE_WRITE
 * java/io/ByteArrayOutputStream writeTo (Ljava/io/OutputStream;)V 1 false # 文件写入 # FILE_WRITE
 * java/io/BufferedOutputStream write * 1 false # 文件写入 # FILE_WRITE
 * java/io/DataOutputStream write * 1 false # 文件写入 # FILE_WRITE
 * java/io/DataOutputStream writeByte (I)V 1 false # 文件写入 # FILE_WRITE
 * java/io/DataOutputStream writeBytes (Ljava/lang/String;)V 1 false # 文件写入 # FILE_WRITE
 * java/io/DataOutputStream writeChars (Ljava/lang/String;)V 1 false # 文件写入 # FILE_WRITE
 * java/io/DataOutputStream writeUTF (Ljava/lang/String;)V 1 false # 文件写入 # FILE_WRITE
 * java/io/OutputStreamWriter write * 1 false # 文件写入 # FILE_WRITE
 * java/io/OutputStreamWriter append * 1 false # 文件写入 # FILE_WRITE
 * java/io/ObjectOutputStream writeObject (Ljava/lang/Object;)V 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream append * 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream format (Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 2 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream format (Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 3 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream format (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream format (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 2 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream print * 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream printf (Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream printf (Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 2 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream printf (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 2 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream printf (Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream; 3 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream println * 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream write * 1 false # 文件写入 # FILE_WRITE
 * java/io/PrintStream writeBytes * 1 false # 文件写入 # FILE_WRITE
 */
public class IOFiles extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("io.files.write.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.io.FileOutputStream", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "writeString")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "copy")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "move")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createLink")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createSymbolicLink")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createTempDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createTempFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.nio.file.Files", "createDirectories")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.multipart.MultipartFile", "transferTo")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.tomcat.http.fileupload.FileItem", "FileItem")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.http.Part", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "print")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.RandomAccessFile", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.RandomAccessFile", "writeBytes")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.RandomAccessFile", "writeChars")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.RandomAccessFile", "writeUTF")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.util.FileCopyUtils", "copy")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.FileWriter", "append")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.FileWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.Writer", "append")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.Writer", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.bufferedWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.OutputStream", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.ByteArrayOutputStream", "writeTo")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.BufferedOutputStream", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.DataOutputStream", "writeByte")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.DataOutputStream", "writeBytes")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.DataOutputStream", "writeChars")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.DataOutputStream", "writeUTF")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.OutputStreamWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.OutputStreamWriter", "append")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.ObjectOutputStream", "writeObject")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "append")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "format")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "print")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "printf")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "println")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "write")
//                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintStream", "writeBytes")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "format")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "print")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "printf")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.io.PrintWriter", "println")
                ) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
