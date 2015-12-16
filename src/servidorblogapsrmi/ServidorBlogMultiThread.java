package servidorblogapsrmi;

import entities.Comment;
import entities.Post;
import entities.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CommentModel;
import model.Dao;
import model.PostModel;
import model.UsuarioModel;

/**
 *
 * @author hugo / Luiz
 */
public class ServidorBlogMultiThread extends Thread implements FuncoesBlog {

    //Luiz e Bruna// 
    //criação dos caminhos do sistema
    private String path0 = new File("").getAbsolutePath().replace("\\", "\\\\");
    // nesta variavel é definida a pasta de atuação da segurança do policy
    private String pathClass = path0; //+ "\\\\build\\\\classes\\\\servidorblogapsrmi";

    private String pathPolicy = new File("").getAbsolutePath().replace("\\", "\\\\") + "\\\\server.policy";

    java.net.InetAddress i;
    String ipServidor;

    private String HEADER = iniciaHeader();

    private String FOOTER = "permission java.util.PropertyPermission \"ANTLR_DO_NOT_EXIT\", \"read\";"
            + "permission java.util.PropertyPermission \"ANTLR_USE_DIRECT_CLASS_LOADING\", \"read\";};";

    @Override
    public void run() {
        ServidorBlogMultiThread s = new ServidorBlogMultiThread();

        try {
            primeiroBoot();

            iniciaSeguranca();

            FuncoesBlog canal = (FuncoesBlog) UnicastRemoteObject.exportObject(s, 0);

            Registry registro = LocateRegistry.createRegistry(1099);

            registro.bind("blog", canal);

            System.out.println("Servidor pronto!");
            System.out.println("\nEndereco do Servidor:\n"
                    + ipServidor);

        } catch (RemoteException | AlreadyBoundException ex) {
            System.out.println("Erro ServidorBlogMultThread metodo run() \n" + ex);
            Logger.getLogger(ServidorBlogMultiThread.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    //Bruna e Luiz
    //Implementação Metodos Manipuladores polices
    @Override
    public List<String> listarRegras() throws RemoteException {

        List<String> listaRegras;
        BufferedReader buffRead;
        String linha = "";

        try {
            listaRegras = new ArrayList();
            buffRead = new BufferedReader(new FileReader(this.pathPolicy));

            while (buffRead.ready()) {

                linha = buffRead.readLine();
                listaRegras.add(linha);

            }
            buffRead.close();

        } catch (IOException ex) {
            Logger.getLogger(ServidorBlogMultiThread.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception LISTAR_REGRAS");
            return null;
        }

        return listaRegras;
    }

    //Grava as regras contidas na lista de string cada item da lista é uma regra
    //faz uma checagem redundante quanto a existencia do arquivo
    @Override
    public boolean gravarArquivo(List<String> regras) throws RemoteException {

        try {

            File file = new File(pathPolicy);

            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter(file, false));

            writer.write(HEADER);
            writer.newLine();

            if (regras != null) {
                for (String r : regras) {
                    writer.write(r);
                    writer.newLine();
                }
            }
            writer.write(FOOTER);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorBlogMultiThread.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;

    }

    @Override
    public void iniciaSeguranca() throws RemoteException {
         //Alteração Luiz e Bruna

        //verifica e armazena  ip atual do servidor
        //se ficasse como local host acontece um erro no momento que o cliente 
        //requisita por todas as regras
        try {
            this.i = java.net.InetAddress.getLocalHost();
            this.ipServidor = i.getHostAddress();
            /*
             Esta propriedade especifica os locais a partir dos quais as classes que são publicados por este VM
             */
            System.setProperty("java.rmi.codebase", pathClass);

            /* 
             Define o caminho do arquivo .policy no qual serão definidas as permissões 
             */
            System.setProperty("java.security.policy", pathPolicy);

            /* 
             O valor desta propriedade representa a seqüência de nome de host, o valor default é o IP, 
             porem, no Notebook que foi utilizado tinha uma configuração no arquivo hosts do computador 
             que alterava o hostname, por isso teve que ser usado 
             */
            //CASO PARA TESTE PODE-SE USAR NO LUGAR DE ipServidor 127.0.0.1, tambem digite 127.0.0.1 no cliente
            System.setProperty("java.rmi.server.hostname", ipServidor);

            // reinicia as configurações em tempo de execução
            Policy.getPolicy().refresh();

            /* 
             SecurityManager para conceder ou negar acesso a recursos. Que sao definidos a partir do arquivo.policy 
             */
            System.setSecurityManager(new SecurityManager());

        } catch (Exception ex) {
            System.out.println("Erro ao verificar ip do servidor ou Iniciar Seguranca \n" + ex.getMessage());
            Logger.getLogger(ServidorBlogMultiThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
// no metodo primeiro boot verifica-se a existencia do arquivo caso nao exista ele o cria e 
    //chama o metodo gravarArquivo que grava o arquivo com as configurações basicas porque recebeu uma lista vazia

    @Override
    public void primeiroBoot() throws RemoteException {
        try {
            List<String> regras = new ArrayList();
            this.i = java.net.InetAddress.getLocalHost();
            this.ipServidor = i.getHostAddress();

            File file = new File(pathPolicy);

            if (!file.exists()) {
                file.createNewFile();
                gravarArquivo(regras);
            }

        } catch (Exception ex) {
            System.out.println("Erro primeiro BOOT");
        }

    }

//    //Este metodo inicia o cabeçalho do arquivo police com as regras basicas de liberação
//    para que o servidor possa acessar os metodos inicialmente e liberar as permissoes para
//    os demais endereços da rede, ele pega ip atual do servidor e passa para o arquivo para liberalo
    private String iniciaHeader() {
        try {
            this.i = java.net.InetAddress.getLocalHost();
            this.ipServidor = i.getHostAddress();

        } catch (Exception e) {
            System.out.println("Erro ao iniciar HEADER");
        }

        return "grant{permission java.net.SocketPermission \"192.168.43.200:1099\", \"accept, resolve\";"
                + "permission java.net.SocketPermission \"192.168.43.200:1024-65335\", \"accept, resolve\";"
                + "permission java.io.FilePermission \"" + pathPolicy + "\",\"read, write, execute\";"
                + "permission java.lang.RuntimePermission \"setSecurityManager\";"
                + "permission java.lang.RuntimePermission \"createSecurityManager\";"
                + "permission java.util.PropertyPermission \"java.rmi.codebase\", \"write\";"
                + "permission java.util.PropertyPermission \"java.security.policy\", \"write\";"
                + "permission java.util.PropertyPermission \"java.rmi.server.hostname\", \"write\";"
                + "permission java.security.SecurityPermission \"getPolicy\";"
                + "permission java.lang.RuntimePermission \"*\";"
                + "permission java.io.FilePermission \"<<ALL FILES>>\",\"read,execute\";"
                + "permission java.util.PropertyPermission \"hibernate.enable_specj_proprietary_syntax\", \"read,write\";"
                + "permission java.net.SocketPermission \"127.0.0.1:1-65535\",\"connect,resolve,accept\";"
                + "permission java.lang.reflect.ReflectPermission \"suppressAccessChecks\";";
        

    }

    @Override
    public boolean cadastrar(String comment, Long id) throws RemoteException {
        CommentModel model = new CommentModel();
        return model.cadastrar(comment, id);
    }

    @Override
    public List<Comment> listar(Long idPost) throws RemoteException {
        CommentModel model = new CommentModel();
        return model.listar(idPost);

    }

    @Override
    public boolean cadastrarPost(Post p) throws RemoteException {
        PostModel model = new PostModel();
        System.out.println("print post"+p.getTexto());
        System.out.println("print post"+p.getTitulo());
        System.out.println("print post"+p.getPublicacaoUsuario());
        return model.cadastrarPost(p);
    }

    @Override
    public boolean cadastrarPost(String titulo, String texto, Usuario usu) throws RemoteException {
        PostModel model = new PostModel();
        return model.cadastrarPost(titulo, texto, usu);
    }

    @Override
    public List<Post> listar() throws RemoteException {
        PostModel model = new PostModel();
        System.out.println("Foi realizada a consulta...");
        return model.listar();
    }

    @Override
    public List<Post> listarPostsUsuario(String emailUsuario) throws RemoteException {
        PostModel model = new PostModel();
        return model.listarPostsUsuario(emailUsuario);
    }

    @Override
    public List<Post> listarPostId(Long id) throws RemoteException {
        PostModel model = new PostModel();
        return model.listarPostId(id);
    }

    @Override
    public boolean cadastrarUsuario(Usuario usu) throws RemoteException {
        UsuarioModel model = new UsuarioModel();
        return model.cadastrarUsuario(usu);
    }

    @Override
    public Usuario validarAcesso(String email, String senha) throws RemoteException {
        UsuarioModel model = new UsuarioModel();
        return model.validarAcesso(email, senha);
    }

    @Override
    public void setMensagem(String mensagem) throws RemoteException {
        Dao.setMensagem(mensagem);
    }

    @Override
    public String getMensagem() throws RemoteException {
        return Dao.getMensagem();
    }

}
