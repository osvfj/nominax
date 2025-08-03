package com.nominax;

import com.nominax.dao.impl.UsuarioDAOFile;
import com.nominax.entity.Usuario;
import com.nominax.util.ManejadorArchivos;
import com.nominax.view.usuario.Login;
import java.io.IOException;

/**
 *
 * @author osvfj
 */

public class Nominax {

    public static void main(String[] args) {
        try {
            if(!ManejadorArchivos.isDirectorioInicializado()){
                Usuario user = new Usuario("admin", "cambiarclave", 0, "Administrador", "", "admin@nominax.com");
                new UsuarioDAOFile().guardar(user);
            }
            
            Login loginScreen = new Login();
            loginScreen.setVisible(true);
            loginScreen.setLocationRelativeTo(null);     

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}