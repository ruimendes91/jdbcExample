package dbex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Properties;
import java.util.Scanner;

public class JDBCExample {
	
	private static Connection conn;
	private static Scanner entrada = new Scanner(System.in);

	private static boolean insert(int bi, String nome, String dataNascimento, String morada, String categoria, String funcao) {
		String insert = "INSERT INTO funcionario (bi, nome, dataNascimento, morada, categoria, funcao)" + "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preStatement = conn.prepareStatement(insert);
			preStatement.setInt(1, bi);
			preStatement.setString(2, nome);
			java.sql.Date data = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dataNascimento).getTime()); 
			preStatement.setDate(3, data);
			preStatement.setString(4, morada);
			preStatement.setString(5, categoria);
			preStatement.setString(6, funcao);
			preStatement.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Ocorreu um erro ao inserir.");
			return false;
		} catch (ParseException e) {
			System.err.println("Ocorreu um erro ao inserir. Erro na data.");
			return false;
		}
	}

	private static boolean remove(int bi) {
		String remove = "DELETE FROM funcionario WHERE bi=?";
		try {
			PreparedStatement preStatement = conn.prepareStatement(remove);
			preStatement.setInt(1, bi);
			preStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Ocorreu um erro ao apagar.");
			return false;
		}
	}

	private static boolean update(int bi, String morada) {
		String update = "UPDATE funcionario SET morada=? WHERE bi=?";
		try {
			PreparedStatement preStatement = conn.prepareStatement(update);
			preStatement.setInt(2, bi);
			preStatement.setString(1, morada);
			preStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("Ocorreu um erro ao atualizar.");
			return false;
		}
	}

	private static ResultSet selecionar(int bi) {
		String seleciona = "SELECT bi, nome, morada FROM funcionario WHERE bi=?";
		try {
			PreparedStatement preStatement = conn.prepareStatement(seleciona);
			preStatement.setInt(1, bi);
			return preStatement.executeQuery();
		} catch (SQLException e) {
			System.err.println("Ocorreu um erro ao selecionar.");
			return null;
		}
	}

	private static void menuInserir() {

		int bi;
		System.out.println("Inserir nº do BI: ");
		bi = entrada.nextInt(); entrada.nextLine();
		
		
		String nome;
		System.out.println("Inserir nome: ");
		nome = entrada.nextLine();
		
		String dataNascimento;
		System.out.println("Inserir data nascimento com o formato yyyy-mm-dd: ");
		dataNascimento = entrada.nextLine();
		
		String morada;
		System.out.println("Inserir morada: ");
		morada = entrada.nextLine();
		
		String categoria;
		System.out.println("Inserir categoria: ");
		categoria = entrada.nextLine();
		
		String funcao;
		System.out.println("Inserir função: ");
		funcao = entrada.nextLine();
		
		
		if(insert(bi, nome, dataNascimento, morada, categoria, funcao))
			System.out.println("Funcionário inserido com sucesso!");
		else
			System.out.println("Erro ao inserir funcionário");
		
		menuPrincipal();
		
	}

	private static void menuApagar() {

		
		int bi;
		System.out.println("Inserir nº do BI: ");
		bi = entrada.nextInt(); entrada.nextLine();

		
		if(remove(bi))
			System.out.println("Funcionário removido com sucesso!");
		else
			System.out.println("Erro ao remover funcionário");
		
		menuPrincipal();
	}

	private static void menuUpdate() {
		
		int bi;
		System.out.println("Inserir nº do BI a atualizar: ");
		bi = entrada.nextInt(); entrada.nextLine();
		
		String morada;
		System.out.println("Inserir nova morada: ");
		morada = entrada.nextLine();
		
		if(update(bi, morada))
			System.out.println("Funcionário atualizado com sucesso.");
		else
			System.out.println("Erro ao atualizar funcionário.");
		
	}

	private static void menuSelecionar() {
		int opcao;
		System.out.println("Indique o bi que pretende consultar: ");
		opcao = entrada.nextInt(); entrada.nextLine();
		ResultSet result = selecionar(opcao);
		if(result == null)
			System.out.println("O funcionário indicado não existe.");
		else {
	        try {
				while(result.next()){
				    System.out.println("Result : " + result.getInt("bi") + " " + result.getString("nome")+ " " + result.getString("morada"));

				}
			} catch (SQLException e) {
				System.err.println("Erro ao ler result set.");
			}
		}

	}

	private static void menuPrincipal() {

		int opcao;

		do {
			System.out.println(
					"1 - Inserir \r\n" + "2 - Apagar \r\n" + "3 - Actualizar \r\n" + "4 - Seleccionar \r\n" + "\nOpção: ");
			
			opcao = entrada.nextInt(); entrada.nextLine();

			switch (opcao) {
				case 1:
					menuInserir();
					break;
	
				case 2:
					menuApagar();
					break;
	
				case 3:
					menuUpdate();
					break;
	
				case 4:
					menuSelecionar();
					break;
				default:
					System.out.println("Opção inválida.");
			}
		} while (opcao != 0);

		entrada.close();
	}

	public static void main(String args[]) throws SQLException {
		// Exemplo: MySQL
		String url = "jdbc:mysql://localhost:3306/natixishr";
		Properties props = new Properties();
		props.setProperty("user", "root");
		props.setProperty("password", "");

		conn = DriverManager.getConnection(url, props);
				
		insert(1284567, "José Manuel", "13-11-1991" , "Rua das Flores", "categoria", "funcao");
		insert(7654321, "Mário Silva", "14-11-1991", "Rua das Acácias", "categoria", "funcao");
		insert(5567432, "Tibério D'Almeida", "15-11-1991", "Rua dos Lírios", "categoria", "funcao");
		
		menuPrincipal();

	}
}
