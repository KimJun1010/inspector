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
 * 1025
 * bsh/Interpreter eval * 1 false # bsh Code execution # RCE
 * org/springframework/scripting/bsh/BshScriptEvaluator evaluate * 1 false # bsh Code execution # RCE
 */
public class BSHRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("bsh.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "bsh.Interpreter", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.scripting.bsh.BshScriptEvaluator", "evaluate")
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
