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
 * 1012
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljava/io/Reader;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljava/io/Reader;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/exolab/castor/xml/EventProducer;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/exolab/castor/xml/SAX2EventProducer;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/exolab/castor/types/AnyNode;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/xml/sax/InputSource;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/w3c/dom/Node;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljavax/xml/stream/XMLEventReader;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljavax/xml/stream/XMLStreamReader;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/exolab/castor/xml/SAX2EventAndErrorProducer;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljavax/xml/transform/Source;)Ljava/lang/Object; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Lorg/exolab/castor/xml/UnmarshalHandler;)Lorg/xml/sax/ContentHandler; 1 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljava/lang/Class;Ljava/io/Reader;)Ljava/lang/Object; 2 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljava/lang/Class;Lorg/xml/sax/InputSource;)Ljava/lang/Object; 2 false # castor反序列化 # RCE
 * org/exolab/castor/xml/Unmarshaller unmarshal (Ljava/lang/Class;Lorg/w3c/dom/Node;)Ljava/lang/Object; 2 false # castor反序列化 # RCE
 */
public class CastorUnserialize extends BaseLocalInspectionTool {
    public static final String MESSAGE = InspectionBundle.message("castor.unserialize.type.msg");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                if (SecExpressionUtils.hasFullQualifiedName(expression, "org.exolab.castor.xml.Unmarshaller", "unmarshal")) {
                    holder.registerProblem(
                            expression,
                            MESSAGE,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }

}
