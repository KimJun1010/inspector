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
package com.itkim.inspector.rule.sqli;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SQLiUtil;
import com.itkim.inspector.utils.SecExpressionUtils;
import com.siyeh.ig.psiutils.ExpressionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 1047: 占位符拼接型SQL注入漏洞
 * <p>
 * eg.
 * <p>
 * (1) String.format("select * from table where id = %s", id);
 * (2) String.format("select * from table where id = %1$3s", id);
 */
public class PlaceholderStringSQLi extends BaseSQLi {
    public static final String MESSAGE = InspectionBundle.message("placeholder.string.sqli.msg");
//    private static final String QUICK_FIX_NAME = InspectionBundle.message("placeholder.string.sqli.fix");

//    private final ShowHelpCommentQuickFix showHelpCommentQuickFix = new ShowHelpCommentQuickFix(QUICK_FIX_NAME, SQL_INJECTION_HELP_COMMENT);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (!SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.String", "format")) {
                    return;
                }
                PsiExpression[] args = expression.getArgumentList().getExpressions();
                if (!(args.length > 0)) {
                    return;
                }
                if (ignoreMethodName(expression)) {
                    return;
                }

                int idx = 0;
                PsiType arg0Type = args[idx].getType();
                if (arg0Type != null && "Locale".equals(arg0Type.getPresentableText())) {
                    idx += 1;
                }
                String content = getLiteralOrVariableLiteral(args[idx]);

                if (content != null &&
                        SQLiUtil.placeholderPattern.matcher(content).find() &&
                        isSql(content)
                ) {
                    List<String> split_cont_by_placeholder = new ArrayList<>(Arrays.asList(SQLiUtil.placeholderPattern.split(content)));
                    if (content.endsWith(split_cont_by_placeholder.get(split_cont_by_placeholder.size() - 1))) {
                        split_cont_by_placeholder.remove(split_cont_by_placeholder.size() - 1);
                    }

                    List<String> concat_cont = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    idx += 1;
                    for (String seg : split_cont_by_placeholder) {
                        sb.append(seg);
                        if (idx < args.length &&
                                SecExpressionUtils.isSqliCareExpression(args[idx]) &&
                                getLiteralOrVariableLiteral(args[idx]) == null
                        ) {
                            concat_cont.add(sb.toString());
                        }
                        sb.append(" ? ");
                        idx += 1;
                    }

                    if (hasEvalAdditive(concat_cont)) {
                        holder.registerProblem(
                                expression,
                                MESSAGE,
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                        );
                    }
                }
            }
        };
    }

    @Nullable
    private String getLiteralOrVariableLiteral(@Nullable PsiExpression expression) {
        String content = null;
        if (ExpressionUtils.isLiteral(expression)) {
            content = SecExpressionUtils.getLiteralInnerText(expression);
        } else {
            PsiLocalVariable localVariable = ExpressionUtils.resolveLocalVariable(expression);
            if (localVariable != null) {
                PsiExpression localVariableInitializer = localVariable.getInitializer();
                if (localVariableInitializer != null && ExpressionUtils.isLiteral(localVariableInitializer)) {
                    content = SecExpressionUtils.getLiteralInnerText(localVariableInitializer);
                }
            }
        }
        return content;
    }
}
