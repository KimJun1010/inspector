package com.itkim.inspector.rule.redirect;

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
 * 1045
 * jakarta/ws/rs/core/Response seeOther * 1 all # REDIRECT # REDIRECT
 * jakarta/ws/rs/core/Response temporaryRedirect * 1 all # REDIRECT # REDIRECT
 */
public class JakartaRedirect extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jakarta.redirect.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "jakarta.ws.rs.coreResponse.", "seeOther")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "jakarta.ws.rs.coreResponse", "temporaryRedirect")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

        };
    }
}
