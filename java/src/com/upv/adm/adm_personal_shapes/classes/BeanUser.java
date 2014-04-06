package com.upv.adm.adm_personal_shapes.classes;

/**
 * 
 * De momento esta clase no se utilizará porque no es necesario guardar información
 * del usuario en la base de datos local. La información de cada usuario se guardará
 * en el servidor web pues cuando los usuarios hagan búsqueda de usuarios necesitan
 * recuperar los perfiles desde la web. De todas formas conservaremos esta clase.
 * 
 */
public class BeanUser {

	private int id;
	
	private String 
			username,
			password,
			name,
			country,
			gender,
			image,
			email,
			phone;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String sex) {
		this.gender = sex;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

