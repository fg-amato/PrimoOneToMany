package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
//import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioDAO {

	public List<Negozio> list() {

		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select * from negozio a ")) {

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public Negozio selectById(Long idNegozioInput) {

		if (idNegozioInput == null || idNegozioInput < 1)
			return null;

		Negozio result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio i where i.id=?")) {

			ps.setLong(1, idNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
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

	public int insert(Negozio negozioInput) {
		if (negozioInput == null) {
			throw new RuntimeException("Input non valido");
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO negozio (nome, indirizzo) VALUES (?, ?)")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int update(Negozio negozioInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			throw new RuntimeException("Input non valido");
		}
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("UPDATE negozio SET nome=? , indirizzo=? WHERE id = ?")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			ps.setLong(3, negozioInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Negozio negozioInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			throw new RuntimeException("Input non valido");
		}

		int result = 0;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM negozio WHERE id = ?")) {

			ps.setLong(1, negozioInput.getId());

			result = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// prende negozioInput e grazie al suo id va sulla tabella articoli e poi
	// ad ogni iterazione sul resultset aggiunge agli articoli di negozioInput
	public void populateArticoli(Negozio negozioInput) {
		if (negozioInput == null) {
			throw new RuntimeException("Input non valido");
		}
		Articolo articoloTemp = null;
		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"select * from negozio n inner join articolo a on n.id = a.negozio_id where n.id = ?");) {

			ps.setLong(1, negozioInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					articoloTemp = new Articolo();
					articoloTemp.setNome(rs.getString("nome"));
					articoloTemp.setMatricola(rs.getString("matricola"));
					articoloTemp.setId(rs.getLong("id"));
					if (!negozioInput.getNome().equals(rs.getString("nome"))
							|| !negozioInput.getIndirizzo().equals(rs.getString("indirizzo"))) {
						throw new RuntimeException("Nome negozio in input o indirizzo non corretti");
					}
					articoloTemp.setNegozio(negozioInput);
					result.add(articoloTemp);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		negozioInput.setArticoli(result);
	}

	// implementare inoltre
	public List<Negozio> findAllByIniziali(String inizialeInput) {
		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio a where a.nome like ?");) {

			ps.setString(1, inizialeInput + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					negozioTemp = new Negozio();
					negozioTemp.setNome(rs.getString("NOME"));
					negozioTemp.setIndirizzo(rs.getString("INDIRIZZO"));
					negozioTemp.setId(rs.getLong("id"));
					result.add(negozioTemp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

}
