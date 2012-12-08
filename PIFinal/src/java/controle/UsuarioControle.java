/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.UsuarioDAO;
import dao.UsuarioDAOImp;
import entidade.Pessoa;
import entidade.Usuario;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aluno
 */
@ManagedBean(name = "usuC")
@SessionScoped
public class UsuarioControle {

    private Usuario usu;
    private UsuarioDAO udao;
    private List menus;
//##############################################################################    

    public Usuario getUsu() {
        if (usu == null) {
            usu = new Usuario();
        }
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    public UsuarioDAO getUdao() {
        return udao;
    }

    public void setUdao(UsuarioDAO udao) {
        this.udao = udao;
    }

    public List getMenus() {
        return menus;
    }

    public void setMenus(List menus) {
        this.menus = menus;
    }
//##############################################################################    

    public String entrar() {
        //entra na pagina padrão
        String retorno = "admin.faces";
        udao = new UsuarioDAOImp();
        FacesContext context = FacesContext.getCurrentInstance();
        //pega o usuario qu está logando
        usu = udao.pesquisaUsuario(usu.getLogin(), usu.getSenha());
        //se login incorredo, dá aviso e volta pra index
        if (usu == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Login inexistente!"));
            limpar();
            retorno = "index.faces";
        //se logou, autentica no filtro e carrega menus do cara
        } else {
            HttpSession session = (HttpSession) FacesContext.
                    getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("autenticado", usu);
            session.setAttribute("pagina", retorno);
            menus = usu.getPerfil().getMenus();
        }
        return retorno;
    }
//##############################################################################        

    private void limpar() {
        usu = null;
        menus = null;
    }
}
