package drishti.payment.calculator.helper;

import drishti.payment.calculator.web.models.TaskPayment;

public class TaskPaymentTestBuilder {

    private final TaskPayment.TaskPaymentBuilder builder;

    public TaskPaymentTestBuilder() {
        this.builder = TaskPayment.builder();
    }

    public static TaskPaymentTestBuilder builder() {
        return new TaskPaymentTestBuilder();
    }

    public TaskPayment build() {
        return this.builder.build();
    }

    public TaskPaymentTestBuilder withConfig(String type) {

        this.builder.type(type)
                .courtfee(100.0)
                .issuanceFeeEnable(true)
                .build();
        return this;
    }
}
