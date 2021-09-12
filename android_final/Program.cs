using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Net;
using System.Threading;
using System.Text.RegularExpressions;
using MySql.Data.MySqlClient;
using System.IO;

namespace android_final
{
    public class PicAndSocket 
    {
        public int index=0;
        public Socket socket;
        public PicAndSocket(Socket s,int i)
        {
            index = i;socket = s;
        }
    }
    public class Client
    {
        Socket socket;
        int id = -1;

        public void SetSocket(Socket sock)
        {
            socket = sock;
        }
        public void SetId(int ID)
        {
            id = ID;
        }
   
        public int GetId()
        {
            return id;
        }
     
        public Socket GetSocket()
        {
            return socket;
        }
    }
    public class MysqlConn {
        static string connetStr = "server=127.0.0.1;port=3306;user=root;password=123456; database=androiduser;";
        MySqlConnection conn = new MySqlConnection(connetStr);
        MySqlCommand cmd;
        public void Init()
        {
            try
            {
                conn.Open();//打开通道，建立连接，可能出现异常,使用try catch语句
                Console.WriteLine("已经建立连接");
                //在这里使用代码对数据库进行增删查改
            }
            catch (MySqlException ex)
            {
                Console.WriteLine(ex.Message);
            }

        }
        public string CheckUser(int account,string password)
        {
            Console.WriteLine("CheckUser Start..... account="+account +" || password= "+password);
            string sql = "select * from users where account = "+account+" and password = '"+password+"'";
            try
            {
                cmd = new MySqlCommand(sql, conn);
                MySqlDataReader reader = cmd.ExecuteReader();//执行ExecuteReader()返回一个MySqlDataReader对象
                if (reader.HasRows)
                {
                    while (reader.Read())//初始索引是-1，执行读取下一行数据，返回值是bool
                    {
                        Console.WriteLine("The result : " + reader.GetInt32("uid") + reader.GetString("account") + reader.GetString("password"));
                        reader.Close();
                        return "yes";
                    }
                }
                else
                {
                    Console.WriteLine("account and password are not match");
                    reader.Close();
                    return "no";
                }
                
            }
            catch(MySqlException e)
            {
                Console.WriteLine(e.Message);
            }
            return null;
        }

        public void Sent()
        {

        }
        
        
    }

