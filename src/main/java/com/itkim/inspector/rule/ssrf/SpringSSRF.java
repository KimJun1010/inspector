package com.itkim.inspector.rule.ssrf;

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
 * 1068
 * org/springframework/web/reactive/function/client/WebClient create (Ljava/lang/String;)Lorg/springframework/web/reactive/function/client/WebClient; 1 true # spring-webflux ssrf # SSRF
 * org/springframework/web/reactive/function/client/WebClient baseUrl (Ljava/lang/String;)Lorg/springframework/web/reactive/function/client/WebClient$Builder; 1 true # spring-webflux ssrf # SSRF
 * org/springframework/web/client/RestTemplate getForEntity (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate exchange (Ljava/lang/String;Lorg/springframework/http/HttpMethod;Lorg/springframework/http/HttpEntity;Ljava/lang/Class;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate execute (Ljava/lang/String;Lorg/springframework/http/HttpMethod;Lorg/springframework/web/client/RequestCallback;Lorg/springframework/web/client/ResponseExtractor;[Ljava/lang/Object;)Ljava/lang/Object; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate getForObject (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate postForEntity (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Class;[Ljava/lang/Object;)Lorg/springframework/http/ResponseEntity; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate postForObject (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate put (Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)V 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate headForHeaders (Ljava/lang/String;[Ljava/lang/Object;)Lorg/springframework/http/HttpHeaders; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate optionsForAllow (Ljava/lang/String;[Ljava/lang/Object;)Ljava/util/Set; 1 false # spring ssrf # SSRF
 * org/springframework/web/client/RestTemplate delete (Ljava/lang/String;[Ljava/lang/Object;)V 1 false # spring ssrf # SSRF
 */
public class SpringSSRF extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("spring.ssrf.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.reactive.function.client.WebClient", "create")
                || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.reactive.function.client.WebClient", "baseUrl")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "getForEntity")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "exchange")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "execute")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "getForObject")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "postForEntity")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "postForObject")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "put")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "headForHeaders")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "optionsForAllow")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.web.client.RestTemplate", "delete")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
