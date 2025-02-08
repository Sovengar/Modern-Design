package com.jonathan.modern_design.config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.time.LocalDate;

// To use add to your test class: @RegisterExtension TimeExtension timeExtension = new TimeExtension("2019-09-29");
public class TimeExtension implements InvocationInterceptor {
    private LocalDate fixed;

    public TimeExtension(LocalDate fixed) {
        this.fixed = fixed;
    }

    public TimeExtension(String fixed) {
        this.fixed = LocalDate.parse(fixed);
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        try (var staticMock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            staticMock.when(LocalDate::now)
                    .thenAnswer(call -> fixed);

            invocation.proceed(); // call the @Test method
        }
    }

    public void setDate(LocalDate date) {
        fixed = date;
    }

}

class TimeFactory { // ~ Clock
    public LocalDate today() {
        return LocalDate.now();
    }
}
