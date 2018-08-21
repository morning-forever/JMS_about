package com.gcy;

import java.util.Hashtable;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;


public class WeblogicJMSClient {
	public static void main(String args[]){
		try {
            
			//0.initial JNDI Context 初始化jndi上下文
			Hashtable<Object,Object> env = new Hashtable<Object,Object>();          
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(Context.PROVIDER_URL,"t3://localhost:7001");      
            InitialContext ic = new InitialContext(env);
            
             //1.通过lookup创建连接工厂——Look up connectionFactory by using JNDI Context
             QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)ic.lookup("gt3.esb.jms.con.ESBConnectionFactory");
            
             //2.用连接工厂创建一个连接对象——use  connectionFactory creating connection
             QueueConnection queueConn = queueConnectionFactory
                    .createQueueConnection(); 
             
            //3.create session  （用连接对象创建会话session）——use connnection creating session
            QueueSession qSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);  
            
            //4.find queue by JNDI lookup （通过lookup找到队列queue/kju/——其实就是目的地destination）
             Queue queue = (Queue)ic.lookup("gt3.esb.jms.mdb.BaseQueueAsynMDBean");
             
            //5.create sender (通过会话session和目的地destination创建一个发送器sender，用于发送send后续session创建的消息Message) 
            QueueSender qSender = qSession.createSender(queue);  
            
            //6.create message  (使用会话session创建文本消息TextMessage)
            Message msg = qSession.createTextMessage("Message is from JMS SenderHell32142342o!"); 
            
            //7.send message （使用发送器Sender发送消息Message）
            /**
             * Why can the message  arrive to destination? 为什么消息能到达目的地？或者说，为什么消息接收者为什么能接收到消息？
             * 因为上面创建的sender是用Session和Queue来创建的（Queue其实可以把它当做destination），而Session又是通过ConnectionFactory
             * 创建了connection进而创建的，ConnecionFactory又是通过jndi找到的，这个jndi又可以锁定到某个MessageDrivenBean。所以当Sender
             * 发送消息时，MessageDrivenBean就能收到消息。
             */
            qSender.send(msg);  
            qSession.close();
            queueConn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
/**
 * 总结JMS编程模式：
 * 一、JMS编程模式的几个概念
 * （1）ConnectionFactory 连接工厂，能创建连上消息消费者的连接的工厂。如果JMS provider 是weblogic，那么这个消息消费者就是某个MessageDrivenBean。
 * 
 * （2）Queue 消息队列，其实消息创建并发送后，消息消费者并没有马上接收到消息。当发送消息时，是把消息放到消息队列。特定的消息队列一定是针对特定的消息消费者。
 * 想象一下，一系列的消息在排着队等着消息消费者消费。
 * 
 * （3）Message 消息实体，Message是用Session来创建的。Session是用Connection来创建的，Connection是ConnectionFactory创建得来的。
 * 
 * （4）Sender 消息发送者，消息发送者调用send方法来发送消息。消息发送者是Session创建的，Session可以创建要发送消息给某个queue的Sender。
 *  
 * 二、JMS编程步骤
 * （1）创建ConnectionFactory
 * （2）用ConnectionFactory创建Connection
 * （3）用Connection创建Session
 * （4）创建Queue
 * （5）用Session和Queue创建Sender
 * （6）用Session创建Message
 * （7）用Sender发送Message
 * 
 *三、以上两点说的都是JMS客户端编程。下面说下JMS服务端编程。
 *  （1）上面提到的消息消费者就是存在于JMS服务端。消息消费者都要实现javax.jms.MessageListener接口的onMessage方法。
 *  
 *四、JMS的提供者有ActiveMQ,Weblogic等。本次总结主要是基于自己用weblogic做了一个例子后的分析。weblogic主要是通过发布一个MessageDrivenBean来实现
 *JMS服务。发布JMS服务时，要指定两个JNDI,一个用于暴露ConnectionFactory,一个用于暴露Destination
 * 
 * 
 */
