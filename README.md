# KafkaCase
Adapter for communicating from Scala with Kafka via case classes

<!-- toc -->
- [Running](#running)
- [Architecture](#architecture)
    - [Models](#models)
    - [Reader](#reader)
    - [Writer](#writer)
    - [Examples](#examples)
<!-- tocstop -->

## Running
First, you need to have installed Java and sbt

To run unit tests cross-scala versions on clean build, run:
`sbt +clean +test`

To run examples, run:
`sbt "project examples" run`
It might be worthwhile to change the main (examples module KafkaCase main class), to only run the part you are copy-pasting into your implementation so you can watch and debug that one

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

Whole module showcases simple scenario how to use writer as well as reader. Each scenario is showcased in 3 different flavors.
- ManualResourceHandling
  - User is expected to manually dispose of resource after using it
- CustomResourceHandling
  - custom code encapsulating resource usage is used
- UsingsResourceHandling
  - Usings paradigm is used, note, this requires scala version 3
  - Samples are in scala3 specific folder due to compiler reasons for scala 2

Simply executing example module as is, demonstrates usage in following order:
- 0 part before the main method collects necessary requirements (message class, settings etc...) which user is expected to obtain from its application/config
  - these are hard-coded value in this example, in real world use case, these would be sourced by the application utilizing this library 
- 1 demonstration of using Writer by means of ManualResourceHandling
  - writes two messages and closes resource manually 
- 2 demonstration of using Writer by means of CustomResourceHandling
  - writes two messages and exits block closing resource 
- 3 demonstration of using Writer by means of UsinsResourceHandling - only for Scala3
  - writes two messages and exits Using block, thus closing resource 
- 4 demonstration of using Reader by means of ManualResourceHandling
  - prints each message available in the topic and finishes, closes resource manually 
- 5 demonstration of using Reader by means of CustomResourceHandling
  - prints each message available in the topic and finishes, exits block closing resource 
- 6 demonstration of using Reader by means of UsinsResourceHandling - only for Scala3
  - prints each message available in the topic and finishes, exits Using block, thus closing resource 
