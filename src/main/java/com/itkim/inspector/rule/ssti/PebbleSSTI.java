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
 * 1033
 * com/mitchellbosecke/pebble/PebbleEngine getLiteralTemplate * 1 all # pebble # SSTI
 * com/mitchellbosecke/pebble/PebbleEngine getTemplate * 1 all # pebble # SSTI
 */
public class PebbleSSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("pebble.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.mitchellbosecke.pebble.PebbleEngine", "getLiteralTemplate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.mitchellbosecke.pebble.PebbleEngine", "getTemplate")
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
