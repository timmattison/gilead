

L'objectif de cette servlet est de faire du server-push de donnée en utilisant l'interface CometProcessor de tomcat a travert des appels RPC de gwt tout en utilisant des pojo hibernate.

en bref:
le protocole http n'implemente pas de mode connecté, ni aucune comunication a l'initiative du serveur.
L'une des astuces utilisées est la suivante:
  1- le client initie une connexion avec le serveur: une simple requete http, qui sera, dans notre cas, un appel rpc.
  2- le serveur attend d'avoir des données a envoyer au client. (pendant ce temps le client attend la réponse !)
  3- le serveur envoie enfin une réponse.
  4- retour 1.

Ainsi le client recoie les données dès quelle sont disponible.

Une methode trivial serait de faire un sleep dans la methode doPost/doGet de la servlet jusqu'a avoir des donnés a envoyer.
Le principal problème de cette methode est qu'elle utilise un thread par client. L'interface CometProcessor de tomcat se propose de résoudre ce problème au travers d'une interface asynchrone (method event). notez que d'autre serveur propose d'autre solutions non compatible. ex: jetty continuation.

plus d'info ici: http://tomcat.apache.org/tomcat-6.0-doc/aio.html
et ici: http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/CometProcessor.html

Toute la mecanique des appels rpc de gwt et gilead passent par la methode doPost, or cette dernière n'est pas utilisé si la servlet implemente CometProcessor. La servlet proposé utilise simplement la mecanique standard gwt et gilead dans la method event.

pour l'utiliser:
- faite hériter votre servlet de HibernateCometService plutot que HibernateServiceRemote.
- Activez les comet dans la config de tomcat (use NIO HTTP connectors).
- Annotez le service que vous voulez rendre bloquant avec @Comet.
- Surchargez la methode haveData. Elle doit renvoyer false si aucune donné n'est disponible. Si cette methode renvoie toujours false, les methodes annoté par @Comet seront systématiquement bloquante.
- Surchargez éventuellement la methode onCurrentRequestAbort qui sera appelé lorsqu'une requette est annulé par le client.
- coté client relancer l'appel rpc dès reception de la réponse.

note:
- Du a la limitation de 2 requettes http pour le client, vous devez vous assurez de n'avoir qu'un seul service en attente sous peine de bloquer le reste de votre application.
- pour les utilisateurs de mac, lancez la JVM serveur avec l'option -Djava.net.preferIPv4Stack=true (cf: http://lists.apple.com/archives/java-dev/2006/Jun/msg00414.html)


