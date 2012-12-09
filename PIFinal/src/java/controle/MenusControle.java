/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import entidade.Menus;
import entidade.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.MenuModel;

/**
 *
 * @author XP-PC
 */
@ManagedBean(name = "menuC")
@SessionScoped
public class MenusControle {
    private Usuario usu;
    private Menus menu;
    private MenuModel mModel;

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public Menus getMenu() {
        return menu;
    }

    public void setMenu(Menus menu) {
        this.menu = menu;
    }

    public MenuModel getmModel() {
        return mModel;
    }

    public void setmModel(MenuModel mModel) {
        this.mModel = mModel;
    }
    
    
}
