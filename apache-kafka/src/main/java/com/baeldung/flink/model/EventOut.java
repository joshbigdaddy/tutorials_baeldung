package com.baeldung.flink.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventOut {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("TimeStamp")
    Date timeStamp;
    @JsonProperty("SignalName")
    String signalName;
	@JsonProperty("Value")
    Object value;
	@JsonProperty("Line")
    String line;
	@JsonProperty("Cell")
    String cell;
	@JsonProperty("Asset")
    String asset;
	    @JsonProperty("SubAsset")
    String subasset;
	    @JsonProperty("Operation")
    String operation;
	    @JsonProperty("ProcessType")
    String processType;


    public EventOut() {
    }

    public EventOut(Date timeStamp, String signalName, Object value, String line, String cell, String asset, String subasset, String operation, String processType) {
        this.timeStamp = timeStamp;
        this.signalName = signalName;
        this.value = value;
        this.line = line;
        this.cell = cell;
        this.asset = asset;
        this.subasset = subasset;
        this.operation = operation;
        this.processType = processType;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getSubasset() {
        return subasset;
    }

    public void setSubasset(String subasset) {
        this.subasset = subasset;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    @Override
    public String toString() {
        return "EventOut{" +
                "timeStamp=" + timeStamp +
                ", signalName='" + signalName + '\'' +
                ", value=" + value +
                ", line='" + line + '\'' +
                ", cell='" + cell + '\'' +
                ", asset='" + asset + '\'' +
                ", subasset='" + subasset + '\'' +
                ", operation='" + operation + '\'' +
                ", processType='" + processType + '\'' +
                '}';
    }

    public Map<String,String> treatValue(){
      String[]  valueSplitByComma=  this.value.toString().split(",");

      Map<String,String> values= new HashMap<>();
      values.put("Status",valueSplitByComma[0].substring(0,1));
      values.put("Traceability",valueSplitByComma[0].substring(1));
      values.put("DQCAsset",valueSplitByComma[1]);
      values.put("Working",valueSplitByComma[2]);
      values.put("FeatureID",valueSplitByComma[3]);
      values.put("AlarmCode",valueSplitByComma[4]);
      values.put("Value",valueSplitByComma[5]);
      values.put("Deviation",valueSplitByComma[6]);
      values.put("AlarmNr",valueSplitByComma[7]);
      return  values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventOut eventOut = (EventOut) o;
        return Objects.equals(timeStamp, eventOut.timeStamp) && Objects.equals(signalName, eventOut.signalName) && Objects.equals(value, eventOut.value) && Objects.equals(line, eventOut.line) && Objects.equals(cell, eventOut.cell) && Objects.equals(asset, eventOut.asset) && Objects.equals(subasset, eventOut.subasset) && Objects.equals(operation, eventOut.operation) && Objects.equals(processType, eventOut.processType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, signalName, value, line, cell, asset, subasset, operation, processType);
    }
}
