import java.io.*;
import java.net.*;

public class ClienteTCP
{
   // private static int portaServidor = 6789;
   private int portaServidor; // 9871
   private String enderecoIP;

   public String lerString () throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      return in.readLine();
   }

   public ClienteTCP (String enderecoIP, int portaServidor){
      this.portaServidor = portaServidor;
      this.enderecoIP = enderecoIP;
   }

   public void sendData (String fileContent) throws Exception{
      //Efetua a primitiva socket
      Socket socket = new Socket(enderecoIP, portaServidor);
      try{
         DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
         //System.out.println ("CLIENTE TCP DADOS:");
         //System.out.println(fileContent);
         //System.out.println ("......................................................");
         saida.writeBytes(fileContent + '\n');
         //Efetua a primitiva receive
         BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         System.out.println("FROM SERVER: " + entrada.readLine());

         //Efetua a primitiva close
         socket.close();
      }catch (Exception e){

      }finally{
            try {
               if (socket != null) {
                  socket.close();
               }
            }catch (Exception e) {
               e.printStackTrace();
            }
      }
   }
}
