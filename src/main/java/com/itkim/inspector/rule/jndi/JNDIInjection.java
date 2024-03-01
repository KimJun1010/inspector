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
package com.itkim.inspector.rule.jndi;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.itkim.inspector.BaseLocalInspectionTool;
import com.itkim.inspector.fix.SetBoolArgQuickFix;
import com.itkim.inspector.utils.InspectionBundle;
import com.itkim.inspector.utils.SecExpressionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 1061: JNDI注入化风险
 */
public class JNDIInjection extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jndi.injection.msg");

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.management.remote.JMXServiceURL")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "java.rmi.registry.Registry ", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "findByDn")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "lookupContext")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.LdapOperations", "search")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "findByDn")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "lookupContext")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "search")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapOperation", "searchForObject")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.management.remote.JMXConnector", "connect")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "lookupLink")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.Context", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.InitialContext", "doLookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.InitialContext", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.InitialContext", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.InitialContext", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.InitialContext", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.directory.InitialDirContext", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.directory.InitialDirContext", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.directory.InitialDirContext", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.directory.InitialDirContext", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.ldap.InitialLdapContext", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.ldap.InitialLdapContext", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.ldap.InitialLdapContext", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.ldap.InitialLdapContext", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax.naming.ldap.InitialLdapContext", "lookupLink")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "lookup")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "lookupContext")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "findByDn")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "rename")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "list")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.ldap.core.LdapTemplate", "listBindings")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org/apache/shiro/jndi/JndiTemplate", "look")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "javax/management/remote/JMXConnectorFactory", "connect")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org/springframework/jndi/JndiTemplate", "lookup")

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
