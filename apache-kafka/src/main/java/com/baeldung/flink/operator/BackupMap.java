package com.baeldung.flink.operator;

import com.baeldung.flink.model.Backup;
import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.model.InputMessage;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackupMap implements MapFunction<EventOut, Backup > {
    @Override
    public Backup map(EventOut eventOut) throws Exception {
        Map<String,String> values= eventOut.treatValue();
        return new Backup(
                 "Process ALarm",
                values.get("Traceability"),
                values.get("Status"),
                values.get("Working"),
                eventOut.getTimeStamp().toString(),
                eventOut.getTimeStamp().toString(),
                values.get("FeatureID"),
                values.get("DQCAsset"),
                eventOut.getValue().toString().trim(),
                values.get("Deviation"),
                 ""
                
        );
    }
}
