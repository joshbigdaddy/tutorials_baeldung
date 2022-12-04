package com.baeldung.flink;

import com.baeldung.flink.model.Backup;
import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.operator.BackupMap;
import com.baeldung.flink.schema.BackupSerializationSchema;
import com.baeldung.flink.schema.EventOutDeserializationSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.collections.ListUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BackupCreatorIntegrationTest {
    public static ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    public void givenProperJson_whenDeserializeIsInvoked_thenProperObjectIsReturned() throws IOException {
        EventOut message = new EventOut(new Date(), "name", "59990120101123320,WR01,1,8100,1,34,120,01011", "line", "cell", "asset", "subasset", "operation", "processType");
        byte[] messageSerialized = mapper.writeValueAsBytes(message);
        DeserializationSchema<EventOut> deserializationSchema = new EventOutDeserializationSchema();
        EventOut messageDeserialized = deserializationSchema.deserialize(messageSerialized);

        assertEquals(message, messageDeserialized);
    }

    @Test
    public void givenMultipleEventOutsFromSource_Only_DQCAlarmOut_Signal_Name_Are_Passing() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        EventOut event1 = new EventOut(new Date(), "DQCAlarmOut", "29990120101123320,WR02,1,8102,2,34,120,01011", "line2", "cell2", "asset", "subasset", "operation", "processType");
        EventOut event2 = new EventOut(new Date(), "name3", "39990120101123320,WR03,1,8103,3,34,120,01011", "line3", "cell3", "asset", "subasset", "operation", "processType");
        EventOut event3 = new EventOut(new Date(), "DQCAlarmOut", "29990120101123320,WR02,1,8102,2,34,120,01011", "line2", "cell2", "asset", "subasset", "operation", "processType");

        List<EventOut> eventOuts = Arrays.asList(event1,event2,event3);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        DataStreamSource<EventOut> testDataSet = env.fromCollection(eventOuts);
        CollectingSink sink = new CollectingSink();
        System.out.println(testDataSet.filter(ev -> ev.getSignalName().equalsIgnoreCase("DQCAlarmOut")).print());
        testDataSet.filter(ev -> ev.getSignalName().equalsIgnoreCase("DQCAlarmOut")).map(new BackupMap()).addSink(sink);

        env.execute();
        System.out.println(sink.backups);
        //6 from previous test and 2 for this one
        assertEquals(8, sink.backups.size());
        //Filtering is being done properly
        assertFalse(sink.backups.contains(event2));
    }
    @Test
    public void givenMultipleEventOutsFromSources_All_Are_Mapped_Correcly() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        EventOut event1 = new EventOut(new Date(), "name2", "29990120101123320,WR02,1,8102,2,34,120,01011", "line2", "cell2", "asset", "subasset", "operation", "processType");
        EventOut event2 = new EventOut(new Date(), "name3", "39990120101123320,WR03,1,8103,3,34,120,01011", "line3", "cell3", "asset", "subasset", "operation", "processType");
        EventOut event3 = new EventOut(new Date(), "name1", "19990120101123320,WR01,1,8101,1,34,120,01011", "line1", "cell1", "asset", "subasset", "operation", "processType");
        EventOut event4 = new EventOut(new Date(), "name4", "49990120101123320,WR04,1,8104,4,34,120,01011", "line4", "cell4", "asset", "subasset", "operation", "processType");
        EventOut event5 = new EventOut(new Date(), "name5", "59990120101123320,WR05,1,8105,5,34,120,01011", "line5", "cell5", "asset", "subasset", "operation", "processType");
        EventOut event6 = new EventOut(new Date(), "name6", "69990120101123320,WR06,1,8106,6,34,120,01011", "line6", "cell6", "asset", "subasset", "operation", "processType");

        List<EventOut> firstBackupMessages = Arrays.asList(event1, event2, event3, event4);
        List<EventOut> secondBackupMessages = Arrays.asList(event5, event6);
        List<EventOut> EventOuts = ListUtils.union(firstBackupMessages, secondBackupMessages);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        DataStreamSource<EventOut> testDataSet = env.fromCollection(EventOuts);
        CollectingSink sink = new CollectingSink();
        testDataSet.map(new BackupMap()).addSink(sink);

        env.execute();

        Awaitility.await().until(() ->  sink.backups.size() == 6);
        assertEquals(6, sink.backups.size());
        System.out.println(sink.backups);
        BackupMap backupMap= new BackupMap();
        //We check that everything is insided
        Backup backupEvent1=backupMap.map(event1);
        Backup backupEvent2=backupMap.map(event2);
        Backup backupEvent3=backupMap.map(event3);
        Backup backupEvent4=backupMap.map(event4);
        Backup backupEvent5=backupMap.map(event5);
        Backup backupEvent6=backupMap.map(event6);

        assertTrue(sink.backups.contains(backupEvent1));
        assertTrue(sink.backups.contains(backupEvent2));
        assertTrue(sink.backups.contains(backupEvent3));
        assertTrue(sink.backups.contains(backupEvent4));
        assertTrue(sink.backups.contains(backupEvent5));
        assertTrue(sink.backups.contains(backupEvent6));

    }
    private static class CollectingSink implements SinkFunction<Backup> {
        
        public static List<Backup> backups = new ArrayList<>();

        @Override
        public synchronized void invoke(Backup value, Context context) throws Exception {
            backups.add(value);
        }
    }
}
