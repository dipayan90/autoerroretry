Autoerroretry
=============


Description
------------

Handle errors on your application code and push objects that may have to be retried either because of 
intermittent connection errors, databased persistence failures or something else.

This library is a very light weight utility that currently uses Amazon SQS for the retries.

It supports the following :

 - Configuring your own Amazon SQS endpoint url.
 - Configuring how often you would want your logic to obtain failures and do a retry of your own custom logic. 
   Default value is 10 secs if nothing is passed.

Future Scope:

- Support for other channels apart from Amazon SQS for retries.

Requirements
------------

1. Java 8+
2. A SQS queue needs to be created on your AWS account. A queue url would be required. 

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

Publish the object that needs to be retried. SQS Url will be required here :

```java

 client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withSqsUrl(queueUrl).build());

```

Send your function as a parameter that you want to be executed, in short your retry logic
Data is received in the form of String that can be easily converted back to your object using:

```java
converter.fromString(e);
```

SQS Url will be required here:

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

Build Library
------------
If you want to run the tests as part of the package you will need a valid SQS Url
which you will have to add at:

```java
ApplicationTest.java
```

To build without running tests:

```mvn clean install -D skipTests=true ```

Get Library
------------

To be added soon. 