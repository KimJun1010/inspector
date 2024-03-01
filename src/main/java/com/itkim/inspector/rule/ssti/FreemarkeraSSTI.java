package com.itkim.inspector.rule.ssti;

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
 * 1035
 * freemarker/cache/StringTemplateLoader putTemplate * 2 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/io/Reader;) 2 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/io/Reader;Lfreemarker/template/Configuration;) 2 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/io/Reader;Lfreemarker/template/Configuration;Ljava/lang/String;) 2 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/lang/String;Lfreemarker/template/Configuration;) 2 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/lang/String;Ljava/io/Reader;Lfreemarker/template/Configuration;) 3 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/lang/String;Ljava/io/Reader;Lfreemarker/template/Configuration;LParserConfiguration;Ljava/lang/String;) 3 all # Freemarker # SSTI
 * freemarker/template/Template <init> (Ljava/lang/String;Ljava/lang/String;Ljava/io/Reader;Lfreemarker/template/Configuration;Ljava/lang/String;) 3 all # Freemarker # SSTI
 */
public class FreemarkeraSSTI extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("freemarker.ssti.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "freemarker.cache.StringTemplateLoader", "putTemplate")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "freemarker.template.Template")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
