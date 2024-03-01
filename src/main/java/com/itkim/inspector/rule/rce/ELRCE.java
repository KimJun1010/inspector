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
 * 1023
 * javax/el/ELProcessor eval (Ljava/lang/String;)Ljava/lang/Object; 1 false # EL # RCE
 * javax/el/ELProcessor getValue (Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object; 1 false # EL # RCE
 */
public class ELRCE extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("el.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.el.ELProcessor", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.el.ELProcessor", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.el.ExpressionFactory", "createValueExpression")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
