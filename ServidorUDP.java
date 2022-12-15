import java.io.*;
import java.net.*;
import java.util.Calendar;

class ServidorUDP
{
   private static int portaServidor = 9871;
   private static byte[] receiveData = new byte[15000];
   private static byte[] sendData = new byte[15000];

   public static void main(String args[]) throws Exception
   {
      DatagramSocket serverSocket = new DatagramSocket(portaServidor);

      while(true) 
      {
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

         System.out.println("Aguardando datagrama do cliente....");
         serverSocket.receive(receivePacket);

         System.out.println("RECEIVED: " + receivePacket.getData());
         InetAddress ipCliente = receivePacket.getAddress();
         int portaCliente = receivePacket.getPort();
         sendData = (new String(receivePacket.getData(),0,receivePacket.getLength())).toUpperCase().getBytes();

         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipCliente, portaCliente);
         serverSocket.send(sendPacket);
         try (FileOutputStream fos = new FileOutputStream("arquivos_chat/arquivo_recebido.txt")) {
            fos.write(receivePacket.getData());
        }
      }
   }
}
