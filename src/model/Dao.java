/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author jose
 */
public abstract class Dao {

    private static String mensagem;

    private static EntityManager manager;

    private static EntityManagerFactory factory;

    public static EntityManager getManager() {
        factory = Persistence.createEntityManagerFactory("ExBlog05PU");
        Dao.manager = factory.createEntityManager();
        return Dao.manager;
    }

    public static void setMensagem(String mensagem){
        Dao.mensagem = mensagem;
    }
    
    public static String getMensagem() {
        return Dao.mensagem;
    }

    public static void closeManager() {
        Dao.manager.close();
        Dao.factory.close();
        manager = null;
        factory = null;
    }
}

