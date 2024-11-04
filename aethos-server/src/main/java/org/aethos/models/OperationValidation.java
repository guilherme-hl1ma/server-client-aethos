package org.aethos.models;

public class OperationValidation extends Message {
    private final String operation;
    private Object object;

    public OperationValidation(String operation)
    {
        this.operation = operation;
    }

    public String getOperation()
    {
        return this.operation;
    }

    public Object getObject()
    {
        return this.object;
    }
}
