package com.itkim.inspector.rule.rce;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

public class QLExpressRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("qlexpress.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            // 重写 visitNewExpression 方法，检测 new 表达式
            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                PsiJavaCodeReferenceElement classReference = expression.getClassReference();
                if (classReference != null) {
                    String qualifiedName = classReference.getQualifiedName();
                    if ("com.ql.util.express.ExpressRunner".equals(qualifiedName)) {
                        holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                    }
                }
            }

            // 继续保留对 execute() 方法的检测
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.ql.util.express.ExpressRunner", "execute")) {
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }



}