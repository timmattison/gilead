
Goal of this servlet is to use a data server-push mechanism by using the tomcat CometProcessor interface with gwt/gilead rpc call.

a short explanation:
http protocol doesn't implement any connected mode nor data transfert to the server initiative.
One of the tricks which may be used is the following:
 1- client starts a connexion with server by running a simple http request. In our case, an rpc call.
 2- server is waiting for data to be send to client. (so client is waiting for data, but has no answer)
 3- server finally sends data to client and closes the request.
 4- go back to 1.

With this mechanism, client gets data as soon as they are produced.

A trivial approch could be made with a 'sleep' in method doPost/doGet of your servlet until data have to be send. (in our case the rpc service)
Main drawback of this first approch is that it uses one thread per client. Tomcat CometProcessor interface can solve this by using an asynchron interface (method event). Note that other java containers propose their own solution which are not compatible. (ex: jetty continuation)

more info here: http://tomcat.apache.org/tomcat-6.0-doc/aio.html
and here: http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/CometProcessor.html

All gwt/gilead rpc call plumbing use the standard httpServlet interface (doPost method). Thus doPost ins't called by tomcat while servlet implements CometProcessor. So, this piece of code implements gwt/gilead rpc in 'event' method.

to use it:
- inerit your service implementation servlet from HibernateCometService instead of HibernateServiceRemote.
- Activate comet in tomcat config (use NIO HTTP connectors or APR).
- Annotate service method you want to be a blocking method with @Comet.
- Overload 'haveData' method. It should return false if no data is available. If it always returns false, anotated methods with @Comet will allways be blocking.
- If necessary overload 'onCurrentRequestAbort' method which will be called when client aborts a request.
- On client side, recall the rpc service straight after response is received.

note:
- Due to the two simultanous http request limitation, you have to ensure that only one blocking rpc call is pending. Otherwise the rest of your application won't be able to comunicate with server.
- for mac user, you will have to launch your server JVM with option: -Djava.net.preferIPv4Stack=true (cf: http://lists.apple.com/archives/java-dev/2006/Jun/msg00414.html)

