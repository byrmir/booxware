/*
 * Copyright 2017 Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>.
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
package com.booxware.test.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.booxware.test.WrapExceptions;
import com.booxware.test.util.ExceptionFactory;
import java.util.Optional;
import java.util.StringJoiner;
import javax.validation.ConstraintViolationException;

/**
 * Used to avoid additional exception processing within business methods.
 *
 * @author Andrey Alexeenko <Andrey.Alexeenko at t-systems.com>
 */
@Aspect
@Component
public class ExceptionAspects {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
            ExceptionAspects.class);

    /**
     * Wraps every exception from annotated methods into required exception
     * bean, please define required exception bean at
     * {@link  com.booxware.test.util.ExceptionFactory}
     *
     * @param joinPoint
     * @param w
     * @param e
     *
     * @throws Throwable
     */
    @AfterThrowing(value = "@annotation(w)", throwing = "e", argNames = "w, e")
    public void wrap(JoinPoint joinPoint, WrapExceptions w, Throwable e)
            throws Throwable {
        String message = null;
        if (ConstraintViolationException.class.isInstance(e)) {
            StringJoiner sj = new StringJoiner(":", "[", "]");
            ConstraintViolationException.class.cast(e).
                    getConstraintViolations().forEach(cv -> {
                        sj.add(cv.getMessage());
                    });
            message = sj.toString();
        } else {
            message = e.getMessage();
        }
        LOGGER.debug("Registered exceptional activity.", message);
        Throwable ex = ExceptionFactory.get().build(w.type(), message);
        throw Optional.ofNullable(ex).orElse(e);
    }

}
