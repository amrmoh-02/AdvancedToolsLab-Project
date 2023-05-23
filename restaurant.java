package myNewPackage;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Stateless
@Entity
public class restaurant implements Serializable{
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public List<report> getReports() {
		return reports;
	}
	public void setReports(List<report> reports) {
		this.reports = reports;
	}
	public List<runner> getRunner() {
		return runner;
	}
	public void setRunner(List<runner> runner) {
		this.runner = runner;
	}
	public List<meal> getMeals() {
		return meals;
	}
	public void setMeals(List<meal> meals) {
		this.meals = meals;
	}
	public List<orderEntity> getOrders() {
		return orders;
	}
	public void setOrders(List<orderEntity> orders) {
		this.orders = orders;
	}
	
	@OneToMany(mappedBy="restaurant")
	private List<report>reports;
		
	@OneToMany(mappedBy="restaurant",fetch = FetchType.LAZY)
    private List<runner>runner;


    @OneToMany(mappedBy="restaurant",fetch = FetchType.LAZY)
    private List<meal>meals;

    @OneToMany(mappedBy="restaurant",fetch = FetchType.LAZY)
    private List<orderEntity>orders;
	 
    
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int Id;
	
	String name;
	int ownerId; 
	
	
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
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	
	
}

