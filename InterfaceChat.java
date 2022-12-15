import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import javax.swing.JFileChooser;
import java.nio.file.Files;
import javax.swing.filechooser.FileSystemView;

public class InterfaceChat {
    String  appName = "Bate-papo";
    JTextArea  chatBox;
    JButton  enviarMensagemButton;
    JButton  enviarArquivoButton;
    JFrame  preChatInterface;
    JFrame  newFrame = new JFrame(appName);
    InterfaceChat  InterfaceChat;
    JTextField  areaMensagens;
    JTextField  getNomeCliente;
    JFileChooser  enviarArquivo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InterfaceChat InterfaceChat = new InterfaceChat();
                InterfaceChat.setupDisplay();
            }
        });
    }

    public void setupDisplay() {
        newFrame.setVisible(false);
        preChatInterface = new JFrame(appName);
        getNomeCliente = new JTextField(15);
        JLabel nomeUsuarioLabel = new JLabel("Qual é o seu nome ?");
        JButton conectarNoChat = new JButton("Entrar no chat");
        conectarNoChat.addActionListener(new enterServerButtonListener());
        JPanel setupPanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        setupPanel.add(nomeUsuarioLabel, preLeft);
        setupPanel.add(getNomeCliente, preRight);
        preChatInterface.add(setupPanel, BorderLayout.CENTER);
        preChatInterface.add(conectarNoChat, BorderLayout.SOUTH);
        preChatInterface.setSize(300, 300);
        preChatInterface.setVisible(true);

    }

    public void openChat() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());

        JPanel painelChat = new JPanel();
        painelChat.setBackground(Color.BLUE);
        painelChat.setLayout(new GridBagLayout());

        areaMensagens = new JTextField(30);
        areaMensagens.requestFocusInWindow();
        enviarArquivoButton = new JButton("Enviar arquivo");
        enviarArquivoButton.addActionListener(new sendFileButtonListener());
        enviarMensagemButton = new JButton("Enviar mensagem");
        enviarMensagemButton.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        painelPrincipal.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        painelChat.add(areaMensagens, left);
        painelChat.add(enviarMensagemButton, right);
        painelChat.add(enviarArquivoButton, right);
        painelPrincipal.add(BorderLayout.SOUTH, painelChat);

        newFrame.add(painelPrincipal);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(1000, 780);
        newFrame.setVisible(true);
    }
    class sendFileButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
            int returnValue = enviarArquivo.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try{
                    File selectedFile = enviarArquivo.getSelectedFile();
                    //String conteudoArquivo = Files.readString(selectedFile.toPath());
                    // Enviar arquivo para o servidor utilizando o protocolo TFTP -> UDP
                    byte [] conteudoArquivo = Files.readAllBytes(selectedFile.toPath());
                    ClienteUDP  udpConnection = new ClienteUDP("192.168.18.11",9871);
                    udpConnection.enviarArquivo(conteudoArquivo);
                    chatBox.append("SERVIDOR: <"+apelidoChat +"> enviou um arquivo " + "\""+selectedFile.getName()+"\"." 
                        + "\n");
                areaMensagens.setText("");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
}
    class sendMessageButtonListener implements ActionListener{
        //ClienteUDP  udpConnection = new ClienteUDP("192.168.18.11",9871);
        public void actionPerformed(ActionEvent event) {
            if (areaMensagens.getText().length() < 1) {
                // Cliente não digitou nenhuma mensagem
            } else if (areaMensagens.getText().equals(".clear")) {
                chatBox.setText("Todas as mensagens foram apagadas. \n");
                areaMensagens.setText("");
            } else {
                String mensagemEnviada = areaMensagens.getText();
                chatBox.append("<" + apelidoChat + ">:  " + mensagemEnviada
                        + "\n");
                areaMensagens.setText("");
                try{
                    enviarMensagemTcpServidor(mensagemEnviada);
                }catch(Exception e){
                }
                
            }
            areaMensagens.requestFocusInWindow();
        }

        public void enviarMensagemTcpServidor(String mensagem) throws Exception{
            try{
                //Enviar mensagem de texto no chat para o servidor -> TCP
                ClienteTCP tcpConnection = new ClienteTCP("192.168.18.11", 6789);
                tcpConnection.sendData(mensagem);
            }
            catch(NullPointerException e){
                throw new Exception("Ops, algo deu errado... ");
            }
            
        }
    }
    String  apelidoChat;

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            apelidoChat = getNomeCliente.getText();
            preChatInterface.setVisible(false);
            openChat();
        }

    }
}