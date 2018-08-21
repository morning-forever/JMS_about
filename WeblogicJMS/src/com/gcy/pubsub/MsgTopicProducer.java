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
     * @功能：主题发送者 
     * @作者： 
     * @日期：2012-10-18 
     */  
      
    private TopicPublisher publisher;  
    private TextMessage msg;  
      
    public MsgTopicProducer(String[] args) throws NamingException, JMSException  
    {  
        /*初始化上下文对象*/  
        String url = "t3://localhost:7001";  
        Properties p = new Properties();  
        p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");  
        p.put(Context.PROVIDER_URL, url);  
        Context ctx = new InitialContext(p);  
          
        /*创建一个连接工厂*/  
        TopicConnectionFactory tConFactory = (TopicConnectionFactory)  
                ctx.lookup("weblogic.jms.connection.factory");  
        /*创建一个主题*/  
        Topic messageTopic = (Topic) ctx.lookup("weblogic.jms.topic.test");  
        /*创建一个连接*/  
        TopicConnection tCon = tConFactory.createTopicConnection();  
        /*创建一个会话*/  
        TopicSession session = tCon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);  
          
        /*创建一个主题发布者，并发送消息*/  
        publisher = session.createPublisher(messageTopic);  
        msg = session.createTextMessage();  
          
    }  
    public void runClient() throws JMSException  
    {  
        msg.setText("Hello");  
        publisher.publish(msg);  
        msg.setText("Welcome to JMS!");  
        publisher.publish(msg);  
        System.out.println("成功！");  
    }  
      
    public static void main(String[] args) throws NamingException, JMSException {  
          
        MsgTopicProducer mp = new MsgTopicProducer(args);  
        mp.runClient();  
    }  
  
}  

