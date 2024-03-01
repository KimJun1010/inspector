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
 * 1019
 * groovy/lang/GroovyClassLoader parseClass (Lgroovy/lang/GroovyCodeSource;) 1 false # groovy # RCE
 * groovy/lang/GroovyClassLoader parseClass (Lgroovy/lang/GroovyCodeSource;Z) 1 false # groovy # RCE
 * groovy/lang/GroovyClassLoader parseClass (Ljava/io/InputStream;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyClassLoader parseClass (Ljava/io/Reader;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyClassLoader parseClass (Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyClassLoader parseClass (Ljava/lang/String;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Lgroovy/lang/GroovyCodeSource;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/io/Reader;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/io/Reader;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/lang/String;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell evaluate (Ljava/net/URI;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell parse (Ljava/io/Reader;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell parse (Ljava/io/Reader;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell parse (Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell parse (Ljava/lang/String;Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell parse (Ljava/net/URI;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Lgroovy/lang/GroovyCodeSource;Ljava/util/List;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Lgroovy/lang/GroovyCodeSource;[Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/io/Reader;Ljava/lang/String;Ljava/util/List;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/io/Reader;Ljava/lang/String;[Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/lang/String;Ljava/lang/String;Ljava/util/List;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/net/URI;Ljava/util/List;) 1 false # groovy # RCE
 * groovy/lang/GroovyShell run (Ljava/net/URI;[Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/util/Eval me (Ljava/lang/String;) 1 false # groovy # RCE
 * groovy/util/Eval me (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;) 3 false # groovy # RCE
 * groovy/util/Eval x (Ljava/lang/Object;Ljava/lang/String;) 2 false # groovy # RCE
 * groovy/util/Eval xy (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;) 3 false # groovy # RCE
 * groovy/util/Eval xyz (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;) 4 false # groovy # RCE
 * org/codehaus/groovy/control/CompilationUnit compile * 0 false # groovy # RCE
 * org/codehaus/groovy/tools/javac/JavaAwareCompilationUnit compile * -1 false # groovy # RCE
 */
public class GroovyRCE extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("groovy.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.GroovyClassLoader", "parseClass")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.GroovyShell", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.GroovyShell", "parse")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.lang.GroovyShel", "run")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.util.Eval", "me")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.util.Eval", "x")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.util.Eval", "xy")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "groovy.util.Eval", "xyz")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.codehaus.groovy.control.CompilationUnit", "compile")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.codehaus.groovy.tools.javac.JavaAwareCompilationUnit", "compile")
                ) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
