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
package com.itkim.inspector.rule.rce;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.fix.DeleteElementQuickFix;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1001: Fastjson反序列化风险
 *
 * com.alibaba:fastjson 在开启AutoTypeSupport时，存在反序列化风险
 *
 * 程序内开启方法
 * (1) ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
 * (2) parseConfLocalVar.setAutoTypeSupport(true);
 * JVM开启方法
 * -Dfastjson.parser.autoTypeSupport=true
 */
public class FastjsonAutoType extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("fastjson.auto.type.msg");
    private static final String QUICK_FIX_NAME = InspectionBundle.message("fastjson.auto.type.fix");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.alibaba.fastjson.parser.ParserConfig", "setAutoTypeSupport")) {
                    PsiExpression[] args = expression.getArgumentList().getExpressions();
                    if (args.length == 1 &&
                        args[0] instanceof PsiLiteralExpression &&
                        Boolean.TRUE.equals(((PsiLiteralExpression)args[0]).getValue())
                    ) {
                        holder.registerProblem(
                                expression,
                                MESSAGE,
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                new DeleteElementQuickFix(expression, QUICK_FIX_NAME)
                        );
                    }
                }
            }
        };
    }

}
