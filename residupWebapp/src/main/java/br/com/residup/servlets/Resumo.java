package br.com.residup.servlets;

import br.com.residup.daos.MoradorDao;
import br.com.residup.daos.OcorrenciaDao;
import br.com.residup.daos.ReservaDao;
import br.com.residup.daos.VisitanteDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/listarResumo"})
public class Resumo extends HttpServlet {

    ReservaDao reservaDao = ReservaDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String action = request.getServletPath();
        var visitantedao = VisitanteDao.getInstance();
        try {
            if(action.equals("/listarResumo") || action.equals("/")){


                String id_morador = String.valueOf(request.getSession().getAttribute("id_morador"));
                List minhasReservas = reservaDao.reservas(Integer.parseInt(id_morador));
                request.setAttribute("minhasReservas", minhasReservas);


                List minhasOcorrencias = OcorrenciaDao.listarDoMorador(Integer.parseInt(id_morador));
                request.setAttribute("minhasOcorrencias", minhasOcorrencias);


                List meusVisitantes = VisitanteDao.listarVisitantes();

                request.setAttribute("meusVisitantes", meusVisitantes);

                request.getRequestDispatcher("resumo.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
