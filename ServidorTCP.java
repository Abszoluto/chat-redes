import java.io.*;
import java.net.*;

class ServidorTCP
{
   private static int portaServidor = 6789;

   public static void main(String argv[]) throws Exception
   {  
      ServerSocket socket = new ServerSocket(portaServidor);
      String sendString;
      String inputLine;

      while(true)
      {
            //Efetua a primitiva accept
            Socket conexao = socket.accept();

            //Efetua a primitiva receive
            System.out.println("Aguardando datagrama do cliente....");
            BufferedReader entrada =  new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            inputLine = entrada.readLine();
            //Operacao com os dados recebidos e preparacao dos a serem enviados
            System.out.println("Received: " + inputLine + "\t");
            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeBytes(inputLine);
            conexao.close();
      }
   }
}
