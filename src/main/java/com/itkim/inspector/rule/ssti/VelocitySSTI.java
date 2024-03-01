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
 * 1032
 * org/apache/velocity/app/Velocity evaluate * 4 all # Velocity # SSTI
 * org/apache/velocity/app/Velocity mergeTemplate * 3 all # Velocity # SSTI
 * org/apache/velocity/app/VelocityEngine evaluate * 4 all # Velocity # SSTI
 * org/apache/velocity/app/VelocityEngine mergeTemplate * 3 all # Velocity # SSTI
 * org/apache/velocity/runtime/RuntimeServices evaluate * 4 all # Velocity # SSTI
 * org/apache/velocity/runtime/RuntimeServices parse * 1 all # Velocity # SSTI
 * org/apache/velocity/runtime/RuntimeSingleton parse * 1 all # Velocity # SSTI
 * org/apache/velocity/runtime/resource/util/StringResourceRepository putStringResource * 2 all # Velocity # SSTI
 */
public class VelocitySSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("velocity.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.app.Velocity", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.app.Velocity", "mergeTemplate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.app.VelocityEngine", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.app.VelocityEngine", "mergeTemplate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.runtime.RuntimeServices", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.runtime.RuntimeServices", "parse")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.runtime.RuntimeSingleton", "parse")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.velocity.runtime.resource.util.StringResourceRepository", "putStringResource")
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
