package demo;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.action.spi.AfterTransactionCompletionProcess;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.Audited;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;

public class Main {

	
	@Entity(name="person")
	@Audited
	public static class Person {
	    @Id public String id;
	    public String name;
	}
	
	public static void main(String[] args) {
		
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:devDb;MVCC=TRUE");
		cfg.setProperty("hibernate.connection.username", "sa");
		cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		cfg.setProperty("hibernate.show_sql", "true");
		
		cfg.addAnnotatedClass(Person.class);
		
		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor)cfg.buildSessionFactory();

		EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(new PostInsertEventListener() {
			
			@Override
			public void onPostInsert(PostInsertEvent event) {
				event.getSession().getActionQueue().registerProcess(new BeforeTransactionCompletionProcess() {
					
					@Override
					public void doBeforeTransactionCompletion(SessionImplementor session) {
						System.out.println("before transaction completion");
					}
				});
				event.getSession().getActionQueue().registerProcess(new AfterTransactionCompletionProcess() {
					
					@Override
					public void doAfterTransactionCompletion(boolean success, SessionImplementor session) {
						System.out.println("after transaction completion");
					}
				});
			}
		});

		
		Session session = sessionFactory.openSession();
		
		Transaction trans = session.beginTransaction();
		Person p = new Person();
		p.id = "1";
		p.name = "Cameron";
		session.save(p);
		
		trans.commit();
		
		session.close();
		
		sessionFactory.close();
	}
}
