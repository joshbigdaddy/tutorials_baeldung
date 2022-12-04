package com.baeldung.flink;

import com.baeldung.flink.model.Backup;
import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.model.EventOut;
import com.baeldung.flink.operator.BackupMap;
import com.baeldung.flink.operator.EventOutTimestampAssigner;
import com.baeldung.flink.operator.WordsCapitalizer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

import java.time.Duration;

import static com.baeldung.flink.connector.Consumers.*;
import static com.baeldung.flink.connector.Producers.*;

public class FlinkDataPipeline {

  public static void capitalize(String inputTopic) throws Exception {
    String outputTopic = "flink_output";
    String consumerGroup = "baeldung";
    String address = "localhost:9092";

    StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

    FlinkKafkaConsumer011<String> flinkKafkaConsumer =
        createStringConsumerForTopic(inputTopic, address, consumerGroup);
    flinkKafkaConsumer.setStartFromEarliest();

    DataStream<String> stringInputStream = environment.addSource(flinkKafkaConsumer);

    FlinkKafkaProducer011<String> flinkKafkaProducer = createStringProducer(outputTopic, address);

    stringInputStream.map(new WordsCapitalizer()).addSink(flinkKafkaProducer);

    environment.execute();
  }

  public static void createBackup(String inputTopic) throws Exception {
    String outputTopic = "flink_output";
    String consumerGroup = "baeldung";
    String kafkaAddress = "localhost:9092";

    StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    FlinkKafkaConsumer011<EventOut> flinkKafkaConsumer =
        createEventOutConsumer(inputTopic, kafkaAddress, consumerGroup);
    flinkKafkaConsumer.setStartFromEarliest();

    flinkKafkaConsumer.assignTimestampsAndWatermarks(new EventOutTimestampAssigner());
    FlinkKafkaProducer011<Backup> flinkKafkaProducer =
        createBackupProducer(outputTopic, kafkaAddress);
    DataStream<EventOut> EventOutsStream = environment.addSource(flinkKafkaConsumer);
    EventOutsStream.filter(ev -> ev.getSignalName().equalsIgnoreCase("DQCAlarmOut"))
        .map(new BackupMap())
        .addSink(flinkKafkaProducer);

  /*    MongoConnectorOptions options = MongoConnectorOptions.builder()
            .withDatabase("my_db")
            .withCollection("my_collection")
            .withConnectString("mongodb://user:password@127.0.0.1:27017")
            .withTransactionEnable(false)
            .withFlushOnCheckpoint(false)
            .withFlushSize(1_000L)
            .withFlushInterval(Duration.ofSeconds(10))
            .build();

         EventOutsStream.filter(ev -> ev.getSignalName().equalsIgnoreCase("DQCAlarmOut"))
        .map(new BackupMap())
        .addSink(new MongoSink<>(new StringDocumentSerializer(), options));
            */

    environment.execute();
  }

  public static void main(String[] args) throws Exception {
    createBackup(args[0]);
  }
}
