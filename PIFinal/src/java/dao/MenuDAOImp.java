/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidade.Menus;
import entidade.Perfil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author XP-PC
 */
public class MenuDAOImp extends Base_DAO_Imp<Menus, Long> implements MenuDAO {

    @Override
    public Menus pesquisa_Por_Id(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Menus> getTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Menus> pesquisaAcesso(Perfil perfil) {
        //throw new UnsupportedOperationException("Not supported yet.");
        session = (Session) Fabrica_Sessao.abreConexao().openSession();
        Query query = session.createQuery("FROM Menus m JOIN m.perfis p WHERE p.id = :valor");
        query.setLong("valor", perfil.getId());
        List<Menus> menus = query.list();
        session.close();
        return menus;
    }

    @Override
    public List<Menus> pesquisaLikeNome(String menus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
