package com.itkim.inspector.rule.fileWrite;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;


/**
 * 1042
 * org/apache/commons/io/file/PathFilter accept (Ljava/nio/file/Path;Ljava/nio/file/attribute/BasicFileAttributes;)Ljava/nio/file/FileVisitResult; 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/file/PathUtils copyFile (Ljava/net/URL;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/file/PathUtils copyFileToDirectory (Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/file/PathUtils copyFileToDirectory (Ljava/net/URL;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/filefilter/FileFilterUtils filter (Lorg/apache/commons/io/filefilter/IOFileFilter;[Ljava/io/File;)[Ljava/io/File; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/filefilter/FileFilterUtils filterList (Lorg/apache/commons/io/filefilter/IOFileFilter;[Ljava/io/File;)Ljava/util/List; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/filefilter/FileFilterUtils filterSet (Lorg/apache/commons/io/filefilter/IOFileFilter;[Ljava/io/File;)Ljava/util/Set; 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/output/DeferredFileOutputStream writeTo (Ljava/io/OutputStream;)V 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/output/FileWriterWithEncoding write * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/output/LockableFileWriter write * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/output/XmlStreamWriter write * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyDirectoryToDirectory (Ljava/io/File;Ljava/io/File;)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyFile * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyFileToDirectory * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyInputStreamToFile (Ljava/io/InputStream;Ljava/io/File;)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyToDirectory * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils copyToFile * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils moveDirectory (Ljava/io/File;Ljava/io/File;)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils moveDirectoryToDirectory (Ljava/io/File;Ljava/io/File;Z)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils moveFile * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils moveFileToDirectory (Ljava/io/File;Ljava/io/File;Z)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils moveToDirectory (Ljava/io/File;Ljava/io/File;Z)V 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils touch (Ljava/io/File;)V 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils write * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils writeByteArrayToFile * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils writeLines * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/FileUtils writeStringToFile * 1 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/IOUtils copy * 2 false # 文件写入 # FILE_WRITE
 * org/apache/commons/io/RandomAccessFileMode create * 1 false # 文件写入 # FILE_WRIT
 */
public class CommonsIO extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("commonIO.file.write.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.file.PathFilter", "accept")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.file.PathUtils", "copyFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.file.PathUtils", "copyFileToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.filefilter.FileFilterUtils", "filter")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.filefilter.FileFilterUtils", "filterList")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.filefilter.FileFilterUtils", "filterSet")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.output.DeferredFileOutputStream", "writeTo")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.output.FileWriterWithEncoding", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.output.LockableFileWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.output.XmlStreamWriter", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyDirectoryToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyFileToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyInputStreamToFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "copyToFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "moveDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "moveDirectoryToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "moveFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "moveFileToDirectory")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "touch")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "write")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "writeByteArrayToFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "writeLines")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "writeStringToFile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.IOUtils", "copy")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.RandomAccessFileMode", "create")

                        //0.0.2 add
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.fileupload", "write")

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
