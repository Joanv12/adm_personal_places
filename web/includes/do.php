<?
	//sleep(3);

	header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
	header("Last-Modified: ".gmdate("D, d M Y H:i:s")." GMT");
	header("Cache-Control: no-cache, must-revalidate");
	header("Pragma: no-cache");
	require_once("utils.inc.php");
	require_once("constants.inc.php");
	
	if (isset($_GET["action"])) {
		if (
			 $_GET["action"] == "..." ||
		false) {
			$temp_post = $_POST;
			$_POST = $_GET;
			$_POST["_post_vars"] = $temp_post;
		}
	}
	log_data("log_data.txt", print_r($_POST, true)."\n", "a+");
	log_data("log_data.txt", print_r($_FILES, true)."\n", "a+");
	$action = isset($_POST["action"])? $_POST["action"]: "";
	switch ($action) {
		
		case "login_user":
			$username = isset($_POST["username"])? $_POST["username"]: "";
			$password = isset($_POST["password"])? $_POST["password"]: "";
			if (!preg_match(get_username_checker(), $username))
				$error_reason = ERROR_USERNAME_FORMAT;
			else if (!preg_match(get_password_checker(), $password))
				$error_reason = ERROR_PASSWORD_FORMAT;
			else {
				$sql = "SELECT * FROM `users` WHERE `username` = '$username' AND `password` = '$password'";
				$query = mysql_query($sql, $conn);
				if (mysql_num_rows($query) == 0)
					$error_reason = ERROR_USERNAME_PASSWORD_INVALID;
			}
			$result = array();
			if (!isset($error_reason))
				$result["status"] = STATUS_SUCCESS;
			else {
				$result["status"] = STATUS_ERROR;
				$result["error_reason"] = $error_reason;
			}
			echo json_encode($result);
			break;
		
		case "register_user":
			$username = isset($_POST["username"])? $_POST["username"]: "";
			$password = isset($_POST["password"])? $_POST["password"]: "";
			$password_old = isset($_POST["password_old"])? $_POST["password_old"]: "";
			$name = isset($_POST["name"])? $_POST["name"]: "";
			$gender = isset($_POST["gender"])? $_POST["gender"]: "";
			$email = isset($_POST["email"])? $_POST["email"]: "";
			$phone = isset($_POST["phone"])? $_POST["phone"]: "";
			$country_iso2 = isset($_POST["country"])? $_POST["country"]: "";
			$country_name = get_data("countries", "iso2", $country_iso2, "name");
			if (!preg_match(get_username_checker(), $username))
				$error_reason = ERROR_USERNAME_FORMAT;
			else if ($password_old != "" && !preg_match(get_password_checker(), $password_old))
				$error_reason = ERROR_PASSWORD_FORMAT;
			else if (!preg_match(get_name_checker(), $name))
				$error_reason = ERROR_NAME_FORMAT;
			else if (!preg_match(get_gender_checker(), $gender))
				$error_reason = ERROR_GENDER_FORMAT;
			else if (!preg_match(get_email_checker(), $email))
				$error_reason = ERROR_EMAIL_FORMAT;
			else if (!preg_match(get_phone_checker(), $phone))
				$error_reason = ERROR_PHONE_FORMAT;
			else if ($country_name == NULL)
				$error_reason = ERROR_COUNTRY_INVALID;
			else {
				$user_exists  = (get_data("users", "username",  $username,  "id") != NULL);
				$email_exists = (get_data("users", "email", $email, "id") != NULL);
				if ($password_old == "") { /* registering */
					if (!preg_match(get_password_checker(), $password))
						$error_reason = ERROR_PASSWORD_FORMAT;
					else if ($user_exists)
						$error_reason = ERROR_USERNAME_ALREADY_REGISTERED;
					else if ($email_exists)
						$error_reason = ERROR_EMAIL_ALREADY_REGISTERED;
					else {
						$data = array();
						$data["username"] = $username;
						$data["password"] = $password;
						$data["name"] = $name;
						$data["gender"] = $gender;
						$data["email"] = $email;
						$data["phone"] = $phone;
						$data["country_iso2"] = $country_iso2;
						if (isset($_FILES["image"]["name"]) && $_FILES["image"]["name"] != "") {
							$image_uri = "../images/".$username.".jpg";
							move_uploaded_file($_FILES["image"]["tmp_name"], $image_uri);
							$data["image_uri"] = $username.".jpg";
						}
						$sql = array_to_sql_insert("users", $data);
						mysql_query($sql, $conn);
					}
				}
				else { /* updating */
					if (!preg_match(get_password_checker(), $password_old))
						$error_reason = ERROR_PASSWORD_FORMAT;
					else if ($password != "" && !preg_match(get_password_checker(), $password))
						$error_reason = ERROR_PASSWORD_FORMAT;
					else {
						$sql = "SELECT * FROM `users` WHERE `username` = '$username' AND `password` = '$password_old'";
						$query = mysql_query($sql, $conn);
						if (mysql_num_rows($query) == 0)
							$error_reason = ERROR_USERNAME_PASSWORD_INVALID;
						else {
							$data = array();
							if ($password != "")
								$data["password"] = $password;
							$data["name"] = $name;
							$data["gender"] = $gender;
							$data["email"] = $email;
							$data["phone"] = $phone;
							$data["country_iso2"] = $country_iso2;
							if (isset($_FILES["image"]["name"]) && $_FILES["image"]["name"] != "") {
								$image_uri = "../images/".$username.".jpg";
								move_uploaded_file($_FILES["image"]["tmp_name"], $image_uri);
								$data["image_uri"] = $username.".jpg";
							}
							else {
								$image_uri = "../images/".$username.".jpg";
								if (file_exists($image_uri))
									unlink($image_uri);
								$data["image_uri"] = "";
							}
							$sql = array_to_sql_update("users", $data, "WHERE `username` = '$username' AND `password` = '$password_old'");
							mysql_query($sql, $conn);
						}
					}
				}
			}
			$result = array();
			if (!isset($error_reason))
				$result["status"] = STATUS_SUCCESS;
			else {
				$result["status"] = STATUS_ERROR;
				$result["error_reason"] = $error_reason;
			}
			echo json_encode($result);
			break;

		case "login_user":
			$username = isset($_POST["username"])? $_POST["username"]: "";
			$password = isset($_POST["password"])? $_POST["password"]: "";
			if (!preg_match(get_username_checker(), $username))
				$error_reason = ERROR_USERNAME_FORMAT;
			else if (!preg_match(get_password_checker(), $password))
				$error_reason = ERROR_PASSWORD_FORMAT;
			else if (!check_authentication($username, $password))
				$error_reason = ERROR_USERNAME_PASSWORD_INVALID;
			$result = array();
			if (!isset($error_reason))
				$result["status"] = STATUS_SUCCESS;
			else {
				$result["status"] = STATUS_ERROR;
				$result["error_reason"] = $error_reason;
			}
			echo json_encode($result);
			break;
		
		case "register_friendship":
		
			$username = isset($_POST["username"])? $_POST["username"]: "";
			$password = isset($_POST["password"])? $_POST["password"]: "";
			$ids_list = isset($_POST["ids_list"])? $_POST["ids_list"]: "";
			$is_adding = isset($_POST["is_adding"])? intval($_POST["is_adding"]): "";
			
			if (!check_authentication($username, $password))
				$error_reason = ERROR_USERNAME_PASSWORD_INVALID;
			else {
				$ids = explode(",", $ids_list);
				foreach ($ids as $target_id) {
					$target_id = intval(trim($target_id));
					register_friendship($username, $target_id, $is_adding);
				}
			}
			$result = array();
			if (!isset($error_reason))
				$result["status"] = STATUS_SUCCESS;
			else {
				$result["status"] = STATUS_ERROR;
				$result["error_reason"] = $error_reason;
			}
			echo json_encode($result);
			break;

		// private usage
		case "remove_table_row":
		
			// assuming every table has an "id" attribute
			$table = isset($_POST["table"])? $_POST["table"]: "";
			$id = isset($_POST["id"])? $_POST["id"]: "";
			
			// specific cases
			switch ($table) {
				case "users":
					$username = get_data($table, "id", $id, "username");
					$image_uri = "../images/".$username.".jpg";
					if (file_exists($image_uri))
						unlink($image_uri);
					break;
			}

			$sql = "DELETE FROM `$table` WHERE `id` = '$id'";
			mysql_query($sql, $conn);

			break;
	}
?>
