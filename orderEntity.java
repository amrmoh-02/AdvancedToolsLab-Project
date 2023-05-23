package myNewPackage;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Stateless
@Entity
public class orderEntity {

	 
	@ManyToOne
    @JoinColumn(name = "restaurant_id")
    private restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "runner_id")    
    private runner runner;
	
     
	 
	public restaurant getRestaurant() {
		return restaurant;
	}


	public void setRestaurant(restaurant restaurant) {
		this.restaurant = restaurant;
	}


	public runner getRunner() {
		return runner;
	}


	public void setRunner(runner runner) {
		this.runner = runner;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int Id;
	
	
	@OneToMany(mappedBy="order", fetch = FetchType.LAZY)
    private List<meal> Item_array;
    
    
	//ArrayList<meal> Item_array = new ArrayList<>();
	
	
	int total_price; 

	enum order_status{
		preparing,
		delivered,
		canceled
	}

	private order_status status;
	
	
	public orderEntity() {
         this.status = order_status.preparing;
         this.Item_array = new ArrayList<>();
     }
	
	
	public int getid() {
		return Id;
	}
	public void setid(int id) {
		this.Id = id;
	}
/*	public ArrayList<meal> getItem_array() {
		return Item_array;
	}
	public void setItem_array(ArrayList<meal> item_array) {
		Item_array = item_array;
	}
	*/
	
	public int getTotal_price() {
		return total_price;
	}
	public List<meal> getItem_array() {
		return Item_array;
	}


	public void setItem_array(List<meal> item_array) {
		Item_array = item_array;
	}


	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
	
	public order_status getStatus() {
		return status;
	}
	public void setStatus(order_status status) {
		this.status = status;
	}

}
