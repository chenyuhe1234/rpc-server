# rpc-server
手写RPC服务端V1.0



        | ------ > 建立连接 Socket socket = new Socket(host,port);
        | ------ > 获取输出流 构造请求对象 请求对象中的数据 通过socket.getOutputStream()输出给服务端
