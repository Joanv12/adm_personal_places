<?
	switch($_SERVER['SERVER_NAME']) {
		case "localhost":
			$way = 0;
			break;
		case "angeltools.tk": case "www.angeltools.tk":
			$way = 1;
			break;
	}
	$dbase_params = array(
		0 => array("localhost", "root", "valencia", "adm_personal_places"),
		1 => array("localhost", "root", "valencia", "adm_personal_places"),
	NULL);

	function connect_database() {
		global $dbase_params, $way;
		$dir = dirname(__FILE__);
		$conn = mysql_connect($dbase_params[$way][0], $dbase_params[$way][1], $dbase_params[$way][2]);
		mysql_select_db($dbase_params[$way][3], $conn);
		return $conn;
	}
	$conn = connect_database();
	
	function log_data($file, $text, $perm = "w+") {
		//$text = str_replace("\n", "\r\n", $text);
		$fr = fopen($file, $perm);
		if ($fr) {
			fwrite($fr, $text);
			fclose($fr);
			chmod($file, 0777);
		}
	}
	function echoc($data) {
		echo "\n<pre>\n";
		print_r($data);
		echo "</pre>\n";
	}
	function get_data($table, $field1_name, $field1_data, $field2_name, $cond = "") {
		global $conn;
		if ($cond != "") $cond = " AND $cond";
		$sql = "SELECT `$field2_name` FROM `$table` WHERE `$field1_name` = '$field1_data'$cond";
		$records = mysql_query($sql, $conn);
		if ($record = mysql_fetch_array($records)) return $record[$field2_name];
		else return NULL;
	}
	function set_data($table, $field1_name, $field1_data, $field2_name, $field2_data) {
		global $conn;
		if (get_data($table, $field1_name, $field1_data, $field2_name) == NULL)
			mysql_query("INSERT INTO `$table`(`$field1_name`) VALUES('$field1_data')", $conn);
		$field2_data = ($field2_data != NULL)? "'$field2_data'": "NULL";
		mysql_query("UPDATE `$table` SET `$field2_name` = $field2_data WHERE `$field1_name` = '$field1_data'", $conn);
	}
	function GetSQLValueString($theValue, $theType) {
		$theValue = (!get_magic_quotes_gpc()) ? addslashes($theValue) : $theValue;
		switch ($theType) {
			case "text":
				$theValue = ($theValue != "") ? "'" . $theValue . "'" : "NULL";
				break;    
			case "function":
				$theValue = ($theValue != "") ? $theValue  : "NULL";
				break;    
		}
		return $theValue;
	}
	function array_to_sql_insert($table, $array) {
		$fields = "";
		$values = "";
		foreach ($array as $key=>$value) {
			if (is_array($value)) {
				$type = $value[1];
				$value = $value[0];
			}
			else $type = "text";
			$fields .= "`$key`, ";
			$values .= GetSQLValueString($value, $type) . ", ";
		}
		$fields = substr($fields, 0, -2);
		$values = substr($values, 0, -2);
		return "INSERT INTO `$table`($fields) VALUES($values)";
	}
	function get_username_checker() {
		return "/^[a-zA-Z][a-zA-Z0-9\_\-]{2,19}$/";
	}
	function get_password_checker() {
		return "/^[a-zA-Z0-9]{4,20}$/";
	}
	function get_name_checker() {
		return "/^[^0-9].{0,49}$/";
	}
	function get_gender_checker() {
		return "/^(M|F)$/";
	}
	function get_email_checker() {
		return "/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/";
	}
	function get_phone_checker() {
		return "/^[0-9]{4,20}$/";
	}
	function check_authentication($username, $password) {
		global $conn;
		$sql = "SELECT * FROM `users` WHERE `username` = '$username' AND `password` = '$password'";
		$query = mysql_query($sql, $conn);
		return (mysql_num_rows($query) == 1);
	}
	function register_friendship($username, $target_id, $is_adding) {
		global $conn;
		$username_id = get_data("users", "username", $username, "id");
		$target_id = get_data("users", "id", $target_id, "id");
		if ($username_id != $target_id && $target_id !== NULL) {
			if (intval($username_id) < intval($target_id)) {
				$user1_id = $username_id;
				$user2_id = $target_id;
				$state = 1;
			}
			else {
				$user1_id = $target_id;
				$user2_id = $username_id;
				$state = 2;
			}
			
			$sql = "SELECT `state` FROM `friendships` WHERE `user1_id` = '$user1_id' && `user2_id` = '$user2_id'";
			$query = mysql_query($sql, $conn);
			if (mysql_num_rows($query) == 1) {
				$record = mysql_fetch_object($query);
				$state_db = $record->state;
			}
			else
				$state_db = 0;
			
			if ($is_adding == 1) {
				if ($state_db == 0) {
					$sql = "INSERT INTO `friendships`(`user1_id`, `user2_id`, `state`) VALUES('$user1_id', '$user2_id', '$state')";
					mysql_query($sql, $conn);
				}
				else if (($state_db == 1 && $state == 2) || ($state_db == 2 && $state == 1)) {
					$sql = "UPDATE `friendships` SET `state` = '3' WHERE `user1_id` = '$user1_id' && `user2_id` = '$user2_id'";
					mysql_query($sql, $conn);
				}
			}
			else if ($is_adding == 0) {
				$sql = "DELETE FROM `friendships` WHERE `user1_id` = '$user1_id' && `user2_id` = '$user2_id'";
				mysql_query($sql, $conn);
			}
			
			$status = STATUS_SUCCESS;
		}
	}
?>
