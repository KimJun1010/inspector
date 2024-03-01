package com.itkim.inspector.rule.ssrf;

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
 * 1066
 * com/squareup/okhttp/Request$Builder url (Ljava/lang/String;)Lcom/squareup/okhttp/Request$Builder; 1 false # SSRF # SSRF
 * okhttp3/Request$Builder url (Ljava/lang/String;)Lokhttp3/Request$Builder; 1 false # SSRF # SSRF
 */
public class OkhttpSSRF extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("okhttp.ssrf.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.squareup.okhttp.Request$Builder", "url")
                || SecExpressionUtils.hasFullQualifiedName(expression, "okhttp3.Request$Builder", "url")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
