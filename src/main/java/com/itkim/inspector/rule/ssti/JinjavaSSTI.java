package com.itkim.inspector.rule.ssti;

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
 * 1034
 * com/hubspot/jinjava/Jinjava render * 1 all # jinjava # SSTI
 * com/hubspot/jinjava/Jinjava renderForResult * 1 all # jinjava # SSTI
 */
public class JinjavaSSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jinjava.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.hubspot.jinjava.Jinjava", "render")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.hubspot.jinjava.Jinjava", "renderForResult")
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
