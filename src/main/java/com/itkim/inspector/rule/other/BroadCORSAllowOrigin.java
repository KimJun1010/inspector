/*
 * Copyright 2020 momosecurity.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itkim.inspector.rule.other;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1069: 宽泛的 CORS Allowed Origin 设置
 * <p>
 * ref: https://rules.sonarsource.com/java/type/Security%20Hotspot/RSPEC-5122
 */
public class BroadCORSAllowOrigin extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("board.cors.allow.origin.msg");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitAnnotation(PsiAnnotation annotation) {

                // Spring MVC framework #CorssOrigin
                if ("org.springframework.web.bind.annotation.CrossOrigin".equals(annotation.getQualifiedName())) {
                    PsiAnnotationParameterList psiAnnotationParameterList = annotation.getParameterList();
                    PsiNameValuePair[] nameValuePairs = psiAnnotationParameterList.getAttributes();
                    if (nameValuePairs.length == 0) {
                        holder.registerProblem(annotation, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                    } else {
                        for (PsiNameValuePair nameValuePair : nameValuePairs) {
                            if (("value".equals(nameValuePair.getAttributeName()) ||
                                    "origins".equals(nameValuePair.getAttributeName())
                            ) && "*".equals(nameValuePair.getLiteralValue())
                                // todo. value and origins field could be a list
                            ) {
                                holder.registerProblem(annotation, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                            }
                        }
                    }
                }
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {

                // Java servlet framework
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.http.HttpServletResponse", "setHeader") ||
                        SecExpressionUtils.hasFullQualifiedName(expression, "javax.servlet.http.HttpServletResponse", "addHeader")
                ) {
                    PsiExpressionList expressionList = expression.getArgumentList();
                    PsiExpression[] args = expressionList.getExpressions();
                    if (args.length == 2 &&
                            args[0] instanceof PsiLiteralExpression &&
                            args[1] instanceof PsiLiteralExpression
                    ) {
                        Object arg0 = ((PsiLiteralExpression) args[0]).getValue();
                        Object arg1 = ((PsiLiteralExpression) args[1]).getValue();
                        if (arg0 instanceof String && arg1 instanceof String &&
                                "access-control-allow-origin".equals(((String) arg0).toLowerCase()) && "*".equals(arg1)
                        ) {
                            holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        }
                    }
                }

                // Spring MVC framework #cors.CorsConfiguration
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.cors.CorsConfiguration", "addAllowedOrigin")) {
                    PsiExpressionList expressionList = expression.getArgumentList();
                    PsiExpression[] args = expressionList.getExpressions();
                    if (args.length == 1) {
                        if (args[0] instanceof PsiLiteralExpression &&
                                "*".equals(((PsiLiteralExpression) args[0]).getValue())
                        ) {
                            holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        } else if (args[0] instanceof PsiReferenceExpression) {
                            PsiReferenceExpression refArg = (PsiReferenceExpression) args[0];
                            if ("CorsConfiguration.ALL".equals(refArg.getQualifiedName())) {
                                holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                            }
                        }
                    }
                }
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.cors.CorsConfiguration", "applyPermitDefaultValues")) {
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }


                // Spring MVC framework #web.servlet.config.annotation.CorsRegistration
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.servlet.config.annotation.CorsRegistry", "addMapping")) {
                    PsiElement parent = expression;
                    boolean foundAllowedOriginsSetup = false;
                    do {
                        parent = parent.getParent();
                        if (parent instanceof PsiMethodCallExpression &&
                                SecExpressionUtils.hasFullQualifiedName((PsiMethodCallExpression) parent, "org.springframework.web.servlet.config.annotation.CorsRegistration", "allowedOrigins")
                        ) {
                            foundAllowedOriginsSetup = true;
                            PsiExpressionList expressionList = ((PsiMethodCallExpression) parent).getArgumentList();
                            PsiExpression[] args = expressionList.getExpressions();
                            if (args.length == 1 &&
                                    args[0] instanceof PsiLiteralExpression &&
                                    "*".equals(((PsiLiteralExpression) args[0]).getValue())
                            ) {
                                holder.registerProblem(parent, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                                break;
                            }
                        }
                    } while (!(parent instanceof PsiCodeBlock));

                    if (!foundAllowedOriginsSetup) {
                        holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                    }
                }
            }
        };
    }
}
