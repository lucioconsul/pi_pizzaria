package util;

import controle.AcessosControle;
import entidade.Acessos;
import entidade.Usuario;
import java.io.IOException;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(servletNames = {"Faces Servlet"})
public class LoginFilter implements Filter {

    private AcessosControle aceC;
    private String url;
    private Usuario usu;

    public AcessosControle getAceC() {
        return aceC;
    }

    public void setAceC(AcessosControle aceC) {
        this.aceC = aceC;
    }

    public String getUrl() {
        if (usu == null) {
            usu = new Usuario();
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Usuario getUsu() {
        return usu;
    }

    public void setUsu(Usuario usu) {
        this.usu = usu;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("passou");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

//##############################################################################            
        //se está logado e tentando navegar
        if (session.getAttribute("autenticado") != null && session.getAttribute("pagina") != null) {
            boolean flag = false;
            HttpServletResponse res = (HttpServletResponse) response;
            //pega o usuario logado
            usu = (Usuario) session.getAttribute("autenticado");
            //pega a url do redirecionamento
            url = session.getAttribute("pagina").toString();
            //percorre acessos do usuario. 
            for (Acessos acess : usu.getPerfil().getAcessos()) {
                //se o usuario tem o acesso à pagina, GO!
                if (acess.getPagina().equals(url)) {
                    chain.doFilter(request, response);
                //se nao tem acesso à pagina, manda mensagem e vai pra index
                } else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Sem permisão!"));
                    res.sendRedirect("index.faces");
                }
            }
        //se está na index, GO! 
        } else if (req.getRequestURI().endsWith("index.faces")) {
            chain.doFilter(request, response);
        //se nao esta logado e tenta uma url manda pra index
        } else {
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect("index.faces");
        }




    }

    @Override
    public void destroy() {
    }
}
