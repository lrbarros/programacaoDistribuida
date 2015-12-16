/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jose
 */
@Entity
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    private String texto;
    @ManyToOne(cascade=CascadeType.ALL)
    private Usuario publicacaoUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="postComment")
    private List<Comment> commentPost;

    
    public Post(){
        
    }
    
    public Post(String titulo, String texto, Usuario publicacaoUsuario) {
        this.titulo = titulo;
        this.texto = texto;
        this.publicacaoUsuario = publicacaoUsuario;
    }
    

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getPublicacaoUsuario() {
        return publicacaoUsuario;
    }

    public void setPublicacaoUsuario(Usuario publicacaoUsuario) {
        this.publicacaoUsuario = publicacaoUsuario;
    }

    public List<Comment> getCommentPost() {
        return commentPost;
    }

    public void setCommentPost(List<Comment> commentPost) {
        this.commentPost = commentPost;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Post[ id=" + id + " ]";
    }
    
}
