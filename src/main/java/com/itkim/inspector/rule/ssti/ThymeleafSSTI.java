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
 * 1036
 * org/thymeleaf/ITemplateEngine process * 1 all # thymeleaf # SSTI
 * org/thymeleaf/ITemplateEngine processThrottled * 1 all # thymeleaf # SSTI
 */
public class ThymeleafSSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("thymeleaf.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.thymeleaf.ITemplateEngine", "process")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.thymeleaf.ITemplateEngine", "processThrottled")
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
