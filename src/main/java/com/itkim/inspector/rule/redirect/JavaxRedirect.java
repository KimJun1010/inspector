package com.itkim.inspector.rule.redirect;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1044
 * javax/servlet/http/HttpServletResponse sendRedirect (Ljava/lang/String;)V 1 false # REDIRECT # REDIRECT
 * javax/servlet/RequestDispatcher getRequestDispatcher (Ljava/lang/String;)Ljavax/servlet/RequestDispatcher; 1 false # REDIRECT # REDIRECT
 * javax/servlet/http/HttpServletRequest getRequestDispatcher (Ljava/lang/String;)Ljavax/servlet/RequestDispatcher; 1 false # REDIRECT # REDIRECT
 * org/springframework/web/servlet/ModelAndView <init> (Ljava/lang/String;)V 1 false # REDIRECT # REDIRECT
 * javax/ws/rs/core/Response seeOther * 1 all # REDIRECT # REDIRECT
 * javax/ws/rs/core/Response temporaryRedirect * 1 all # REDIRECT # REDIRECT
 */
public class JavaxRedirect extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("javax.redirect.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.http.HttpServletResponse", "sendRedirect")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.RequestDispatcher", "getRequestDispatcher")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.http.HttpServletRequest", "getRequestDispatcher")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.ws.rs.core.Response", "seeOther")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.ws.rs.core.Response", "temporaryRedirect")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.servlet.ModelAndView")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
