What is this
----
This is an example application demonstrating wrong behaviour when stopping a TomEE that has two applications running that both use the Datastax driver for cassandra.

What has changed since the [initial test case](https://github.com/schroenser/parallel-cassandra-webapps/tree/initial-test-case)
----
The producers and the corresponding disposers for `Cluster` and `Session` were replaced by a CDI extension that is able to register beans for `Cluster` and `Session` in the application scope.

How to use this
----
1. Build and start using maven: `mvn clean package cassandra:start tomee:run cassandra:stop`
2. Wait for TomEE to complete starting. It should say something like `INFO - Server startup in XYZ ms`.
3. Call the resources of both applications by accessing [http://localhost:8080/test1/greeting](http://localhost:8080/test1/greeting) and [http://localhost:8080/test2/greeting](http://localhost:8080/test2/greeting) with a browser
4. Stop TomEE by issuing `quit` in the console.

The wrong behaviour
----
This change gets rid of some of the exceptions concerning `ThreadLocal`s, but the warnings about unstopped threads remain. There is still only one call to close the `Session` and the `Cluster` in the log file, before the warnings are issued. The second pair of `Cluster` / `Session` instances is never closed.