# EMF Persistence Framework

This framework is a general persistence environment for EMF. It is extensible to support different features of different persistence approaches. In addition to that it is fully OSGi backed and makes heavily use of the following specifications:

* Configuration Admin
* Declarative Services
* Promises
* Pushstreams

Because it is related to the Eclipse Modelling Framework, the Geckoprojects EMF OSGi framework is also used, to bring EMF into a dynamic environment.

This framework als provides an API to attach own persistent adapters. We currently provide adapters for:

* **MongoDB**
* **JDBC**
* **Lucene**

## Base Framework

Important components of the persistence framework are:

* **Persistence Runtime** - provides runtime information via DTO's. Each adapter must provide an own runtime instance. One of the major tasks of the runtime are
  * provide information about the overall capabilities that are supported by this adapter 
  * provide configuration information about connections as errors
* **Codec** - Basic implementation that helps to de-/serialize an EObject
* **Converter** - Custom data type conversion
* **Query Model** - Default model for basic queries
* **Projection Model** - default handling for projection
* **EMF OSGi** integration
  * *UriHandlerProvider* handling, to register URI schemas for a certain adapter
  * *ResourceSetConfiguration* handling
* **Pushstream support** to stream data