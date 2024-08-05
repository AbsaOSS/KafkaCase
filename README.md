# KafkaCase
Adapter for communicating from Scala with Kafka via case classes

<!-- toc -->
- [Architecture](#architecture)
    - [Models](#models)
    - [Reader](#reader)
    - [Writer](#writer)
    - [Examples](#examples)
<!-- tocstop -->

## Architecture

Project consists of 4 separate modules
 - Models
 - Reader
 - Writer
 - Examples

### Models
Contains case classes intended to be used by the team as contractual classes.

Module is not mandatory, pretty much any case class would do.

### Reader
Reader is intended to be used for reading messages as case classes from kafka topic.

Only requirement for the class is to be serializable with circe.io, either explicitly, or by importing circe.auto before usage

1. Define *Properties* with intended properties for apache.kafka. Payload is to be treated as a string, serialization is handled by KafkaCase
2. Construct a *Reader* (*ReaderImpl*) with properties for intended *topic* and topic's CaseClass as type argument
3. Reader implements Iterator, so just iterate over items as they appear

Note:
 - Reader implements AutoCloseable, so treat it accordingly
 - Reader reads forever, should you wish to finish iteration once no more items are available, set optional parameter during construction phase accordingly

### Writer
Writer is intended to be used for writing messages as case classes into kafka topic.

Only requirement for the class is to be serializable with circe.io, either explicitly, or by importing circe.auto before usage

1. Define *Properties* with intended properties for apache.kafka. Payload is to be treated as a string, serialization is handled by KafkaCase
2. Construct a *Writer* (*WriterImpl*) with properties for intended *topic* and topic's CaseClass as type argument
3. Call writer.Write(key:String, message: CaseClass) as required

Note:
- Writer implements AutoCloseable, so treat it accordingly
- Writer is invoked as Fire & Forget, if you need sync behavior, you can either
  - call *flush* whenever you need a checkpoint
  - use *WriteSync* instead, which calls *Flush* after each message

### Examples
Sample usage of the library can be observed in Examples module