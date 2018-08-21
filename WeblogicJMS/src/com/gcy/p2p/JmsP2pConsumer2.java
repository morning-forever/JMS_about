package com.gcy.p2p;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsP2pConsumer2 {
	public static void main(String[] args) {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory");
        System.setProperty(Context.PROVIDER_URL, "t3://localhost:7001");
        QueueConnection connection = null;
        QueueSession session = null;
        Context ctx = null;

        try {
            ctx = new InitialContext();

            //1.ͨ��JNDI��ѯConnectionFactory
            QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("weblogic.jms.p2p.factory");

            //2.ͨ��������������connection
            connection = factory.createQueueConnection();

            //3.����session
            session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);

            //4.����Destination
            Destination destination = (Destination) ctx.lookup("weblogic.jms.p2p.queue");

            //5.����MessageProducer������������MessageConsumer�����ѣ�
            MessageConsumer mc = session.createConsumer(destination);
            connection.start();
            
            //������Ϣ��
            Message message = mc.receive();
            
            if(message instanceof TextMessage){
                TextMessage tm = (TextMessage) message ;
                String user = tm.getStringProperty("user");
                System.out.println(tm.getText() + ", user : "+user);
            }
            connection.close();
            
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
            if (ctx != null)
                try {
                    ctx.close();
                } catch (NamingException e) {
                }
        }
    }
}
