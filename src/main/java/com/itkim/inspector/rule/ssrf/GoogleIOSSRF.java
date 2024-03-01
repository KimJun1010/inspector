package com.itkim.inspector.rule.ssrf;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethodCallExpression;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1067
 * com/google/common/io/Resources asByteSource (Ljava/net/URL;) 1 false # SSRF # SSRF
 * com/google/common/io/Resources asCharSource (Ljava/net/URL;Ljava/nio/charset/Charset;) 1 false # SSRF # SSRF
 * com/google/common/io/Resources copy (Ljava/net/URL;Ljava/io/OutputStream;) 1 false # SSRF # SSRF
 * com/google/common/io/Resources readLines * 1 false # SSRF # SSRF
 * com/google/common/io/Resources toByteArray (Ljava/net/URL;) 1 false # SSRF # SSRF
 * com/google/common/io/Resources toString (Ljava/net/URL;Ljava/nio/charset/Charset;) 1 false # SSRF # SSRF
 */
public class GoogleIOSSRF extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("google.ssrf.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "asByteSource")
                || SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "asCharSource")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "copy")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "readLines")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "toByteArray")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.google.common.io.Resources", "toString")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
