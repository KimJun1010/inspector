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
 * 1021
 * org/apache/commons/jexl3/Expression callable * 0 false # jexl # RCE
 * org/apache/commons/jexl3/Expression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JexlEngine getProperty (Lorg/apache/commons/jexl2/JexlContext;Ljava/lang/Object;Ljava/lang/String;) 3 false # jexl # RCE
 * org/apache/commons/jexl3/JexlEngine getProperty (Ljava/lang/Object;Ljava/lang/String;) 2 false # jexl # RCE
 * org/apache/commons/jexl3/JexlEngine setProperty (Lorg/apache/commons/jexl2/JexlContext;Ljava/lang/Object;Ljava/lang/String;) 3 false # jexl # RCE
 * org/apache/commons/jexl3/JexlEngine setProperty (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;) 2 false # jexl # RCE
 * org/apache/commons/jexl3/JexlExpression callable * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JexlExpression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JexlScript callable * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JexlScript execute * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JxltEngine$Expression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JxltEngine$Expression prepare * 0 false # jexl # RCE
 * org/apache/commons/jexl3/JxltEngine$Template evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl3/Script callable * 0 false # jexl # RCE
 * org/apache/commons/jexl3/Script execute * 0 false # jexl # RCE
 * org/apache/commons/jexl2/Expression callable * 0 false # jexl # RCE
 * org/apache/commons/jexl2/Expression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl2/JexlEngine getProperty (Lorg/apache/commons/jexl2/JexlContext;Ljava/lang/Object;Ljava/lang/String;) 3 false # jexl # RCE
 * org/apache/commons/jexl2/JexlEngine getProperty (Ljava/lang/Object;Ljava/lang/String;) 2 false # jexl # RCE
 * org/apache/commons/jexl2/JexlEngine setProperty (Lorg/apache/commons/jexl2/JexlContext;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;) 3 false # jexl # RCE
 * org/apache/commons/jexl2/JexlEngine setProperty (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;) 2 false # jexl # RCE
 * org/apache/commons/jexl2/JexlExpression callable * 0 false # jexl # RCE
 * org/apache/commons/jexl2/JexlExpression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl2/JexlScript callable * 0 false # jexl # RCE
 * org/apache/commons/jexl2/JexlScript execute * 0 false # jexl # RCE
 * org/apache/commons/jexl2/Script callable * 0 false # jexl # RCE
 * org/apache/commons/jexl2/Script execute * 0 false # jexl # RCE
 * org/apache/commons/jexl2/UnifiedJEXL$Expression evaluate * 0 false # jexl # RCE
 * org/apache/commons/jexl2/UnifiedJEXL$Expression prepare * 0 false # jexl # RCE
 * org/apache/commons/jexl2/UnifiedJEXL$Template evaluate * 0 false # jexl # RCE
 */
public class JEXLRCE extends BaseLocalInspectionTool {

    public static final String MESSAGE = InspectionBundle.message("jexl.rce.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.Expression", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.Expression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlEngine", "getProperty")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlEngine", "setProperty")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlExpression", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlExpression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlScript", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JexlScript", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JxltEngine$Expression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JxltEngine$Expression", "prepare")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.JxltEngine$Template", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.Script", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl3.Script", "execute")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.Expression", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.Expression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlEngine", "getProperty")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlEngine", "setProperty")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlExpression", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlExpression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlScript", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.JexlScript", "execute")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.Script", "callable")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.Script", "execute")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.UnifiedJEXL$Expression", "evaluate")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.UnifiedJEXL$Expression", "prepare")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.jexl2.UnifiedJEXL$Template", "evaluate")
                )
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };
    }
}
