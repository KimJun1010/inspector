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
 * 1007
 * org/yaml/snakeyaml/Yaml load * 1 false # snakeYaml反序列化 # RCE
 * org/yaml/snakeyaml/Yaml loadAll * 1 false # snakeYaml反序列化 # RCE
 * org/yaml/snakeyaml/Yaml parse * 1 false # snakeYaml反序列化 # RCE
 * org/yaml/snakeyaml/Yaml loadAs * 1 false # snakeYaml反序列化 # RCE
 */
public class SnakeYamlUnserialize extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("snakeYaml.unserialize.type.msg");


    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.yaml.snakeyaml.Yaml","load")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.yaml.snakeyaml.Yaml","loadAll")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.yaml.snakeyaml.Yaml","parse")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.yaml.snakeyaml.Yaml","loadAs")) {
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
