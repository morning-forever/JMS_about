package com.gcy.p2p;

import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsP2pProducer {
	public static void main(String[] args) {
		
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();          
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL,"t3://localhost:7001");      
        
        
        
        QueueConnection connection = null;
        QueueSession session = null;
        Context ctx = null;

        try {
             
            
            ctx = new InitialContext(env);

            //1.通过JNDI查询ConnectionFactory weblogic.jms.connection.factory
           // QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("weblogic.jms.p2p.factory");
            QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("weblogic.jms.p2p.factory");

            //2.通过工厂建立连接connection
            connection = factory.createQueueConnection();

            //3.创建session
            session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);

            //4.创建Destination
            Destination destination = (Destination) ctx.lookup("weblogic.jms.p2p.queue");

            //5.创建MessageProducer（生产）或者MessageConsumer（消费）
            MessageProducer mp = session.createProducer(destination);

            //构造消息体
            TextMessage tm = session.createTextMessage();
            tm.setText("csdn tczxg");
            tm.setStringProperty("user", "嘿嘿，JMS挺简单的。。。");

            mp.send(tm);

        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (JMSException jse) {
            jse.printStackTrace();
        } finally {
            if (session != null)
                try {
                    session.close();
                } catch (JMSException e) {
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            if (ctx != null)
                try {
                    ctx.close();
                } catch (NamingException e) {
                }
        }
    }
}

