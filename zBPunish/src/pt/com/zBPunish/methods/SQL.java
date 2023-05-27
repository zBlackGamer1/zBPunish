package pt.com.zBPunish.methods;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.scheduler.BukkitRunnable;

import pt.com.zBPunish.Main;
import pt.com.zBPunish.methods.Historico.HistoricAction;
import pt.com.zBPunish.methods.Historico.HistoricType;
import pt.com.zBPunish.utils.zBUtils;

public class SQL {
	public Connection con = null;
	private String url;
	private String user;
	private String password;
	
	public void Iniciar() {
		Main.SQLLoaded = false;
		String adress = Main.getInstance().getConfig().getString("MySQL.address");
		String database = Main.getInstance().getConfig().getString("MySQL.database");
		url = "jdbc:mysql://" + adress + "/" + database;
		user = Main.getInstance().getConfig().getString("MySQL.username");
		password = Main.getInstance().getConfig().getString("MySQL.password");
		if (openMySQL()) {
			Main.SQLLoaded = true;
			zBUtils.ConsoleMsg("&7[&bzBPunish&7] &aConexão ao MySQL foi realizada.");
			loadDados();
		} else {
			if (openSQLite()) loadDados();
			else return;
		}
		AutoSaveTimer();
	}

	public void Encerrar() {
		close();
		saveDados();
	}

	private Boolean openMySQL() {
		if (Main.getInstance().getConfig().getBoolean("MySQL.ativo")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(url, user, password);
				createtable();
				return true;
			} catch (SQLException | ClassNotFoundException e) {}
		}
		return false;
	}

	private Boolean openSQLite() {
		File file = new File(Main.getInstance().getDataFolder(), "database.db");
		String url = "jdbc:sqlite:" + file;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			createtable();
			Main.SQLLoaded = true;
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("§6[zBPunish] §cNão foi poss§vel conectar com o banco de dados.");
		}
		return false;
	}

	private void close() {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
			}
		}
	}

	private void createtable() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `allpunishes` (`id` INTEGER, `type` TEXT, `motivo` TEXT, `provas` TEXT,"
					+ " `target` TEXT, `autor` TEXT, `datapunish` DATE, `dataexpire` DATE, `unpunished` BOOLEAN)");
			stm.execute();
			stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `punisheshistoric` (`data` DATE, `id` INTEGER, `type` TEXT, `autor` TEXT)");
			stm.execute();
			stm.close();
		} catch (SQLException e) {
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("&7[&bzBPunish&7] &cOcorreu um erro ao criar tabela no banco de dados.");
		}
	}

	private void loadDados() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `allpunishes`");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				new Punish(rs.getInt("id"), PunishType.valueOf(rs.getString("type")), rs.getDate("datapunish"), rs.getDate("dataexpire"),
						rs.getString("motivo"), rs.getString("autor"), rs.getString("target"), rs.getString("provas"), rs.getBoolean("unpunished"));
			}

			stm = con.prepareStatement("SELECT * FROM `punisheshistoric`");
			rs = stm.executeQuery();
			while (rs.next()) {
				new HistoricAction(Punish.getPunish(rs.getInt("id")), rs.getDate("data"), HistoricType.valueOf(rs.getString("type")), rs.getString("autor"));
			}
		} catch (SQLException e) {
		}
		close();
	}

	private void saveDados() {
		if (!openMySQL()) {
			openSQLite();
		}
		for (int ID : Main.cache.todas.keySet()) {
			Punish p = Punish.getPunish(ID);
			if (!TableContains(ID)) {
				CreatePunish(p);
			} else {
				PreparedStatement stm = null;
				try {
					stm = con.prepareStatement("UPDATE `allpunishes` SET `provas` = ? WHERE `id` = ?");
					stm.setInt(2, ID);
					stm.setString(1, p.getProva());
					stm.executeUpdate();

					stm = con.prepareStatement("UPDATE `allpunishes` SET `unpunished` = ? WHERE `id` = ?");
					stm.setInt(2, ID);
					stm.setBoolean(1, p.isUnpunished());
					stm.executeUpdate();
				} catch (SQLException e) {
				}
			}
		}
		for (HistoricAction h : Main.cache.historico.values()) {
			if (!HistoricContains(h)) {
				CreateHistoric(h);
			}
		}
		close();
	}

	private Boolean TableContains(int ID) {
		PreparedStatement stm = null;
		Boolean b = false;
		try {
			stm = con.prepareStatement("SELECT * FROM `allpunishes` WHERE `id` = ?");
			stm.setInt(1, ID);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				b = true;
			}
		} catch (SQLException e) {
		}
		return b;
	}

	private Boolean HistoricContains(HistoricAction h) {
		PreparedStatement stm = null;
		Boolean b = false;
		try {
			stm = con.prepareStatement("SELECT * FROM `punisheshistoric` WHERE `data` = ?");
			stm.setDate(1, new java.sql.Date(h.getDate().getTime()));
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				b = true;
			}
		} catch (SQLException e) {
		}
		return b;
	}

	private void CreatePunish(Punish pun) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("INSERT INTO `allpunishes` (`id`, `type`, `motivo`, `provas`, `target`, `autor`, `datapunish`,"
					+ " `dataexpire`, `unpunished`) VALUES (?,?,?,?,?,?,?,?,?)");
			stm.setInt(1, pun.getPunishID());
			stm.setString(2, pun.getPunishType().toString());
			stm.setString(3, pun.getMotivo());
			stm.setString(4, pun.getProva());
			stm.setString(5, pun.getPunished());
			stm.setString(6, pun.getPunisher());
			stm.setDate(7, new java.sql.Date(pun.getPunishDate().getTime()));
			if(pun.getExpireDate() != null) stm.setDate(8, new Date(pun.getPunishDate().getTime()));
			else stm.setDate(8, new Date(pun.getPunishDate().getTime() + 1L));
			stm.setBoolean(9, pun.isUnpunished());

			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}
	
	private void CreateHistoric(HistoricAction h) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("INSERT INTO `punisheshistoric` (`data`, `id`, `type`, `autor`) VALUES (?,?,?,?)");
			stm.setDate(1, new java.sql.Date(h.getDate().getTime()));
			stm.setInt(2, h.getPunish().getPunishID());
			stm.setString(3, h.getType().toString());
			stm.setString(4, h.getAuthor());
			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}

	private void AutoSaveTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				AutoSave();
			}
		}.runTaskTimer(Main.getInstance(), 1200 * 30L, 1200 * 30L);
	}

	private void AutoSave() {
		saveDados();
		zBUtils.ConsoleMsg("&7[&bzBPunish&7] &aAuto-Save completo.");
	}
}

