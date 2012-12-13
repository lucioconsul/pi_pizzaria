/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controle.UsuarioControle;
import dao.UsuarioDAO;
import dao.UsuarioDAOImp;
import entidade.Acessos;
import entidade.Usuario;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author XP-PC
 */
public class FaseListner implements PhaseListener {

    public void afterPhase(PhaseEvent event) {
        Usuario usuario = null;
        UsuarioDAO usuD;
        
        
        FacesContext contexto = event.getFacesContext();                        //contexto atual
        String paginaAtual = contexto.getViewRoot().getViewId();                //página que atualmente está interagindo com o ciclo
        boolean isLoginPage = paginaAtual.lastIndexOf("/logar.xhtml") > -1;     //se pagina atual é de login
        HttpSession sessao = (HttpSession) contexto.                            //sessão atual
                getExternalContext().getSession(false);                         
        
        //se for index indo pro logar, deixar ir livre
        if (paginaAtual.lastIndexOf("index.xhtml") > -1) {
            NavigationHandler nh = contexto.getApplication().getNavigationHandler();
            nh.handleNavigation(contexto, null, "sucesso");
        }else{        
        
                //se tem sessão :a primeira vez que passa por aqui é antes de criar a sessao, entao passa direto.
                //a segunda passada é antes do forward do login, portanto tem sessao mas ninguem logado
                if (sessao != null) {
                    //se tem alguem logado, carrega.
                    usuario = (Usuario) sessao.getAttribute("autenticado");
                    //se ja foi preenchido o campo mas ainda nao carregou na session, pega o usuario direto da jsp
                    if(usuario == null){
                        String senha = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("formulario_login:senha");
                        String login = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("formulario_login:login");
                        usuD = new UsuarioDAOImp();
                        usuario = usuD.pesquisaUsuario(login, senha);
                        sessao.setAttribute("autenticado", usuario);
                    }
                }

                //se é pagina de login e usuario null, volta pro login
                if (isLoginPage && usuario == null){
                    NavigationHandler nh = contexto.getApplication().getNavigationHandler();
                    nh.handleNavigation(contexto, null, "sair");
                }

                // se é nao é a pagina de login e o cara nao ta logado
                else if (!isLoginPage && usuario == null) {
                    // Redireciona o fluxo para a página de login
                    NavigationHandler nh = contexto.getApplication().getNavigationHandler();
                    nh.handleNavigation(contexto, null, "sair");
                } 

                //se ta navedando logado, verifica as permissoes de acessos do usuario
                else if (!isLoginPage && usuario != null) {
                    List<Acessos> acessos = usuario.getPerfil().getAcessos();
                    String decisao = "sair";
                    for (Acessos a : acessos) {
                        if (a.getPagina().equals(paginaAtual)) {
                            decisao = "sucesso";
                        }
                    }
                    NavigationHandler nh = contexto.getApplication().getNavigationHandler();
                    nh.handleNavigation(contexto, null, decisao);
                }
        }
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
