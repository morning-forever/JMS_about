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
     * @功能：发布-订阅消息服务   同步接收者 
     * @作者： 
     * @日期：2012-10-18 
     */  
      
    private TopicSubscriber subscriber ;  
    private TextMessage msg;  
      
    public SyncMessageTopicReceiver() throws NamingException, JMSException  
    {  
        /*初始化上下文对象*/  
        String url = "t3://localhost:7001";  
        Properties p = new Properties();  
        p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");  
        p.put(Context.PROVIDER_URL, url);  
        Context ctx = new InitialContext(p);  
          
        /*创建主题连接工厂*/  
        TopicConnectionFactory tConFactory = (TopicConnectionFactory)   
                ctx.lookup("weblogic.jms.connection.factory");  
        /*创建一个主题*/  
        Topic messageTopic = (Topic) ctx.lookup("weblogic.jms.topic.test");  
        /*创建连接*/  
        TopicConnection tCon = tConFactory.createTopicConnection();  
        /*创建会话*/  
        TopicSession session = tCon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);  
        /*创建接收者*/  
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
