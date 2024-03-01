package com.itkim.inspector.rule.rce;

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
 * 1030
 * java/lang/ClassLoader defineClass ([BII)Ljava/lang/Class; 1 all # defineClass # RCE
 * java/lang/ClassLoader defineClass (Ljava/lang/String;[BII)Ljava/lang/Class; 2 all # defineClass # RCE
 * java/lang/ClassLoader defineClass (Ljava/lang/String;[BIILjava/security/ProtectionDomain;)Ljava/lang/Class; 2 all # defineClass # RCE
 * java/lang/ClassLoader defineClass (Ljava/lang/String;Ljava/nio/ByteBuffer;Ljava/security/ProtectionDomain;)Ljava/lang/Class; 2 all # defineClass # RCE
 * org/mozilla/javascript/DefiningClassLoader defineClass * 2 false # defineClass # RCE
 */
public class ClassLoaderRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("classLoader.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.ClassLoader", "defineClass")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mozilla.javascript.DefiningClassLoader", "defineClass")
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
