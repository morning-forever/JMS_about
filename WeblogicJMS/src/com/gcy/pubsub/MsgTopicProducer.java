package com.gcy.pubsub;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MsgTopicProducer {
	/** 
     * @���ܣ����ⷢ���� 
     * @���ߣ� 
     * @���ڣ�2012-10-18 
     */  
      
    private TopicPublisher publisher;  
    private TextMessage msg;  
      
    public MsgTopicProducer(String[] args) throws NamingException, JMSException  
    {  
        /*��ʼ�������Ķ���*/  
        String url = "t3://localhost:7001";  
        Properties p = new Properties();  
        p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");  
        p.put(Context.PROVIDER_URL, url);  
        Context ctx = new InitialContext(p);  
          
        /*����һ�����ӹ���*/  
        TopicConnectionFactory tConFactory = (TopicConnectionFactory)  
                ctx.lookup("weblogic.jms.connection.factory");  
        /*����һ������*/  
        Topic messageTopic = (Topic) ctx.lookup("weblogic.jms.topic.test");  
        /*����һ������*/  
        TopicConnection tCon = tConFactory.createTopicConnection();  
        /*����һ���Ự*/  
        TopicSession session = tCon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);  
          
        /*����һ�����ⷢ���ߣ���������Ϣ*/  
        publisher = session.createPublisher(messageTopic);  
        msg = session.createTextMessage();  
          
    }  
    public void runClient() throws JMSException  
    {  
        msg.setText("Hello");  
        publisher.publish(msg);  
        msg.setText("Welcome to JMS!");  
        publisher.publish(msg);  
        System.out.println("�ɹ���");  
    }  
      
    public static void main(String[] args) throws NamingException, JMSException {  
          
        MsgTopicProducer mp = new MsgTopicProducer(args);  
        mp.runClient();  
    }  
  
}  

