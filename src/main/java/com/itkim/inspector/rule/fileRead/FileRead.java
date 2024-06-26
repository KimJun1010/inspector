package com.itkim.inspector.rule.fileRead;

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
 * 1040
 * java/io/FileInputStream <init> * 1 false # 文件读取的sink # FILE_READ
 * java/lang/Class getResourceAsStream (Ljava/lang/String;)Ljava/io/InputStream; 1 false # 文件读取 # FILE_READ
 * <p>
 * org.apache.commons.io.FileUtils#readFileToByteArray
 * org.apache.commons.io.FileUtils#readFileToString(java.io.File)
 * org.apache.commons.io.FileUtils#readFileToString(java.io.File, java.nio.charset.Charset)
 * org.apache.commons.io.FileUtils#readFileToString(java.io.File, java.lang.String)
 * org.apache.commons.io.FileUtils#readLines(java.io.File)
 * org.apache.commons.io.FileUtils#readLines(java.io.File, java.nio.charset.Charset)
 * org.apache.commons.io.FileUtils#readLines(java.io.File, java.lang.String)
 */
public class FileRead extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("file.read.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.Class", "getResourceAsStream")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "readFileToByteArray")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "readFileToString")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.io.FileUtils", "readLines")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.io.FileInputStream")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
