package com.itkim.inspector.rule.dos;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import com.siyeh.ig.psiutils.MethodCallUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * 1039
 * 正则表达式拒绝服务攻击(RegexDos)
 * <p>
 * 当编写校验的正则表达式存在缺陷或者不严谨时, 攻击者可以构造特殊的字符串来大量消耗服务器的系统资源，造成服务器的服务中断或停止。
 * ref: https://cloud.tencent.com/developer/article/1041326
 * <p>
 * check:
 * java.util.regex.Pattern#compile args:0
 * java.util.regex.Pattern#matchers args:0
 * <p>
 * fix:
 * (1) optimize Regular Expressions
 * (2) use com.google.re2j
 * <p>
 * notes:
 * `isExponentialRegex` method copy from CodeQL
 */
public class PatternDOS extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("pattern.matches.type.msg");

    public static boolean isExponentialRegex(String s) {
        return
                // Example: ([a-z]+)+
                Pattern.matches(".*\\([^()*+\\]]+\\]?(\\*|\\+)\\)(\\*|\\+).*", s) ||
                        // Example: (([a-z])?([a-z]+))+
                        Pattern.matches(".*\\((\\([^()]+\\)\\?)?\\([^()*+\\]]+\\]?(\\*|\\+)\\)\\)(\\*|\\+).*", s) ||
                        // Example: (([a-z])+)+
                        Pattern.matches(".*\\(\\([^()*+\\]]+\\]?\\)(\\*|\\+)\\)(\\*|\\+).*", s) ||
                        // Example: (a|aa)+
                        Pattern.matches(".*\\(([^()*+\\]]+\\]?)\\|\\1+\\??\\)(\\*|\\+).*", s) ||
                        // Example: (.*[a-z]){n} n >= 10
                        Pattern.matches(".*\\(\\.\\*[^()*+\\]]+\\]?\\)\\{[1-9][0-9]+,?[0-9]*\\}.*", s);
    }

//    @Override
//    @NotNull
//    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
//        return new JavaElementVisitor() {
//            @Override
//            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
//                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.util.regex.Pattern", "matches")) {
//                    holder.registerProblem(
//                            expression,
//                            MESSAGE,
//                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
//                }
//            }
//        };
//    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (MethodCallUtils.isCallToRegexMethod(expression)) { // `isCallToRegexMethod` judges include java.util.regex.Pattern
                    String methodName = MethodCallUtils.getMethodName(expression);
                    if (methodName != null && (methodName.equals("compile") || methodName.equals("matches"))) {
                        PsiExpression[] expressions = expression.getArgumentList().getExpressions();
                        if (expressions.length > 0) {
                            PsiLiteralExpression literal = getLiteralExpression(expressions[0]);
                            if (literal != null && isExponentialRegex(SecExpressionUtils.getLiteralInnerText(literal))) {
                                holder.registerProblem(expressions[0], MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                            }
                        }
                    }
                }
            }
        };
    }

    @Nullable
    private PsiLiteralExpression getLiteralExpression(PsiExpression expression) {
        if (expression instanceof PsiReferenceExpression) {
            PsiElement elem = ((PsiReferenceExpression) expression).resolve();
            if (elem instanceof PsiVariable) {
                PsiExpression initializer = ((PsiVariable) elem).getInitializer();
                if (initializer instanceof PsiLiteralExpression) {
                    return (PsiLiteralExpression) initializer;
                }
            }
        } else if (expression instanceof PsiLiteralExpression) {
            return (PsiLiteralExpression) expression;
        }
        return null;
    }
}
