package com.gcy.pubsub;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
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

public class AsyncMessageTopicReceiver implements MessageListener {

	/** 
     * @����:����-������Ϣ���� �첽������Ϣ 
     * @���ߣ� 
     * @���ڣ�2012-10-18 
     */  
  
    private int EXPECTED_MESSAGE_COUNT = 10;  
    private int messageCount = 0;  
    private TopicSubscriber subscriber;  
  
    public AsyncMessageTopicReceiver() throws NamingException, JMSException {  
        /* ��ʼ�������Ķ��� */  
        String url = "t3://localhost:7001";  
        Properties p = new Properties();  
        p.put(Context.INITIAL_CONTEXT_FACTORY,  
                "weblogic.jndi.WLInitialContextFactory");  
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
        /* ���ü��� */  
        subscriber.setMessageListener(this);  
        tCon.start();  
  
    }  
  
    public boolean expectMoreMessage() {  
        return messageCount < EXPECTED_MESSAGE_COUNT;  
    }  
  
    @Override  
    public void onMessage(Message message) {  
        System.out.println("onMessage");  
        try {  
            TextMessage msg = (TextMessage) message;  
            System.out.println("Receiver:" + msg.getText());  
        } catch (JMSException e1) {  
            // TODO Auto-generated catch block  
            e1.printStackTrace();  
        }  
  
        messageCount++;  
    }  
  
    public static void main(String[] args) throws NamingException, JMSException {  
       /* int MAX_TRIES = 30;  
        int tryCount = 0;  */
        /*AsyncMessageTopicReceiver receiver = */new AsyncMessageTopicReceiver();  
        /*while (receiver.expectMoreMessage() && (tryCount < MAX_TRIES)) {  
              
            try {  
                System.out.println(tryCount);  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
            tryCount++;  
        }*/  
  
    }  

}
