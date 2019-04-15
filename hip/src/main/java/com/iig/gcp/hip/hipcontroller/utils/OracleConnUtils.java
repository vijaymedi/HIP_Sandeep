package com.iig.gcp.hip.hipcontroller.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OracleConnUtils {

	private static String oracle_ip_port;

	@Value("${oracle.ip.port.sid}")
	public void setSid(String value) {
		OracleConnUtils.oracle_ip_port = value;
	}

	private static String oracle_jdbc_url;

	@Value("${oracle.jdbc.url}")
	public void setJdbcUrl(String value) {
		OracleConnUtils.oracle_jdbc_url = value;
	}

	private static String oracle_user_name;

	@Value("${oracle.user.name}")
	public void setDBName(String value) {
		OracleConnUtils.oracle_user_name = value;
	}

	private static String oracle_decrypt_pwd;

	@Value("${oracle.encrypt.pwd}")
	public void setPassword(String value) {
		OracleConnUtils.oracle_decrypt_pwd = value;
	}

	private static String master_key_path;

	@Value("${master.key.path}")
	public void setMasterKeyPath(String value) {
		OracleConnUtils.master_key_path = value;
	}
    /**
     * 
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception
     */
	public static Connection getOracleConnection() throws ClassNotFoundException, SQLException, Exception {
		String content = EncryptionUtil.readFile(master_key_path);
		String connectionUrl = oracle_jdbc_url.replaceAll("#orcl_ip", oracle_ip_port);
		byte[] base_pwd = org.apache.commons.codec.binary.Base64.decodeBase64(oracle_decrypt_pwd);
		String orcl_decoded_pwd = EncryptionUtil.decryptText(base_pwd, EncryptionUtil.decodeKeyFromString(content));
		Connection conn = DriverManager.getConnection(connectionUrl, oracle_user_name, orcl_decoded_pwd);
		return conn;
	}

}