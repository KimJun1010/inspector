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
 * 1053
 * DES / 3DES(DESede) 为过时的加密标准
 *
 * 检查如下内容
 * javax.crypto.Cipher.getInstance
 * <li><tt>DES/CBC/NoPadding</tt> (56)</li>
 * <li><tt>DES/CBC/PKCS5Padding</tt> (56)</li>
 * <li><tt>DES/ECB/NoPadding</tt> (56)</li>
 * <li><tt>DES/ECB/PKCS5Padding</tt> (56)</li>
 * <li><tt>DESede/CBC/NoPadding</tt> (168)</li>
 * <li><tt>DESede/CBC/PKCS5Padding</tt> (168)</li>
 * <li><tt>DESede/ECB/NoPadding</tt> (168)</li>
 * <li><tt>DESede/ECB/PKCS5Padding</tt> (168)</li>
 *
 * ref:
 * https://www.nist.gov/news-events/news/2005/06/nist-withdraws-outdated-data-encryption-standard
 */
public class OutdatedEncryptionInspector extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("outdated.encryption.inspector.msg");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.crypto.Cipher", "getInstance")) {
                    PsiExpressionList argList = expression.getArgumentList();
                    PsiExpression[] args = argList.getExpressions();
                    if (args.length > 0 && args[0] instanceof PsiLiteralExpression) {
                        String trans = SecExpressionUtils.getLiteralInnerText(args[0]);
                        if (null != trans && trans.startsWith("DES")) {
                            holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        }
                    }
                }
            }
        };
    }
}
