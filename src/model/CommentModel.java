

package model;

import entities.Comment;
import entities.Post;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;



public class CommentModel extends Dao{
    
    public boolean cadastrar(String comment, Long id){
        if(comment.equals("") || id ==  null || id <= 0l){
            Dao.setMensagem("Informe os dados corretamente!!!");
            return false;
        }
        
        EntityManager em = Dao.getManager();
        
        Post p = (Post) em.createQuery("SELECT p FROM Post p WHERE id=:id")
                .setParameter("id", id)
                .getSingleResult();
        
        Date date = new Date();
        
        Comment c = new Comment(comment, p, date);
        
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
                em.close();
                Dao.setMensagem("Erro. Não foi possível gravar comentário \n"+e.toString());
                return false;
            }
        }
        em.close();
        return true;
    }    
    
    public List<Comment> listar(Long idPost){
        if(idPost == null || idPost <= 0l){
            Dao.setMensagem("Informe o id do Post");
            return null;
        }
        
        EntityManager em = Dao.getManager();
        
        @SuppressWarnings("unchecked")
        List<Comment> lista = em.createQuery("SELECT c FROM Comment c WHERE c.postComment.id = :id ORDER BY c.id DESC")
                .setParameter("id", idPost)
                .getResultList();
        
        em.close();
        
        return lista; 
    }
}
