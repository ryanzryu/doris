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

package org.apache.doris.nereids.trees.expressions.functions.scalar;

import org.apache.doris.catalog.FunctionSignature;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.functions.AlwaysNotNullable;
import org.apache.doris.nereids.trees.expressions.functions.CustomSignature;
import org.apache.doris.nereids.trees.expressions.visitor.ExpressionVisitor;
import org.apache.doris.nereids.types.BooleanType;
import org.apache.doris.nereids.util.ExpressionUtils;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * accept any arguments, return false.
 */
public class Ignore extends ScalarFunction implements CustomSignature, AlwaysNotNullable {
    /**
     * constructor with 0 argument.
     */
    public Ignore() {
        super("ignore");
    }

    /**
     * constructor with 1 or more arguments.
     */
    public Ignore(Expression arg, Expression... varArgs) {
        super("ignore", ExpressionUtils.mergeArguments(arg, varArgs));
    }

    @Override
    public FunctionSignature customSignature() {
        return FunctionSignature.of(BooleanType.INSTANCE, true, getArgumentsTypes());
    }

    /**
     * withChildren.
     */
    @Override
    public Ignore withChildren(List<Expression> children) {
        Preconditions.checkArgument(children.size() >= 1);
        return new Ignore(children.get(0),
                children.subList(1, children.size()).toArray(new Expression[0]));
    }

    @Override
    public <R, C> R accept(ExpressionVisitor<R, C> visitor, C context) {
        return visitor.visitIgnore(this, context);
    }
}
