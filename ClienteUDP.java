import java.io.*;
import java.net.*;

public class ClienteUDP
{
   private  int portaServidor; // 9871
   private  byte[] sendData = new byte[15000];
   private  byte[] receiveData = new byte[15000];
   private  String enderecoIP;


   public  byte[] lerString () throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      return in.readLine().getBytes();
   }
   public ClienteUDP(String ipAddress, int porta) throws Exception{
      this.enderecoIP = ipAddress;
      this.portaServidor = porta;
   }

   public void enviarArquivo (byte [] mensagem) throws Exception{
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress ipServidor = InetAddress.getByName(enderecoIP);
      //InetAddress ipServidor = InetAddress.getByName("192.168.0.179");
      sendData = mensagem;

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipServidor, portaServidor);
      clientSocket.send(sendPacket);

      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);
      clientSocket.close();

      System.out.println("FROM SERVER:" + receivePacket.getData());
   }
}
