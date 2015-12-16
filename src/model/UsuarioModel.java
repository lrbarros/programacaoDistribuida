/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author jose
 */
public class UsuarioModel extends Dao{
    
    
    public boolean cadastrarUsuario(Usuario usu){
        if(usu == null){
            Dao.setMensagem("Erro. É necessário informar um usuário!");
            return false;
        }
        
        EntityManager em = Dao.getManager();
        
        try {
            em.getTransaction().begin();
            em.persist(usu);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
                em.close();
                Dao.setMensagem("Erro. Não foi possível inserir usuário \n"+e.toString());
                return false;
            }
        }
        em.close();
        return true;
    }
    
    
    public Usuario validarAcesso(String email, String senha){
        if(email.equals("") || senha.equals("")){
            Dao.setMensagem("Erro. Todos os parâmetros devem ser informados");
            return null;
        }
        
        EntityManager em = Dao.getManager();
        String jpql = "SELECT u FROM Usuario u WHERE email = :email AND senha = :senha";
        TypedQuery<Usuario> qry = em.createQuery(jpql, Usuario.class);
        qry.setParameter("email", email);
        qry.setParameter("senha", senha);
        Usuario usu = (Usuario) qry.getSingleResult();
        
        em.close();
        
        return usu;
    }
}
