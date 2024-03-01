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
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiPolyadicExpression;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SQLiUtil;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1048: 多项式拼接型SQL注入漏洞
 * <p>
 * eg.
 * (1) "select *" + " from table" + " where id =" + id;
 * (2) "select * from table" + " where id = " + getUserId();
 */
public class PolyadicExpressionSQLi extends BaseSQLi {
    public static final String MESSAGE = InspectionBundle.message("polyadic.expression.sqli.msg");
//    private static final String QUICK_FIX_NAME = InspectionBundle.message("polyadic.expression.sqli.fix");

//    private final ShowHelpCommentQuickFix showHelpCommentQuickFix = new ShowHelpCommentQuickFix(QUICK_FIX_NAME, SQL_INJECTION_HELP_COMMENT);

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitPolyadicExpression(PsiPolyadicExpression expression) {
                List<PsiExpression> exps = SecExpressionUtils.deconPolyadicExpression(expression);
                if (exps.isEmpty() || ignoreMethodName(expression)) {
                    return;
                }

                String expStr = exps.stream()
                        .map(item -> SecExpressionUtils.getText(item, true))
                        .collect(Collectors.joining());
                if (isSql(expStr)) {
                    List<String> sql_segments = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();

                    boolean hasVar = false;
                    for (PsiExpression exp : exps) {
                        if (SecExpressionUtils.isSqliCareExpression(exp)) {
                            String s = SecExpressionUtils.getLiteralInnerText(exp);
                            if (s == null) {
                                if (!sb.toString().isEmpty()) {
                                    sql_segments.add(sb.toString());
                                }

                                if (!SecExpressionUtils.isText(exp)) {
                                    hasVar = true;
                                }

                                sb.append(" ? ");
                            } else {
                                sb.append(s);
                            }
                        } else {
                            sb.append(" ? ");
                        }
                    }
                    // 末段要 drop 掉，不用查

                    if (sql_segments.isEmpty() || Boolean.FALSE.equals(hasVar) || !hasEvalAdditive(sql_segments)) {
                        // 对于 "select * from " + getTable() + " where id = %s" 的情况
                        // getTable() 被忽略了，要考虑后面 %s 的问题
                        if (hasPlaceholderProblem(expStr)) {
                            holder.registerProblem(expression, PlaceholderStringSQLi.MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        }
                        return;
                    }
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            private boolean hasPlaceholderProblem(String content) {
                return SQLiUtil.placeholderPattern.matcher(content).find() &&
                        isSql(content) &&
                        hasEvalAdditive(content, SQLiUtil.placeholderPattern);
            }
        };
    }
}
