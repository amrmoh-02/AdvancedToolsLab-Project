package myNewPackage;
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
public class runner {

	 
	@ManyToOne
    @JoinColumn(name="restaurant_id")
    private restaurant restaurant;

    @OneToMany(mappedBy ="runner",fetch = FetchType.LAZY)
    private List<orderEntity> orders;
	
    
    
	
	public restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public List<orderEntity> getOrders() {
		return orders;
	}
	public void setOrders(List<orderEntity> orders) {
		this.orders = orders;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int Id;
	String name;
	int completed_orders;
	
	enum runner_status
	{
		available,
		busy
	}
	
	
	runner_status status;

	
	
	public runner() {
         this.status = runner_status.available;
         this.completed_orders = 0;
     }
	
	
	
	int delivery_fees;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		this.Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getDelivery_fees() {
		return delivery_fees;
	}
	public void setDelivery_fees(int delivery_fees) {
		this.delivery_fees = delivery_fees;
	}
	
	public runner_status getStatus() {
		return status;
	}
	public void setStatus(runner_status status) {
		this.status = status;
	}
	public int getCompleted_orders() {
		return completed_orders;
	}
	public void setCompleted_orders(int completed_orders) {
		this.completed_orders = completed_orders;
	}
	

	
	
}
