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
 * 1022
 * org/springframework/expression/ExpressionParser parseExpression * -1 true # SPEL # RCE
 * org/springframework/expression/spel/standard/SpelExpressionParser parseExpression * 1 true # SPEL # RCE
 * org/springframework/expression/spel/standard/SpelExpressionParser parseRaw * -1 true # SPEL # RCE
 */
public class SPELRCE extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("spel.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.expression.ExpressionParser", "parseExpression")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.expression.spel.standard.SpelExpressionParser", "parseExpression")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.expression.spel.standard.SpelExpressionParser", "parseRaw")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
