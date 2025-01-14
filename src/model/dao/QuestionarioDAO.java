/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import com.toedter.calendar.JDateChooser;
import connection.ConnectionFactory;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import model.bean.Questionario;

/**
 *
 * @author Matheus
 */
public class QuestionarioDAO {
    
    AnimalDAO adao = new AnimalDAO();

    public ArrayList<Questionario> getQuestionario(int id) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Questionario> questionarios = new ArrayList<Questionario>();
        conexao = ConnectionFactory.conector();
        String sql = "select * from tbl_perguntas where idperguntas = " + id;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                questionarios.add(new Questionario(rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getBoolean(9), rs.getInt(10), rs.getDate(11), adao.getAnimal(id,rs.getDate(11))));
            }
            rs.close();
            pst.close();
            conexao.close();
            return questionarios;
        } catch (Exception e) {
            System.out.println("erro getQuestionario");
            return null;
        }
    }

    public boolean cadastrarQuestionario(int id, JCheckBox pergunta1, JCheckBox pergunta1p2, JCheckBox pergunta2, JCheckBox pergunta2p1, JSpinner pergunta2p2, JCheckBox pergunta3, JCheckBox pergunta3p1, JCheckBox pergunta4, JCheckBox pergunta5, JDateChooser cadastrarData, Component componente){
        
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        String sql = "INSERT INTO tbl_perguntas (idperguntas,cisterna,cisternaconsumo,cxdagua,tampada,capacidade,pcartesiano,pococonsumo,fseptica,animais,datapergunta) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setBoolean(2, pergunta1.isSelected());
            pst.setBoolean(3, pergunta1p2.isSelected());
            pst.setBoolean(4, pergunta2.isSelected());
            pst.setBoolean(5, pergunta2p1.isSelected());
            pst.setInt(6, Integer.parseInt(pergunta2p2.getValue().toString()));
            pst.setBoolean(7, pergunta3.isSelected());
            pst.setBoolean(8, pergunta3p1.isSelected());
            pst.setBoolean(9, pergunta4.isSelected());
            pst.setBoolean(10, pergunta5.isSelected());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            pst.setString(11, dt.format(cadastrarData.getDate()));
            pst.executeUpdate();
            pst.close();
            conexao.close();
            return true;
        } catch (SQLException ex) {
            if(ex.toString().contains("java.sql.SQLIntegrityConstraintViolationException")){
                JOptionPane.showMessageDialog(componente, "Já existe um formulário com esse endereço para esta mesma data, é possível editá-lo na aba respectiva.");
                return false;
            } else {
                JOptionPane.showMessageDialog(componente, "Falha na conexão com o banco de dados, tente novamente.");
                return false;
            }
        } 
    }
    
    public boolean editarQuestionario(int id, JCheckBox pergunta1, JCheckBox pergunta1p2, JCheckBox pergunta2, JCheckBox pergunta2p1, JSpinner pergunta2p2, JCheckBox pergunta3, JCheckBox pergunta3p1, JCheckBox pergunta4, JCheckBox pergunta5, JDateChooser cadastrarData, String data, Component componente) {
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "UPDATE tbl_perguntas SET idperguntas=?,cisterna=?,cisternaconsumo=?,cxdagua=?,tampada=?,capacidade=?,pcartesiano=?,pococonsumo=?,fseptica=?,animais=?,datapergunta='"+dt.format(cadastrarData.getDate())+"' where idperguntas="+id+" and datapergunta = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setBoolean(2, pergunta1.isSelected());
            pst.setBoolean(3, pergunta1p2.isSelected());
            pst.setBoolean(4, pergunta2.isSelected());
            pst.setBoolean(5, pergunta2p1.isSelected());
            pst.setInt(6, Integer.parseInt(pergunta2p2.getValue().toString()));
            pst.setBoolean(7, pergunta3.isSelected());
            pst.setBoolean(8, pergunta3p1.isSelected());
            pst.setBoolean(9, pergunta4.isSelected());
            pst.setBoolean(10, pergunta5.isSelected());
            pst.setString(11, data);
            pst.executeUpdate();
            pst.close();
            conexao.close();
            return true;
        } catch (SQLException ex) {
            if(ex.toString().contains("java.sql.SQLIntegrityConstraintViolationException")){
                JOptionPane.showMessageDialog(componente, "Já existe um formulário com esse endereço para esta mesma data, é possível editá-lo na aba respectiva.");
                return false;
            } else {
                JOptionPane.showMessageDialog(componente, "Falha na conexão com o banco de dados, tente novamente.");
                return false;
            }
        }
    }
    
    public boolean deletarQuestionario(int id, JDateChooser cadastrarData){
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ConnectionFactory.conector();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        
        String sql= "delete from tbl_perguntas where idperguntas=? and datapergunta=?";
        String sql1 = "delete from tbl_animais where idanimais=? and dataanimais=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, dt.format(cadastrarData.getDate()));
            pst.executeUpdate();
            
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, dt.format(cadastrarData.getDate()));
            pst.executeUpdate();
            
            pst.close();
            conexao.close();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(QuestionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
