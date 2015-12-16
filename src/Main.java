
import entities.Usuario;
import model.UsuarioModel;
import servidorblogapsrmi.ServidorBlogMultiThread;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Luiz
 */
public class Main {
      public static void main(String[] args) {
          Usuario usu = new Usuario("luiz","luiz@luiz","123");
          
          
          UsuarioModel model = new UsuarioModel();
           model.cadastrarUsuario(usu);
          
        ServidorBlogMultiThread ss = new ServidorBlogMultiThread();
        ss.start();
    }
}
