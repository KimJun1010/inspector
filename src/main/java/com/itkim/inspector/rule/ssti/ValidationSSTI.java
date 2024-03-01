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
 * 1031
 * javax/validation/ConstraintValidatorContext buildConstraintViolationWithTemplate * -1 true # SSTI常用sink # SSTI
 * org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl buildConstraintViolationWithTemplate * -1 true # SSTI常用sink # SSTI
 */
public class ValidationSSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("validation.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.validation.ConstraintValidatorContext", "buildConstraintViolationWithTemplate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl", "buildConstraintViolationWithTemplate")
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
