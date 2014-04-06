package com.upv.adm.adm_personal_shapes.classes;

public class BeanShape implements IListItem {

	private Long id;
	
	private String 
			name,
			description,
			type;

	private String coords;

	private String image;

	public BeanShape(Long id, String name, String description, String type, String coords, String image) {

		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.coords = coords;
		this.image = image;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString(){
		return this.name;
		
	}

	@Override
	public String getKey() {
		return getKey();
	}

	@Override
	public String getValue() {
		return getName();
	}

}
