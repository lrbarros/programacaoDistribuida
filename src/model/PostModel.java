/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entities.Post;
import entities.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author jose
 */
public class PostModel extends Dao {

    public boolean cadastrarPost(Post p) {
        if (p == null) {
            Dao.setMensagem("Erro. É necessário informar uma publicação!");
            return false;
        }

        System.out.println("CHEGOU ATÉ A PERSISTÊNCIA");

        EntityManager em = Dao.getManager();

        try {
            em.getTransaction().begin();
            Usuario usu = em.find(Usuario.class, p.getPublicacaoUsuario().getId());
            p.setPublicacaoUsuario(usu);
            em.persist(p);
            System.out.println("dentro do persist " + p.getTexto());
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
            System.out.println("ERRO " + e.getMessage());
            return false;
        }
        em.close();
        return true;
    }

    public boolean cadastrarPost(String titulo, String texto, Usuario usu) {

        if (usu == null || titulo.equals("") || texto.equals("")) {
            Dao.setMensagem("Erro. É necessário informar uma publicação!");
            return false;
        }

        EntityManager em = Dao.getManager();

        Usuario u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.email=:email")
                .setParameter("email", usu.getEmail())
                .getSingleResult();

        Post p = new Post(titulo, texto, u);

        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                em.close();
                Dao.setMensagem("Erro. Não foi possível inserir usuário \n" + e.toString());
                return false;
            }
        }
        em.close();
        return true;
    }

    public List<Post> listar() {
        EntityManager em = Dao.getManager();

        @SuppressWarnings("unchecked")
        List<Post> lista = em.createQuery("SELECT p FROM Post p ORDER BY p.id DESC")
                .getResultList();

        em.close();

        return lista;
    }

    public List<Post> listarPostsUsuario(String emailUsuario) {
        if (emailUsuario.equals("")) {
            Dao.setMensagem("Necessário informar e-mail");
            return null;
        }

        EntityManager em = Dao.getManager();

        @SuppressWarnings("unchecked")
        List<Post> lista = em.createQuery("SELECT p FROM Post p WHERE p.publicacaoUsuario.email = :email")
                .setParameter("email", emailUsuario)
                .getResultList();
        em.close();
        return lista;
    }

    public List<Post> listarPostId(Long id) {
        if (id == null || id <= 0l) {
            Dao.setMensagem("Necessário informar e-mail");
            return null;
        }

        id = id.longValue();

        EntityManager em = Dao.getManager();

        @SuppressWarnings("unchecked")
        List<Post> lista = em.createQuery("SELECT p FROM Post p WHERE p.id = :id")
                .setParameter("id", id)
                .getResultList();
        em.close();
        return lista;
    }
}
