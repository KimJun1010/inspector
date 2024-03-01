/*
 * Copyright 2020 momosecurity.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itkim.inspector.rule.rce;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.jvm.annotation.JvmAnnotationEnumFieldValue;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.itkim.inspector.BaseFixElementWalkingVisitor;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.fix.DeleteElementQuickFix;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 1050: Jackson反序列化风险
 *
 * com.fasterxml.jackson.core:jackson-databind 在开启DefaultTyping时，存在反序列化风险
 *
 * 开启方式
 * (1) ObjectMapper.enableDefaultTyping();  enableDefaultTyping空参数与有参数方法，均受影响
 * (2) Annotation: @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
 * (3) Annotation: @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
 */
public class JacksonDatabindDefaultTyping extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jackson.databind.default.typing.msg");
    private static final String DEFAULT_TYPING_FIX_NAME = InspectionBundle.message("jackson.databind.default.typing.default.fix");
    private static final String ANNOTATION_FIX_NAME = InspectionBundle.message("jackson.databind.default.typing.annotation.fix");

    private final AnnotationQuickFix annotationQuickFix = new AnnotationQuickFix();

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "com.fasterxml.jackson.databind.ObjectMapper", "enableDefaultTyping")) {
                    PsiExpression varExp = expression.getMethodExpression().getQualifierExpression();
                    if (varExp != null &&  varExp.getReference() != null) {
                        PsiElement var = varExp.getReference().resolve();
                        if (var != null) {
                            UseToJackson2JsonRedisSerializerVisitor visitor = new UseToJackson2JsonRedisSerializerVisitor(var);
                            if (checkVariableUseFix(var, null, visitor)) {
                                return ;
                            }
                        }
                    }

                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            new DeleteElementQuickFix(expression, DEFAULT_TYPING_FIX_NAME)
                    );
                }
            }

            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                if ("com.fasterxml.jackson.annotation.JsonTypeInfo".equals(annotation.getQualifiedName())) {
                    PsiAnnotationParameterList psiAnnotationParameterList = annotation.getParameterList();
                    PsiNameValuePair[] nameValuePairs = psiAnnotationParameterList.getAttributes();
                    for (PsiNameValuePair nameValuePair : nameValuePairs) {
                        if ("use".equals(nameValuePair.getAttributeName()) &&
                            nameValuePair.getAttributeValue() instanceof JvmAnnotationEnumFieldValue
                        ) {
                            JvmAnnotationEnumFieldValue annotationValue = (JvmAnnotationEnumFieldValue)nameValuePair.getAttributeValue();
                            if ("com.fasterxml.jackson.annotation.JsonTypeInfo.Id".equals(annotationValue.getContainingClassName()) &&
                                ("CLASS".equals(annotationValue.getFieldName()) || "MINIMAL_CLASS".equals(annotationValue.getFieldName()))
                            ) {
                                holder.registerProblem(nameValuePair, MESSAGE, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, annotationQuickFix);
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static class UseToJackson2JsonRedisSerializerVisitor extends BaseFixElementWalkingVisitor {
        private final PsiElement refVar;

        public UseToJackson2JsonRedisSerializerVisitor(PsiElement elem) {
            this.refVar = elem;
        }

        @Override
        public void visitElement(PsiElement element) {
            if (element instanceof PsiMethodCallExpression) {
                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression)element;
                if (SecExpressionUtils.hasFullQualifiedName(methodCallExpression, "org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer", "setObjectMapper")) {
                    PsiExpressionList args = methodCallExpression.getArgumentList();
                    if (args.getExpressionCount() != 1) { return ; }
                    if (args.getExpressions()[0] instanceof PsiReferenceExpression) {
                        PsiReference refElem = args.getExpressions()[0].getReference();
                        if (refElem != null && refVar.isEquivalentTo(refElem.resolve())) {
                            this.setFix(true);
                            this.stopWalking();
                            return  ;
                        }
                    }
                }
            }
            super.visitElement(element);
        }
    }

    public static class AnnotationQuickFix implements LocalQuickFix {

        @Override
        public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getFamilyName() {
            return ANNOTATION_FIX_NAME;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            // getPsiElement() must return PsiNameValuePair
            PsiNameValuePair nameValuePair = (PsiNameValuePair)descriptor.getPsiElement();
            PsiAnnotationMemberValue member = nameValuePair.getValue();
            if (member != null) {
                String[] splitText = member.getText().split("\\.");
                if (splitText.length > 1 &&
                    (splitText[splitText.length-1].equals("CLASS") || splitText[splitText.length-1].equals("MINIMAL_CLASS"))
                ) {
                    splitText[splitText.length-1] = "NAME";
                }

                PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
                member.replace(factory.createExpressionFromText(String.join(".", splitText), null));
            }
        }
    }
}
