package com.baeldung.flink.operator;

import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.model.InputMessage;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;
import java.time.ZoneId;

public class EventOutTimestampAssigner implements AssignerWithPunctuatedWatermarks<EventOut> {

    @Override
    public long extractTimestamp(EventOut element, long previousElementTimestamp) {
        ZoneId zoneId = ZoneId.systemDefault();
        return element.getTimeStamp().getTime();
    }

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(EventOut lastElement, long extractedTimestamp) {
        return new Watermark(extractedTimestamp - 15);
    }
}
