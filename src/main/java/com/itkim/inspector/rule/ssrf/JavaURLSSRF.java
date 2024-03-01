package com.itkim.inspector.rule.ssrf;

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
 * 1064
 * java/net/URL <init> (Ljava/lang/String;)V 1 false # SSRF -- openConnection方法触发、openStream方法触发 # SSRF
 * java/net/Socket <init> (Ljava/lang/String;I)V 1 false # SSRF # SSRF
 * java/net/URI create (Ljava/lang/String;)Ljava/net/URI; 0 false # SSRF # SSRF
 * java/net/URL openStream ()Ljava/io/InputStream; 0 false # SSRF文件读取的sink # FILE
 * java/net/http/HttpRequest newBuilder (Ljava/net/URL;Ljava/nio/charset/Charset;) 1 false # HttpClient.newHttpClient();传入send方法 # SSRF
 * javax/ws/rs/client/Client target * 1 false # jakarta ssrf -- ClientBuilder/newClient();client/target(url); # SSRF
 */
public class JavaURLSSRF extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("java.url.ssrf.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.net.URI", "create")
                || SecExpressionUtils.hasFullQualifiedName(expression, "java.net.URI", "openStream")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.net.http.HttpRequest", "newBuilder")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.ws.rs.client.Client", "target")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.net.URL")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.net.Socket"))
                holder.registerProblem(
                        expression,
                        MESSAGE,
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };


    }
}
