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
 * 1006
 * javax/script/Compilable compile (Ljava/lang/String)Ljavax/script/CompiledScript; 1 false # ScriptEngine # RCE
 * javax/script/CompiledScript eval * 0 false # ScriptEngine # RCE
 */
public class CompilableRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("compilable.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.script.Compilable", "compile")
                || SecExpressionUtils.hasFullQualifiedName(expression, "javax.script.Compilable", "eval")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
