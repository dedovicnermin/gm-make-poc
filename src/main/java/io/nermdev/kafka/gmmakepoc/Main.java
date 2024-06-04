package io.nermdev.kafka.gmmakepoc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Properties;

@Slf4j
public class Main {
  static final class ConfigUtils {
    private ConfigUtils() {}

    public static void loadConfig(final String file, final Properties properties) {
      try (final FileInputStream inputStream = new FileInputStream(file)) {
        properties.load(inputStream);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        System.exit(1);
      }
    }

    public static Properties getProperties(final String[] args) {
      if (args.length < 1) throw new IllegalArgumentException("Pass path to application.properties");
      final Properties properties = new Properties();
      loadConfig(args[0], properties);
      return properties;
    }
  }

  static final Faker fake = Faker.instance();
  static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args) {
    final Properties properties = ConfigUtils.getProperties(args);
    final String topic = properties.getProperty("topic");
    final long sleepTime = Long.parseLong(properties.getProperty("sleep.time"));
    properties.remove("topic");
    properties.remove("sleep.time");
    try (final KafkaProducer<String, Tag> producer = new KafkaProducer<>(properties)) {
      while (true) {
        final ProducerRecord<String, Tag> pRecord = new ProducerRecord<>(topic, generateTag());
        log.info("Sending event : {}", mapper.writeValueAsString(pRecord.value()));
        producer.send(pRecord);
        Thread.sleep(sleepTime);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }


  }

  private static Tag generateTag() {
    final String ts = Instant.now().toString();
    final int i = fake.number().numberBetween(1, 501);
    if (i <= 400) {
      final double v = fake.number().randomDouble(4, -100L, 500L);
      final Float aFloat = Float.valueOf(String.valueOf(v));
      return new Tag(192, fake.starTrek().location(), ts, aFloat, aFloat.toString());
    } else {
      final boolean aBoolean = fake.random().nextBoolean();
      Float f = aBoolean ? 1.0f : 0.0f;
      return new Tag(192, fake.starTrek().location(), ts, f, Boolean.toString(aBoolean));
    }
  }
}