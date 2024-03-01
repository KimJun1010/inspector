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
 * 1027
 * com/jcraft/jsch/JSch getSession * -1 false # JSchOSInjection setCommand/setPassword/setConfig # RCE
 */
public class JSchOSRCE extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("jschOS.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.jcraft.jsch.JSch", "getSession")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.jcraft.jsch.JSch", "setCommand")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.jcraft.jsch.JSch", "setPassword")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.jcraft.jsch.JSch", "setConfig")
                )
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };
    }
}
