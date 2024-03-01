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
 * 1008
 * org/ho/yaml/Yaml load * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/Yaml loadStream * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/Yaml loadStreamOfType * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/Yaml loadType * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/YamlConfig load * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/YamlConfig loadStream * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/YamlConfig loadStreamOfType * 1 false # jyaml反序列化 # RCE
 * org/ho/yaml/YamlConfig loadType * 1 false # jyaml反序列化 # RCE
 */
public class JYamlUnserialize extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jYaml.unserialize.type.msg");


    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.Yaml", "load")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.Yaml", "loadStream")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.Yaml", "loadStreamOfType")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.Yaml", "loadType")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.YamlConfig", "load")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.YamlConfig", "loadStream")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.YamlConfig", "loadStreamOfType")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.ho.yaml.YamlConfig", "loadType"))
                    holder.registerProblem(expression, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };
    }
}
