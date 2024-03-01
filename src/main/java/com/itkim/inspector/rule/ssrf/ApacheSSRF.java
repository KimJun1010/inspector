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
 * 1063
 * org/apache/commons/httpclient/methods/GetMethod <init> (Ljava/lang/String;)V 1 false # SSRF # SSRF
 * org/apache/commons/httpclient/methods/PostMethod <init> (Ljava/lang/String;)V 1 false # SSRF # SSRF
 * org/apache/http/client/fluent/Request Get (Ljava/lang/String;)Lorg/apache/http/client/fluent/Request; 0 false # SSRF # SSRF
 * org/apache/http/client/fluent/Request Post (Ljava/lang/String;)Lorg/apache/http/client/fluent/Request; 0 false # SSRF # SSRF
 * <p>
 * org/apache/http/client/methods/HttpGet <init> (Ljava/lang/String;)V 1 false # SSRF # SSRF
 * org/apache/http/client/methods/HttpRequestBase setURI (Ljava/net/URI;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpHead <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpPost <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpPut <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpDelete <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpOptions <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpTrace <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/HttpPatch <init> (Ljava/lang/String;)V 1 false # Apache ssrf # SSRF
 * <p>
 * org/apache/http/message/BasicHttpRequest <init> (Lorg/apache/http/RequestLine;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/message/BasicHttpRequest <init> (Ljava/lang/String;Ljava/lang/String;Lorg/apache/http/ProtocolVersion;)V 2 false # Apache ssrf # SSRF
 * org/apache/http/message/BasicHttpRequest <init> (Ljava/lang/String;Ljava/lang/String;)V 2 false # Apache ssrf # SSRF
 * <p>
 * org/apache/http/message/BasicHttpEntityEnclosingRequest <init> (Lorg/apache/http/RequestLine;)V 1 false # Apache ssrf # SSRF
 * org/apache/http/message/BasicHttpEntityEnclosingRequest <init> (Ljava/lang/String;Ljava/lang/String;Lorg/apache/http/ProtocolVersion;)V 2 false # Apache ssrf # SSRF
 * org/apache/http/message/BasicHttpEntityEnclosingRequest <init> (Ljava/lang/String;Ljava/lang/String;)V 2 false # Apache ssrf # SSRF
 * <p>
 * org/apache/http/client/methods/RequestBuilder get * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder post * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder put * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder delete * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder options * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder head * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder trace * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder patch * 1 false # Apache ssrf # SSRF
 * org/apache/http/client/methods/RequestBuilder setUri * 1 false # Apache ssrf # SSRF
 */
public class ApacheSSRF extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("apache.ssrf.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.fluent.Request", "Get")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.fluent.Request", "Post")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpRequestBase", "setURI")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "get")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "post")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "put")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "delete")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "options")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "head")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "trace")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.RequestBuilder", "patch")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }


            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.httpclient.methods.GetMethod")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.commons.httpclient.methods.PostMethod")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpGet")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpHead")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpPost")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpPut")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpDelete")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpOptions")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpTrace")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.client.methods.HttpPatch")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.message.BasicHttpRequest")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.apache.http.message.BasicHttpEntityEnclosingRequest"))
                holder.registerProblem(
                        expression,
                        MESSAGE,
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
        };
    }
}
