package com.itkim.inspector.rule.fileWrite;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.utils.InspectionBundle;
import org.jetbrains.annotations.NotNull;

//org.apache.commons.fileupload.FileItem#write
public class Commons extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("commons.file.write.type.msg");

//    @Override
//    @NotNull
//    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
//        return new JavaElementVisitor() {
//            @Override
//            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
//                PsiMethod method = expression.resolveMethod();
//                if (method != null) {
//                    PsiClass containingClass = method.getContainingClass();
//                    if (containingClass != null) {
//                        String methodName = method.getName();
//                        if ("write".equals(methodName) && isFileItemImplementation(containingClass)) {
//                            holder.registerProblem(expression,
//                                    MESSAGE,
//                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
//                        }
//                    }
//                }
//            }
//        };
//    }

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                // 获取调用的方法
                PsiMethod method = expression.resolveMethod();
                if (method != null) {
                    String methodName = method.getName();
                    PsiClass containingClass = method.getContainingClass();

                    // 检查是否是 FileItem 接口调用了 write() 方法
                    if ("write".equals(methodName) && isFileItemInstance(expression)) {
                        holder.registerProblem(expression,
                                MESSAGE,
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                    }

                    // 获取方法的所属类
                    if (containingClass != null) {
                        methodName = method.getName();

                        // 检查是否是 FileItem 的实现类调用了 write() 方法
                        if ("write".equals(methodName) && isFileItemInstance(expression.getMethodExpression())) {
                            holder.registerProblem(expression, methodName,
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        }
                    }
                }
            }

            /**
             * 检查调用者是否是 FileItem 接口
             */
            private boolean isFileItemInstance(PsiMethodCallExpression expression) {
                PsiExpression qualifierExpression = expression.getMethodExpression().getQualifierExpression();
                if (qualifierExpression != null) {
                    PsiType type = qualifierExpression.getType();
                    // 检查调用者的类型是否为 FileItem 接口
                    return type != null && type.equalsToText("org.apache.commons.fileupload.FileItem");
                }
                return false;
            }

            /**
             * 检查类是否是 FileItem 的实现类
             */
            private boolean isFileItemImplementation(PsiClass psiClass) {
                if (psiClass == null) {
                    return false;
                }
                // 检查是否实现了 FileItem 接口
                PsiClassType[] interfaces = psiClass.getImplementsListTypes();
                for (PsiClassType interfaceType : interfaces) {
                    PsiClass resolvedInterface = interfaceType.resolve();
                    if (resolvedInterface != null && "org.apache.commons.fileupload.FileItem".equals(resolvedInterface.getQualifiedName())) {
                        return true;
                    }
                }
                return false;
            }

            /**
             * 检查调用对象是否为 FileItem 的实例或实现类
             */
            private boolean isFileItemInstance(PsiElement element) {
                // 确保 element 是 PsiMethodCallExpression 类型
                if (!(element instanceof PsiMethodCallExpression)) {
                    return false;
                }

                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) element;
                PsiExpression qualifierExpression = methodCallExpression.getMethodExpression().getQualifierExpression();

                if (qualifierExpression != null) {
                    PsiType type = qualifierExpression.getType();

                    // 检查调用对象的类型是否为 FileItem 接口
                    if (type != null && "org.apache.commons.fileupload.FileItem".equals(type.getCanonicalText())) {
                        return true;
                    }

                    // 检查是否为 FileItem 的子类
                    if (type != null && type instanceof PsiClassType) {
                        PsiClass psiClass = ((PsiClassType) type).resolve();
                        if (psiClass != null && isFileItemImplementation(psiClass)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }
}
