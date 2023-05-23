package myNewPackage;
import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Stateless
@Entity
public class meal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ManyToOne
    @JoinColumn(name = "restaurant_id")
    private restaurant restaurant;
	
	@ManyToOne
    private orderEntity order;
	
	public orderEntity getOrder() {
		return order;
	}
	public void setOrder(orderEntity order) {
		this.order = order;
	}
	public restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	int Id; 
	
	String name;
	int price;

	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
