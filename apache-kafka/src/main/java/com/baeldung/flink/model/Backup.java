package com.baeldung.flink.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Backup {

    @JsonProperty("Description")
    String description;
    @JsonProperty("Traceability Code")
    String traceabilityCode;
    @JsonProperty("Status")
    String status;
    @JsonProperty("Working")
    String working;
    @JsonProperty("StartTime")
    String startTime;
    @JsonProperty("EndTime")
    String endTime;
    @JsonProperty("Feature")
    String feature;
    @JsonProperty("Asset")
    String asset;
    @JsonProperty("Value")
    String value;
    @JsonProperty("Limit")
    String limit;
    @JsonProperty("Deviation")
    String deviation;

    public Backup(String description, String traceabilityCode, String status, String working, String startTime, String endTime, String feature, String asset, String value, String limit, String deviation) {
        this.description = description;
        this.traceabilityCode = traceabilityCode;
        this.status = status;
        this.working = working;
        this.startTime = startTime;
        this.endTime = endTime;
        this.feature = feature;
        this.asset = asset;
        this.value = value;
        this.limit = limit;
        this.deviation = deviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTraceabilityCode() {
        return traceabilityCode;
    }

    public void setTraceabilityCode(String traceabilityCode) {
        this.traceabilityCode = traceabilityCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getDeviation() {
        return deviation;
    }

    public void setDeviation(String deviation) {
        this.deviation = deviation;
    }

    @Override
    public String toString() {
        return "Backup{" +
                "description='" + description + '\'' +
                ", traceabilityCode='" + traceabilityCode + '\'' +
                ", status='" + status + '\'' +
                ", working='" + working + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", feature='" + feature + '\'' +
                ", asset='" + asset + '\'' +
                ", value='" + value + '\'' +
                ", limit='" + limit + '\'' +
                ", deviation='" + deviation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Backup backup = (Backup) o;
        return Objects.equals(description, backup.description) && Objects.equals(traceabilityCode, backup.traceabilityCode) && Objects.equals(status, backup.status) && Objects.equals(working, backup.working) && Objects.equals(startTime, backup.startTime) && Objects.equals(endTime, backup.endTime) && Objects.equals(feature, backup.feature) && Objects.equals(asset, backup.asset) && Objects.equals(value, backup.value) && Objects.equals(limit, backup.limit) && Objects.equals(deviation, backup.deviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, traceabilityCode, status, working, startTime, endTime, feature, asset, value, limit, deviation);
    }
}
