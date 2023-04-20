package br.com.residup.daos;
import br.com.residup.models.Morador;
import br.com.residup.shared.GerenciadorConexaoH2;

import static br.com.residup.shared.GerenciadorConexaoH2.abrirConexao;
import static br.com.residup.shared.GerenciadorConexaoH2.fecharConexao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoradorDao {

    private static MoradorDao instance;
    private Connection connection;
    private MoradorDao() {
        this.handleOpenConnection();
    }
    private void handleOpenConnection() {
        try {
            this.connection = abrirConexao();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MoradorDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MoradorDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static MoradorDao getInstance() {
        if(instance == null) {
            instance = new MoradorDao();
        }
        return instance;
    }

    public static boolean createMorador(Morador morador) {
        boolean retorno = false;
        try (Connection connection = abrirConexao();
             PreparedStatement instrucaoSQL = connection.prepareStatement(
                     "INSERT INTO MORADOR (NOME, SOBRENOME, CPF, RG, NUMERO_APARTAMENTO, BLOCO, SENHA_ACESSO, DATA_INCERCAO) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP )",
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            instrucaoSQL.setString(1, morador.getNome());
            instrucaoSQL.setString(2, morador.getSobrenome());
            instrucaoSQL.setString(3, morador.getCpf());
            instrucaoSQL.setString(4, morador.getRg());
            instrucaoSQL.setString(5, morador.getNumeroApartamento());
            instrucaoSQL.setString(6, morador.getBloco());
            instrucaoSQL.setString(7, morador.getSenhaDeAcesso());

            int linhasRetorno = instrucaoSQL.executeUpdate();

            if (linhasRetorno > 0) {
                retorno = true;

                ResultSet generatedKeys = instrucaoSQL.getGeneratedKeys();
                if (generatedKeys.next()) {
                    morador.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do morador.");
                }
            } else {
                retorno = false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
            return retorno;
        }
    }
