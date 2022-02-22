package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class ArticoloDAO {

	public List<Articolo> list() {

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				// STRATEGIA EAGER FETCHING
				ResultSet rs = s.executeQuery("select * from articolo a inner join negozio n on n.id=a.negozio_id")) {

			while (rs.next()) {
				Articolo articoloTemp = new Articolo();
				articoloTemp.setNome(rs.getString("NOME"));
				articoloTemp.setMatricola(rs.getString("matricola"));
				articoloTemp.setId(rs.getLong("a.id"));

				Negozio negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("n.id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				articoloTemp.setNegozio(negozioTemp);
				result.add(articoloTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public Articolo selectById(Long idArticoloInput) {

		if (idArticoloInput == null || idArticoloInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a where a.id=?")) {

			ps.setLong(1, idArticoloInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Articolo articoloInput) {

		if (articoloInput.getNegozio() == null || articoloInput.getNegozio().getId() < 1) {
			throw new RuntimeException("Input non valido");
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("INSERT INTO articolo (nome, matricola,negozio_id) VALUES (?, ?, ?)")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public Articolo selectByIdWithJoin(Long idInput) {
		if (idInput == null || idInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"select * from articolo a inner join negozio n on a.negozio_id = n.id where a.id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setId(rs.getLong("a.id"));
					result.setNome(rs.getString("a.nome"));
					result.setMatricola(rs.getString("a.matricola"));
					result.setNegozio(
							new Negozio(rs.getLong("n.id"), rs.getString("n.nome"), rs.getString("n.indirizzo")));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int update(Articolo articoloInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("UPDATE articolo SET nome=? , matricola=? WHERE id = ?")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Articolo articoloInput) {
		if (articoloInput == null || articoloInput.getId() < 1)

		{
			throw new RuntimeException("Input non valido");
		}

		int result = 0;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM articolo WHERE id = ?")) {

			ps.setLong(1, articoloInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// implementare inoltre
	public List<Articolo> findAllByNegozio(Negozio negozioInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			throw new RuntimeException("Input non valido");
		}
		List<Articolo> result = new ArrayList<>();
		Articolo articoloTemp = null;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"select * from negozio inner join articolo on negozio.id = articolo.id where a.negozio_id =?");) {

			ps.setLong(1, negozioInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					articoloTemp = new Articolo();
					articoloTemp.setNome(rs.getString("NOME"));
					articoloTemp.setMatricola(rs.getString("MATRICOLA"));
					articoloTemp.setId(rs.getLong("id"));
					result.add(articoloTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Articolo> findAllByMatricola(String matricolaInput) {

		List<Articolo> result = new ArrayList<>();
		Articolo articoloTemp = null;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from  articolo where matricola =?");) {

			ps.setString(1, matricolaInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					articoloTemp = new Articolo();
					articoloTemp.setNome(rs.getString("NOME"));
					articoloTemp.setMatricola(rs.getString("MATRICOLA"));
					articoloTemp.setId(rs.getLong("id"));
					result.add(articoloTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Articolo> findAllByIndirizzoNegozio(String indirizzoNegozioInput) {
		List<Articolo> result = new ArrayList<>();
		Articolo articoloTemp = null;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"select * from  articolo inner join negozio on articolo.negozio_id = negozio.id where indirizzo =?");) {

			ps.setString(1, indirizzoNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					articoloTemp = new Articolo();
					articoloTemp.setNome(rs.getString("NOME"));
					articoloTemp.setMatricola(rs.getString("MATRICOLA"));
					articoloTemp.setId(rs.getLong("id"));
					result.add(articoloTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

}
