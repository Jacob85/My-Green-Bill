package com.mygreenbill.common;

/**
 * Created by Jacob on 3/18/14.
 */
public class Status
{
    private OperationStatus operationStatus;
    private String description;     //mainly for logging peruses

    public Status()
    {
    }

    public Status(OperationStatus operationStatus, String description)
    {
        this.operationStatus = operationStatus;
        this.description = description;
    }

    public OperationStatus getOperationStatus()
    {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus)
    {
        this.operationStatus = operationStatus;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public enum OperationStatus
    {
        FAILED("Failed"),
        SUCCESS("Success");

        private String status;

        OperationStatus(String status)
        {
            this.status = status;
        }

        public String status()
        {
            return this.status;
        }
    }

    @Override
    public String toString()
    {
        return "Status{" +
                "operationStatus=" + operationStatus.status() +
                ", description='" + description + '\'' +
                '}';
    }
}
