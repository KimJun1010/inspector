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
 * 1020
 * com/opensymphony/xwork2/ognl/OgnlUtil callMethod * 1 false # ognl-injection # RCE
 * com/opensymphony/xwork2/ognl/OgnlUtil getValue * 1 false # ognl-injection # RCE
 * com/opensymphony/xwork2/ognl/OgnlUtil setValue * 1 false # ognl-injection # RCE
 * ognl/Node getValue * 0 false # ognl-injection # RCE
 * ognl/Node setValue * 0 false # ognl-injection # RCE
 * ognl/Ognl getValue * 1 false # ognl-injection # RCE
 * ognl/Ognl setValue * 1 false # ognl-injection # RCE
 * ognl/enhance/ExpressionAccessor get * 0 all # ognl-injection # RCE
 * ognl/enhance/ExpressionAccessor set * 0 all # ognl-injection # RCE
 * org/apache/commons/ognl/Node getValue * 0 all # ognl-injection # RCE
 * org/apache/commons/ognl/Node setValue * 0 all # ognl-injection # RCE
 * org/apache/commons/ognl/Ognl getValue * 1 false # ognl-injection # RCE
 * org/apache/commons/ognl/Ognl setValue * 1 false # ognl-injection # RCE
 * org/apache/commons/ognl/enhance/ExpressionAccessor get * 0 all # ognl-injection # RCE
 * org/apache/commons/ognl/enhance/ExpressionAccessor set * 0 all # ognl-injection # RCE
 */
public class OGNLInjectionRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("ognl.injection.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.opensymphony.xwork2.ognl.OgnlUtil", "callMethod")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.opensymphony.xwork2.ognl.OgnlUtil", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.opensymphony.xwork2.ognl.OgnlUtil", "setValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.Node", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.Node", "setValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.Ognl", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.Ognl", "setValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.enhance.ExpressionAccessor", "get")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "ognl.enhance.ExpressionAccessor", "set")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.Node", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.Node", "setValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.Ognl", "getValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.Ognl", "setValue")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.enhance.ExpressionAccessor", "get")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.ognl.enhance.ExpressionAccessor", "set")
                )
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
        };
    }
}