    public  class Server
    {
        MysqlConn dataBase = new MysqlConn();
        static int id = 0;//分配的ip
        //所有通信的socket
        List<Client> users = new List<Client>();
        public Server() {
            Console.WriteLine("-----------Mysql connect-----------");
            dataBase.Init();
            Console.WriteLine("-----------Server start-----------");
            string ipv4 = GetLocalIp();
            Console.WriteLine("Server host is :"+ipv4);
            IPEndPoint ipep = new IPEndPoint(IPAddress.Parse(ipv4), 4040);
            Socket serverSocket = new Socket(ipep.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            serverSocket.Bind(ipep);
            //同时连接的最大上限  多了排队
            serverSocket.Listen(10);
            //创建后台线程一直等待连接连接
            Thread thread = new Thread(Connecting);
            thread.IsBackground = true;
            thread.Start(serverSocket);
        }

        public void Connecting(object serverSocket)
        {
            while (true)
            {
                Socket socket = (Socket)serverSocket;
                //返回通信的套接字
                try
                {
                    Socket clientSocket = socket.Accept();
                    Client client = new Client();
                    client.SetSocket(clientSocket);
                    client.SetId(id);
                    id++;
                    Console.WriteLine("Get User that id = "+id);
                    Thread thread1 = new Thread(Connecting);
                    thread1.IsBackground = true;
                    thread1.Start(serverSocket);

                    //持续接收消息
                    users.Add(client);
                    Thread thread = new Thread(GetMsg);
                    thread.IsBackground = true;
                    thread.Start(clientSocket);
                }
                catch { }
            }
        }

        public void EasySent(int id)
        {
            Console.WriteLine("try to sent ....");
            foreach(Client user in users)
            {

                    string message = "i sent it to you "+"\n";
                    byte[] buf = Encoding.UTF8.GetBytes(message);
                    try
                    {
                        user.GetSocket().Send(Encoding.UTF8.GetBytes("i sent it to you "+ "\n"));
                    }
                    catch
                    {

                    }
            }
        }
        public void GetMsg(object socketMsg)
        {
            Socket socket = (Socket)socketMsg;
            FileStream fs = new FileStream("D:\\qiguai.jpg", FileMode.Append);
            int picIndex = 0;
            int count = 0;
            
            Console.WriteLine("right now get message");
            while (true)
            {
                Byte[] buff = new Byte[1000];
                try
                {
                    int length = socket.Receive(buff);
                    //显示内容
                    string str = Encoding.UTF8.GetString(buff, 0, length);
                    string[] arr = Regex.Split(str, ":");
                    Console.WriteLine("android:"+str);
                     //a:account:password
                    switch (arr[0])
                    {
                        case "a":
                          socket.Send(Encoding.UTF8.GetBytes("a:"+dataBase.CheckUser(Convert.ToInt32(arr[1]), arr[2])));
                            break;
                        case "b":
                            count = picIndex = Convert.ToInt32(arr[1]);
                            Console.WriteLine("一共有" + picIndex + "个包");
                            //PicAndSocket picAndSocket = new PicAndSocket(socket, picIndex);
                            //Thread thread = new Thread(GetPicture);
                            //thread.IsBackground = true;
                            //thread.Start(picAndSocket);
                            break;
                        case "c":
                            fs.Close();
                            break;
                        case "d":
                            Console.WriteLine("d！");
                            string fileName = "D:\\1.jpg";
                            FileInfo fileInfo = new FileInfo(fileName);
                            Console.WriteLine("传给android的图片大小是" + fileInfo.Length);
                            FileStream stream = fileInfo.OpenRead();
                            
                            Byte[] buffer = new Byte[fileInfo.Length];
                            stream.Read(buffer, 0, buffer.Length);
                           
                            socket.Send(buffer);

                            break;
                        //default:
                        //    fs.Write(buff, 0, length);








                            //if (picIndex == 0)
                            //{
                            //    Console.WriteLine("picIndex == 0 结束");
                            //    fs.Write(picBuff, 0, picBuff.Length);
                            //    fs.Close();

                            //    Console.WriteLine("图片大小 = " + picBuff.Length);
                            //    Console.WriteLine("图片大小1 = " + length);
                            //}
                            //else
                            //{

                            //    fs.Write(buff, 0, length);
                            //    Console.WriteLine("某一个包");
                            //    picIndex--;
                            //    buff.CopyTo(picBuff, picIndex);

                            //}
                            break;
                    }
                    

                }
                catch { }
            }
        }

      public void  GetPicture (object picAndSocket)
        {
            Byte[] buff = new Byte[1000];
            PicAndSocket PAS = (PicAndSocket)picAndSocket;
            int index = PAS.index;
            Socket socket = PAS.socket;
            FileStream fs = new FileStream("D:\\qiguai.jpg", FileMode.CreateNew);
            while (true)
            {
                int length = socket.Receive(buff);
          
                fs.Write(buff, 0, length);
                index--;
                Console.WriteLine("一个包...");
                if (index == 0)
                {
                    break;
                }
            }
            fs.Close();

        }

        public string GetLocalIp()
        {
            ///获取本地的IP地址
            string AddressIP = string.Empty;
            foreach (IPAddress _IPAddress in Dns.GetHostEntry(Dns.GetHostName()).AddressList)
            {
                if (_IPAddress.AddressFamily.ToString() == "InterNetwork")
                {
                    AddressIP = _IPAddress.ToString();
                }
            }
            return AddressIP;
        }

    }
    class Program
    {
        static void Main(string[] args)
        {
           
            Server sv = new Server();
            while (true) ;
        }
    }
}
