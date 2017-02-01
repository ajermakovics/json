# Json

Convenience library for handling JSON objects in Java. Goals:

- Usability
- Minimise verbosity 
- Play well with Nashorn JavaScript engine from Java 8

In essence `Json` is just a thin wrapper around a `Map` where properties are stored. There are lots of functions to manipulate a `Map` such as in Guava or Apache Commons, and you should be able to re-use them. Additional `Map` utilities are provided here. 

     Json addr = new Json() {
    	String host = "localhost";
    	int port = 80;
    };
    	 
    addr.toString(); 	// {"host": "localhost", "port": 80}
    addr.set("path", "/").set("proto", "http");
    addr.get("port"); 	// 80

See tests for more examples.	

## Features

 - Simple and convenient constructors
 - Implements `Map` interface so you can treat it like a Map of Maps
 - JDK8 Nashorn compatible - use it in JavaScript functions
 - Convenience methods for building and getting values
 - Serialize to/from String
 - Access properties without explicit casting
 - Provides `equals`/`hashCode`, so you can store Json in collections

## Download

Grab it from [JitPack](https://jitpack.io/#ajermakovics/json)

Json depends on Jackson

## Constructors

    new Json("{port: 80}"); // from string
    new Json( hashMap );  // from map
    new Json("port", 80); // key-value
    new Json() { int port = 80; } // from fields 

## Operations

    Json json = new Json().set("port", 80)
                          .set("host", "localhost"); // chain calls
    
    int port = json.get("port", 0); // get or return default. avoid casting 
     
    Json nested = json.at("path", "to", "nested"); // json.path.to.nested
    
    json.merge( anotherJson ); // merge values recursively
    
    json.copy(); // deep copy

As well as all the methods from `Map`:

   - isEmpty(), keySet(), containsKey(), entrySet(), ...
    
## Scripting usage

You can pass `Json` to Nashorn JavaScript functions and treat it like a native JSON object:

    Json addr = new Json("port", 80);

    scriptEngine.eval("function nextPort(addr) { addr.port++; }");

    invocable.invokeFunction("nextPort", addr);
		
    out.println( addr.get("port") ); // 81
             
## Build

    gradle jar
    
Note: running tests requires JDK 8 due to Nashorn tests. Java 6 is required for compiling and building the jar.

## License

Apache 2.0

