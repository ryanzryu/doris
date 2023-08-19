// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.rules.expression.rules;

import org.apache.doris.nereids.rules.expression.AbstractExpressionRewriteRule;
import org.apache.doris.nereids.rules.expression.ExpressionRewriteContext;
import org.apache.doris.nereids.trees.expressions.CaseWhen;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.WhenClause;
import org.apache.doris.nereids.trees.expressions.functions.scalar.If;
import org.apache.doris.nereids.trees.expressions.literal.NullLiteral;
import org.apache.doris.qe.ConnectContext;

/**
 * Rewrite rule to convert CASE WHEN to IF.
 * For example:
 * CASE WHEN a > 1 THEN 1 ELSE 0 END -> IF(a > 1, 1, 0)
 */
public class CaseWhenToIf extends AbstractExpressionRewriteRule {

    public static CaseWhenToIf INSTANCE = new CaseWhenToIf();

    @Override
    public Expression visitCaseWhen(CaseWhen caseWhen, ExpressionRewriteContext context) {
        Expression expr = caseWhen;
        if (ConnectContext.get() == null || ConnectContext.get().getSessionVariable() == null
                || !ConnectContext.get().getSessionVariable().isEnableCaseWhenTransformation()) {
            return expr;
        }
        if (caseWhen.getWhenClauses().size() == 1) {
            WhenClause whenClause = caseWhen.getWhenClauses().get(0);
            Expression operand = whenClause.getOperand();
            Expression result = whenClause.getResult();
            expr = new If(operand, result, caseWhen.getDefaultValue().orElse(new NullLiteral(result.getDataType())));
        }
        // TODO: traverse expr in CASE WHEN / If.
        return expr;
    }
}
