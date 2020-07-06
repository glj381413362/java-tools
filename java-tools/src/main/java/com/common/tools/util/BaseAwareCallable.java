/*
 *
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.tools.util;

import java.util.concurrent.Callable;

/**
 * <p>
 *
 * </p>
 *
 * @author gongliangjun 2019/12/28 7:27 PM
 */
public abstract class BaseAwareCallable<T> implements Callable<T> {


    @Override
    public T call() throws Exception {
        return performActualWork();
    }

    protected abstract T performActualWork();
}
