package com.gcy;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class WeblogicMdb  implements MessageListener{

	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		 if(arg0 instanceof TextMessage)
	        {   
	            TextMessage txtMsg = (TextMessage) arg0; 
	            try {
	                System.out.println("接收到的消息为："+txtMsg.getText());
	            } catch (JMSException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	}
	

}
