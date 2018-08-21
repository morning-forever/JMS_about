package com.gcy.pubsub;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SyncMessageTopicReceiver {
	/** 
     * @���ܣ�����-������Ϣ����   ͬ�������� 
     * @���ߣ� 
     * @���ڣ�2012-10-18 
     */  
      
    private TopicSubscriber subscriber ;  
    private TextMessage msg;  
      
    public SyncMessageTopicReceiver() throws NamingException, JMSException  
    {  
        /*��ʼ�������Ķ���*/  
        String url = "t3://localhost:7001";  
        Properties p = new Properties();  
        p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");  
        p.put(Context.PROVIDER_URL, url);  
        Context ctx = new InitialContext(p);  
          
        /*�����������ӹ���*/  
        TopicConnectionFactory tConFactory = (TopicConnectionFactory)   
                ctx.lookup("weblogic.jms.connection.factory");  
        /*����һ������*/  
        Topic messageTopic = (Topic) ctx.lookup("weblogic.jms.topic.test");  
        /*��������*/  
        TopicConnection tCon = tConFactory.createTopicConnection();  
        /*�����Ự*/  
        TopicSession session = tCon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);  
        /*����������*/  
        subscriber = session.createSubscriber(messageTopic);  
        tCon.start();     
    }  
    public void runClient() throws JMSException  
    {  
        msg = (TextMessage) subscriber.receive();  
        System.out.println("Receiver:"+msg.getText());  
        msg = (TextMessage) subscriber.receive();  
        System.out.println("Receiver:"+msg.getText());  
          
    }  
    public static void main(String[] args) throws NamingException, JMSException {  
        SyncMessageTopicReceiver receiver = new SyncMessageTopicReceiver();  
        receiver.runClient();  
    }  
}
