hibernate-envers-cme
====================

Run ./gradlew to see the java.util.ConcurrentModificationException

	Exception in thread "main" java.util.ConcurrentModificationException
		at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:819)
		at java.util.ArrayList$Itr.next(ArrayList.java:791)
		at org.hibernate.engine.spi.ActionQueue$BeforeTransactionCompletionProcessQueue.beforeTransactionCompletion(ActionQueue.java:660)
		at org.hibernate.engine.spi.ActionQueue.beforeTransactionCompletion(ActionQueue.java:307)
		at org.hibernate.internal.SessionImpl.beforeTransactionCompletion(SessionImpl.java:611)
		at org.hibernate.engine.transaction.internal.jdbc.JdbcTransaction.beforeTransactionCommit(JdbcTransaction.java:105)
		at org.hibernate.engine.transaction.spi.AbstractTransactionImpl.commit(AbstractTransactionImpl.java:175)
		at demo.Main.main(Main.java:74)

