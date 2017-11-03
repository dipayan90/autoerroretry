Autoerroretry
=============


Description
------------

Handle errors on your application code and push objects that may have to be retried either because of 
intermittent connection errors, databased persistence failures or something else.

This library is a very light weight utility that currently supports Amazon SQS and Apache Kafka as
backend queuing mechanisms for retrying errors.

It supports the following :

 - Configuring your own Connection settings for Amazon SQS, Apache Kafka and File Based Queue Tape.
 - Configuring how often you would want your logic to obtain failures and do a retry of your own custom logic. 
   Default value is 10 secs if nothing is passed.
   You can also configure an initial delay in kick starting the retry.Default value for the initial delay is 0 seconds

Future Scope:

- Support for other channels apart from Amazon SQS and Apache Kafka for retries.

Requirements
------------

1. Java 8+
2. A SQS queue needs to be created on your AWS account. A queue url would be required. 
3. If Using Kafka as the underlying messaging apparatus, you would need the broker hosts information, topic name and the client group Id. 
 Also indicate if in-case order in which the objects need to be retried should be in order or not.
4. The easiest way to get up and running is using a file based queuing system Tape ( https://github.com/square/tape )
Sample Code
-------------

Package comes with a converter to convert your class to string and back to your class.

Note: Your class should implement Serializable for this to work.


```java
ObjectStringConverter converter = ObjectStringConverter.getObjectStringConverter();
```

Convert Your Class to String first:

```java
 Transaction transaction = new Transaction();
        transaction.setDate("12/27/1990");
        transaction.setTransactionId(1);
        transaction.setValue(100);
        
 String s = converter.toString(transaction);
  
 List<String> messageList = Collections.singletonList(s);
```

Publish the object that needs to be retried. FIFO based SQS Url will be required here :

```java

 client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.SQS)
                .withSqsUrl(queueUrl).build());

```
Since we may retry the same message before the 5 minute deduplication interval set by AWS,
we have se the deduplication ID such that it also processes dupes withing the 5 minute interval. 

Publish object to kafka based backend: 

```java
client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.KAFKA)
                .withKafkaRetries(0)
                .withKafkaServers(Collections.singletonList("localhost:9092"))
                .withKafkaTopic("retry")
                .withOrderGuarentee(true)
                .build());
```

Publish object to File based backend:

```java
client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.TAPE)
                .withTapeFileName("tape.txt")
                .build());
```

Send your function as a parameter that you want to be executed, in short your retry logic
Data is received in the form of String that can be easily converted back to your object using:

```java
converter.fromString(e);
```

Amazon SQS based receiver. SQSUrl will be required here:

```java

client.recieveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withSqsUrl(queueUrl)
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { System.out.println(converter.fromString(e)); });
            return null;
        });

```

Apache kafka based receiver: 

```java
client.receiveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withMessageBroker(MessageBroker.KAFKA)
                .withKafkaServers(Collections.singletonList("localhost:9092"))
                .withKafkaConsumerGroupId("localConsumer")
                .withKafkaTopicName("retry")
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { value.addAndGet(1);
                try {
                    System.out.println( "  Message:  " + converter.fromString(e).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
```
File Based receiver:

```java
 client.receiveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withMessageBroker(MessageBroker.TAPE)
                .withtapeFileName("tape.txt")
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { value.addAndGet(1);
                try {
                    System.out.println( "  Message:  " + converter.fromString(e).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
```

Build Library
------------
Best place to test the library will be to run:

```java
ApplicationTest.java
```

There are 3 tests, one for SQS based backend, Kafka based backend and a Simple File based backend.

To build the package:

```mvn clean install ```

Get Library
------------
```xml
<dependency>
  <groupId>com.nordstrom.ds</groupId>
  <artifactId>autoerroretry</artifactId>
  <version>1.5</version>
  <type>pom</type>
</dependency> 
```