package com.itkim.inspector.rule.jdbc;

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
 * 1043
 * javax/sql/DataSource getConnection * -1 false # JDBC反序列化sink # JDBC
 *
 * java/sql/Driver connect (Ljava/lang/String;Ljava/util/Properties;) 1 false # JDBC # JDBC
 * java/sql/DriverManager getConnection (Ljava/lang/String;) 1 false # JDBC # JDBC
 * java/sql/DriverManager getConnection (Ljava/lang/String;Ljava/util/Properties;) 1 false # JDBC # JDBC
 * java/sql/DriverManager getConnection (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/springframework/boot/jdbc/DataSourceBuilder url (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi create (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi create (Ljava/lang/String;Ljava/util/Properties;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi create (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi open (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi open (Ljava/lang/String;Ljava/util/Properties;) 1 false # JDBC # JDBC
 * org/jdbi/v3/core/Jdbi open (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) 1 false # JDBC # JDBC
 * com/zaxxer/hikari/HikariConfig <init> (Ljava/util/Properties;) 1 false # JDBC # JDBC
 * com/zaxxer/hikari/HikariConfig setJdbcUrl (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/springframework/jdbc/datasource/AbstractDriverBasedDataSource setUrl (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/springframework/jdbc/datasource/DriverManagerDataSource <init> (Ljava/lang/String;) 1 false # JDBC # JDBC
 * org/springframework/jdbc/datasource/DriverManagerDataSource <init> (Ljava/lang/String;Ljava/util/Properties;) 1 false # JDBC # JDBC
 * org/springframework/jdbc/datasource/DriverManagerDataSource <init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;) 1 false # JDBC # JDBC
 */
public class JDBCUnserialize extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("jdbc.unserialize.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "javax.sql.DataSource", "getConnection")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.sql.Driver", "connect")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "java.sql.DriverManager", "getConnection")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.jdbc.DataSourceBuilder", "url")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.jdbi.v3.core.Jdbi", "create")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "jorg.jdbi.v3.core.Jdbi", "open")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "com.zaxxer.hikari.HikariConfig", "setJdbcUrl")
                        || SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.jdbc.datasource.AbstractDriverBasedDataSource", "setUrl")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.springframework.jdbc.datasource.DriverManagerDataSource")
                || SecExpressionUtils.hasFullQualifiedName(expression, "com.zaxxer.hikari.HikariConfig")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
