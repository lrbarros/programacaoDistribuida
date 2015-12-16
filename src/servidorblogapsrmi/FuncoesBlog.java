/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorblogapsrmi;

import entities.Comment;
import entities.Post;
import entities.Usuario;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author hugo\\Luiz
 */
public interface FuncoesBlog extends Remote {
    
    //Interfaces para manipulação de policy
    
  
    public List<String> listarRegras() throws RemoteException;
    public boolean gravarArquivo(List<String>regras)throws RemoteException;   
    public void iniciaSeguranca() throws RemoteException;
    public void primeiroBoot() throws RemoteException;
    
    //Interfaces BLOG BRUNA JOSE
     public boolean cadastrar(String comment, Long id) throws RemoteException;
     public List<Comment> listar(Long idPost) throws RemoteException;
     public boolean cadastrarPost(Post p)throws RemoteException;
     public boolean cadastrarPost(String titulo, String texto, Usuario usu)throws RemoteException;
     public List<Post> listar() throws RemoteException;
     public List<Post> listarPostsUsuario(String emailUsuario) throws RemoteException;
     public List<Post> listarPostId(Long id) throws RemoteException;
     public boolean cadastrarUsuario(Usuario usu) throws RemoteException;
     public Usuario validarAcesso(String email, String senha)throws RemoteException;
     
     public void setMensagem(String mensagem) throws RemoteException;
     public String getMensagem() throws RemoteException;
     
     
     
}