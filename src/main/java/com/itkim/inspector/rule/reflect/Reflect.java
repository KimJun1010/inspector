package com.itkim.inspector.rule.reflect;

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
 * 1046
 * java/lang/reflect/Method invoke (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object; 0 false # Method.invoke,其实需要两个参数都可控才能打 # REFLECT
 * java/net/URLClassLoader newInstance ([Ljava/net/URL;Ljava/lang/ClassLoader;)Ljava/net/URLClassLoader; -1 false # 实例化任意对象的sink # REFLECT
 * java/lang/ClassLoader loadClass * 1 true # ClassLoader加载任意Class # REFLECT
 * org/codehaus/groovy/runtime/InvokerHelper invokeMethod * 1 false # groovy-specific sinks # REFLECT
 * groovy/lang/MetaClass invokeMethod * -1 true # groovy-specific sinks # REFLECT
 * groovy/lang/MetaClass invokeConstructor * -1 true # groovy-specific sinks # REFLECT
 * groovy/lang/MetaClass invokeStaticMethod * -1 true # groovy-specific sinks # REFLECT
 */
public class Reflect extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("reflect.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.reflect.Method", "invoke")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.net.URLClassLoader", "newInstance")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.lang.ClassLoader", "loadClass")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.codehaus.groovy.runtime.InvokerHelper", "invokeMethod")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.MetaClass", "invokeMethod")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.MetaClass", "invokeConstructor")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.MetaClass", "invokeStaticMethod")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
