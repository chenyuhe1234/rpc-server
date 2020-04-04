package com.gupaoedu.vip;

import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
@Component
public class GpRpcServer {

	ExecutorService executorService = Executors.newCachedThreadPool();


	// 使用socketServer来暴露服务
	public void publisher(Object service, int port) {

		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(8080);
			// 不断的去监听处理socket请求 每个socket都用单独的线程去处理
			while (true) {
				Socket socket = serverSocket.accept();

				executorService.execute(new ProcessorHandler(socket, service));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}
