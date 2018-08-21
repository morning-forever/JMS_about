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
            
			//0.initial JNDI Context ��ʼ��jndi������
			Hashtable<Object,Object> env = new Hashtable<Object,Object>();          
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(Context.PROVIDER_URL,"t3://localhost:7001");      
            InitialContext ic = new InitialContext(env);
            
             //1.ͨ��lookup�������ӹ�������Look up connectionFactory by using JNDI Context
             QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)ic.lookup("gt3.esb.jms.con.ESBConnectionFactory");
            
             //2.�����ӹ�������һ�����Ӷ��󡪡�use  connectionFactory creating connection
             QueueConnection queueConn = queueConnectionFactory
                    .createQueueConnection(); 
             
            //3.create session  �������Ӷ��󴴽��Ựsession������use connnection creating session
            QueueSession qSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);  
            
            //4.find queue by JNDI lookup ��ͨ��lookup�ҵ�����queue/kju/������ʵ����Ŀ�ĵ�destination��
             Queue queue = (Queue)ic.lookup("gt3.esb.jms.mdb.BaseQueueAsynMDBean");
             
            //5.create sender (ͨ���Ựsession��Ŀ�ĵ�destination����һ��������sender�����ڷ���send����session��������ϢMessage) 
            QueueSender qSender = qSession.createSender(queue);  
            
            //6.create message  (ʹ�ûỰsession�����ı���ϢTextMessage)
            Message msg = qSession.createTextMessage("Message is from JMS SenderHell32142342o!"); 
            
            //7.send message ��ʹ�÷�����Sender������ϢMessage��
            /**
             * Why can the message  arrive to destination? Ϊʲô��Ϣ�ܵ���Ŀ�ĵأ�����˵��Ϊʲô��Ϣ������Ϊʲô�ܽ��յ���Ϣ��
             * ��Ϊ���洴����sender����Session��Queue�������ģ�Queue��ʵ���԰�������destination������Session����ͨ��ConnectionFactory
             * ������connection���������ģ�ConnecionFactory����ͨ��jndi�ҵ��ģ����jndi�ֿ���������ĳ��MessageDrivenBean�����Ե�Sender
             * ������Ϣʱ��MessageDrivenBean�����յ���Ϣ��
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
 * �ܽ�JMS���ģʽ��
 * һ��JMS���ģʽ�ļ�������
 * ��1��ConnectionFactory ���ӹ������ܴ���������Ϣ�����ߵ����ӵĹ��������JMS provider ��weblogic����ô�����Ϣ�����߾���ĳ��MessageDrivenBean��
 * 
 * ��2��Queue ��Ϣ���У���ʵ��Ϣ���������ͺ���Ϣ�����߲�û�����Ͻ��յ���Ϣ����������Ϣʱ���ǰ���Ϣ�ŵ���Ϣ���С��ض�����Ϣ����һ��������ض�����Ϣ�����ߡ�
 * ����һ�£�һϵ�е���Ϣ�����Ŷӵ�����Ϣ���������ѡ�
 * 
 * ��3��Message ��Ϣʵ�壬Message����Session�������ġ�Session����Connection�������ģ�Connection��ConnectionFactory���������ġ�
 * 
 * ��4��Sender ��Ϣ�����ߣ���Ϣ�����ߵ���send������������Ϣ����Ϣ��������Session�����ģ�Session���Դ���Ҫ������Ϣ��ĳ��queue��Sender��
 *  
 * ����JMS��̲���
 * ��1������ConnectionFactory
 * ��2����ConnectionFactory����Connection
 * ��3����Connection����Session
 * ��4������Queue
 * ��5����Session��Queue����Sender
 * ��6����Session����Message
 * ��7����Sender����Message
 * 
 *������������˵�Ķ���JMS�ͻ��˱�̡�����˵��JMS����˱�̡�
 *  ��1�������ᵽ����Ϣ�����߾��Ǵ�����JMS����ˡ���Ϣ�����߶�Ҫʵ��javax.jms.MessageListener�ӿڵ�onMessage������
 *  
 *�ġ�JMS���ṩ����ActiveMQ,Weblogic�ȡ������ܽ���Ҫ�ǻ����Լ���weblogic����һ�����Ӻ�ķ�����weblogic��Ҫ��ͨ������һ��MessageDrivenBean��ʵ��
 *JMS���񡣷���JMS����ʱ��Ҫָ������JNDI,һ�����ڱ�¶ConnectionFactory,һ�����ڱ�¶Destination
 * 
 * 
 */
