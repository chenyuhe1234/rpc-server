package com.gupaoedu.vip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ProcessorHandler implements Runnable {

	private Socket socket;

	private Object service;


	public ProcessorHandler(Socket socket, Object service) {
		this.socket = socket;
		this.service = service;
	}


	/**
	 * 反射进行方法的调用
	 */
	public Object invoke(RpcRequest rpcRequest) throws Exception {

		Object result = null;

		// 关注 请求的类 | 方法 | 方法入参
			/*反射调用方法*/
		Class clazz = Class.forName(rpcRequest.getClassName());
		// 获取方法的参数类列表
		Object[] params = rpcRequest.getParameters();
		Class<?>[] paramsType = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			paramsType[i] = params[i].getClass();
		}
		// 获取到具体的method
		Method targetMehod = clazz.getMethod(rpcRequest.getMethodName(), paramsType);

		// 反射进行方法的调用
		result = targetMehod.invoke(service, params);
		return result;
	}

	@Override
	public void run() {

		// 对请求的流进行反序列化 ---- > 拿到入参对象
		ObjectInputStream objectInputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
			Object result = invoke(rpcRequest);
			// 将返回的结果写入到socket中去
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(result);
			objectOutputStream.flush();


		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}