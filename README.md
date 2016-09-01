What is this
----
This is an example application demonstrating wrong behaviour when stopping a TomEE that has two applications running that both use the Datastax driver for cassandra.

How to use this
----
1. Build and start using maven: `mvn clean package cassandra:start tomee:run cassandra:stop`
2. Wait for TomEE to complete starting. It should say something like `INFO - Server startup in XYZ ms`.
3. Call the resources of both applications by accessing [http://localhost:8080/test1/greeting](http://localhost:8080/test1/greeting) and [http://localhost:8080/test2/greeting](http://localhost:8080/test2/greeting) with a browser
4. Stop TomEE by issuing `quit` in the console.

The wrong behaviour
----
Only one of the applications mangages to call the dispose methods for its `Session` and `Cluster` instances. After that, the TomEE starts to complain about unstopped threads and unremoved `ThreadLocal` instances. The dispose methods of the second application are not called.