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
 * 1018
 * org/mvel2/MVEL eval * 1 false # MVEL # RCE
 * org/mvel2/MVEL evalToBoolean * 1 false # MVEL # RCE
 * org/mvel2/MVEL evalToString * 1 false # MVEL # RCE
 * org/mvel2/MVEL executeAllExpression * 1 false # MVEL # RCE
 * org/mvel2/MVEL executeExpression * 1 false # MVEL # RCE
 * org/mvel2/MVEL executeSetExpression * 1 false # MVEL # RCE
 * org/mvel2/MVELRuntime execute * 2 false # MVEL # RCE
 * org/mvel2/compiler/Accessor getValue * 0 false # MVEL # RCE
 * org/mvel2/compiler/CompiledAccExpression getValue * 0 false # MVEL # RCE
 * org/mvel2/compiler/CompiledExpression getDirectValue * 0 false # MVEL # RCE
 * org/mvel2/compiler/ExecutableStatement getValue * 0 false # MVEL # RCE
 * org/mvel2/jsr223/MvelCompiledScript eval * 0 false # MVEL # RCE
 * org/mvel2/jsr223/MvelScriptEngine eval * 1 false # MVEL # RCE
 * org/mvel2/jsr223/MvelScriptEngine evaluate * 1 false # MVEL # RCE
 * org/mvel2/templates/TemplateRuntime eval * 1 false # MVEL # RCE
 * org/mvel2/templates/TemplateRuntime execute * 1 false # MVEL # RCE
 */
public class MVELRCE extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("mvel.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "evalToBoolean")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "evalToString")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "executeAllExpression")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "executeExpression")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVEL", "executeSetExpression")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.MVELRuntime", "execute")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.compiler.Accessor", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.compiler.CompiledAccExpression", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.compiler.CompiledExpression", "getDirectValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.compiler.ExecutableStatement", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.jsr223.MvelCompiledScript", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.jsr223.MvelScriptEngine", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.jsr223.MvelScriptEngine", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.templates.TemplateRuntime", "eval")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.mvel2.templates.TemplateRuntime", "execute"))
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };
    }
}
