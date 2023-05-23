package br.com.residup.servlets;

import br.com.residup.daos.LoginDao;
import br.com.residup.models.Morador;
import br.com.residup.models.IconAlertJS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static br.com.residup.shared.Uteis.scriptMensagemAlertJs;

@WebServlet("/index")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("cpf") != null) {
            response.sendRedirect(request.getContextPath() + "/Ocorrencia");
        } else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        Morador morador = new Morador(cpf, senha);

        LoginDao loginDao = LoginDao.getInstance();
        boolean loginValido = loginDao.logar(morador);

        if (loginValido) {
            HttpSession session = request.getSession();
            session.setAttribute("cpf", cpf);
            session.setAttribute("id_morador", loginDao.recuperarId(cpf));

            if (loginDao.validaPrimeiroAcesso(cpf)) {
                response.sendRedirect(request.getContextPath() + "/visitantes");
                String scriptMensagem = scriptMensagemAlertJs
                        (IconAlertJS.warning, "Sua senha é a padrão fornecida pelo síndico.",
                                "Para sua segurança, altere a senha!");

                request.getSession().setAttribute("primeiroAcesso", scriptMensagem);
                request.getSession().setAttribute("primeiroAcessoFlag", true);
                System.out.println("ok");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/Ocorrencia");
                return;
            }

        } else {
            System.out.println("Login não encontrado/incorreto");
            request.setAttribute("error", "CPF e/ou senha incorretos.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }

    }

}
