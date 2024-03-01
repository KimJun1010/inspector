package com.itkim.inspector.rule.dos;

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
 * 1038
 * java/lang/System exit * -1 false # 系统退出导致DOS # DOS
 * java/lang/Shutdown exit * -1 false # 系统退出导致DOS # DOS
 * java/lang/Runtime exit * -1 false # 系统退出导致DOS # DOS
 */
public class SystemDOS extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("system.exit.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.System", "exit")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.Shutdown", "exit")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.Runtime", "exit")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
