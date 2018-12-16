package com.xingyun;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogs {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		//RabbitMQ��������
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		//���ý���
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		
		//ʹ�����ɵ����ƴ���һ���ǳ־õģ���ռ���Զ�ɾ������
		String queueName = channel.queueDeclare().getQueue();
		
		//��
		channel.queueBind(queueName,EXCHANGE_NAME, "");
		
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}