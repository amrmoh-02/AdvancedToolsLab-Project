package myNewPackage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam; 
import javax.ws.rs.core.MediaType;
 
@Stateless
@Path("/UserController")

@RolesAllowed({"RestaurantOwner, Customer,Runner"})

public class userController {
	
	@PersistenceContext 
	private EntityManager em;
	@PermitAll
	     
	@RolesAllowed("RestaurantOwner")
    @POST
    @Path("/createRes")
    public String CreateRestaurant(@QueryParam("name") String name, @QueryParam("ownerid") int ownerid)
    {	
		restaurant newRestaurant = new restaurant();
		newRestaurant.setOwnerId(ownerid);
		newRestaurant.setName(name);
		em.persist(newRestaurant);
		return "success";
		
    }
	 
	
  @RolesAllowed("RestaurantOwner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/createMenu")
    public List<String> createRestaurantMenu(CreateMenuRequest request)
    {
        int restId = request.getRestaurantId();
        ArrayList<meal>ListOfmeals = request.getMeals();

        TypedQuery<restaurant> myQuery = em.createQuery("SELECT r FROM restaurant r where r.Id = :restId",restaurant.class);
        myQuery.setParameter("restId", restId);
        restaurant restaurant = myQuery.getSingleResult();

        
        if(restaurant!=null) {
        for(int i = 0;i<ListOfmeals.size();i++)
        {
        	ListOfmeals.get(i).setRestaurant(restaurant);
        	em.persist(ListOfmeals.get(i));
        }
        
        restaurant.setMeals(ListOfmeals);
        em.merge(restaurant);
        
      
        List<String> mealNames = new ArrayList<>();
        for (meal m : restaurant.getMeals()) {
            mealNames.add(m.getName());
        }
        return mealNames;        
        }
         else
        {
            return null;
        }
    }
	
    
    
     @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("RestaurantOwner")
      @POST
      @Path("/AddMeal")
      public String AddMeal(@QueryParam("restaurantid")int restaurantid,@QueryParam("name") String name,@QueryParam("newPrice") int newPrice)
      {
          TypedQuery<restaurant> myQuery = em.createQuery("SELECT r FROM restaurant r where r.Id = :restaurantid",restaurant.class);
          myQuery.setParameter("restaurantid", restaurantid);
          restaurant restaurant = myQuery.getSingleResult();
          
          String MealInfo = "";

          if(restaurant !=null) {
          meal m = new meal();
          m.setName(name);
          m.setPrice(newPrice);
          m.setRestaurant(restaurant);
          em.persist(m);
          
          List<meal> list_of_meals = restaurant.getMeals();
          list_of_meals.add(m);
          restaurant.setMeals(list_of_meals);
          em.merge(restaurant);          
                  
          for(int i = 0; i < list_of_meals.size()-1;i++)
          {
        	  MealInfo += "MealName: " + list_of_meals.get(i).getName() + ",MealPrice: " + list_of_meals.get(i).getPrice();        	          	        	  
          }       
          return MealInfo;
          }          
		return null;       
      }
     
	
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("RestaurantOwner")
    @PUT
    @Path("/editPrice")
    public String changeMealPrice(@QueryParam("restaurantid")int restaurantid ,@QueryParam("name") String name ,@QueryParam("newPrice") int newPrice)
    {	
    	TypedQuery<restaurant> myQuery = em.createQuery("SELECT r FROM restaurant r where r.Id = :restaurantid",restaurant.class);
        myQuery.setParameter("restaurantid", restaurantid);
		restaurant restaurant = myQuery.getSingleResult();
				
		 
        if(restaurant !=null)
        {
        	List<meal> list_of_meals = restaurant.getMeals();
        	 String MealInfo = "";
        	
            for (int i=0 ; i< list_of_meals.size(); i++)
            {
                if(list_of_meals.get(i).getName().equals(name))
                {
                    list_of_meals.get(i).setPrice(newPrice);             
                    em.merge(list_of_meals.get(i));                   
                    MealInfo = "Meal Name: " + list_of_meals.get(i).getName() + ",Meal Price: " + list_of_meals.get(i).getPrice();                    
                }
            }
            em.merge(restaurant);
            return MealInfo;
        }
		return null;
        
       
    }
	
	
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("RestaurantOwner")
	@GET
	@Path("/getResDetails")
	public String getRestaurantDetails(@QueryParam("restaurantid")int restaurantid)
	{
    	String resName;
    	int resId;
    	String resDetails;
    	    	    	
    	
		 TypedQuery<restaurant> query = em.createQuery("SELECT r FROM restaurant r where r.Id = :restaurantid", restaurant.class);
		 query.setParameter("restaurantid", restaurantid);
		 restaurant res = query.getSingleResult();
		 
		 resName = res.getName();
		 resId = res.getId();
		 
		 resDetails =" Restaurant Name: " + resName +","+ " RestaurantID: " + resId;
		 resDetails += " ,Meals: ";
		 
		 for(int i = 0 ; i < res.getMeals().size();i++) {
			 resDetails += " " + res.getMeals().get(i).getName();
		 }
		 
		 return resDetails;
		 		 		 
		 
	}
	 

	
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Runner")
    @PUT
    @Path("/CancelOrder")
    public String CancelOrder(@QueryParam("orderid") int orderid)
    {
    	
    	TypedQuery<orderEntity> orderquery = em.createQuery("SELECT o FROM orderEntity o where o.Id = :orderid", orderEntity.class);
		orderquery.setParameter("orderid", orderid);
		orderEntity ord = orderquery.getSingleResult();	
	
	
        if (ord !=null && ord.getStatus()==orderEntity.order_status.preparing)
        {
        	int runnerid = ord.getRunner().getId();
    		
    		TypedQuery<runner> runnerquery = em.createQuery("SELECT r FROM runner r where r.Id = :runnerid", runner.class);
    		runnerquery.setParameter("runnerid", runnerid);
    		runner run = runnerquery.getSingleResult();
    		
    		
            ord.setStatus(orderEntity.order_status.canceled);
            run.setStatus(runner.runner_status.available);
            
            em.merge(run);
            em.merge(ord);
            
            return "Order: " +  ord.getid() + " is "+ ord.getStatus().toString();
        } 
        else {
        	return null;
		}
    }

    
    
	@RolesAllowed("RestaurantOwner")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/createResReport")
    public String creatRestaurantReport(@QueryParam("restaurantid")int restaurantid)
    {
    	TypedQuery<restaurant> myQuery = em.createQuery("SELECT r FROM restaurant r where r.Id = :restaurantid",restaurant.class);
        myQuery.setParameter("restaurantid", restaurantid);
		restaurant restaurant = myQuery.getSingleResult();
		
		String ReportInfo= "";
	
        if (restaurant != null)
        {
         report rep = new report();
         int totalOrdersNum = 0;
         int totalCancelledNum = 0;
         int totalEarned = 0;
              
         List<orderEntity> orders = new ArrayList<>(restaurant.getOrders());
        
        for(int i = 0;i<restaurant.getOrders().size();i++)
        {
            if(orders.get(i).getStatus() == orderEntity.order_status.delivered)
            {
               totalOrdersNum++;
              totalEarned+= orders.get(i).getTotal_price();
            }

            else if(orders.get(i).getStatus() == orderEntity.order_status.canceled)
            {
                totalCancelledNum++;
            }
        }
        
        rep.setTotalOrdersNum(totalOrdersNum);
        rep.setTotalCancelledNum(totalCancelledNum);
        rep.setTotalEarned(totalEarned);
        
        rep.setRestaurant(restaurant);
        em.persist(rep);
        
        ReportInfo = "Total Earned: "+ rep.getTotalEarned() + "\n"
        + "Total Orders: " + rep.getTotalOrdersNum() + "\n"
        + "Total Cancelled: "+ rep.getTotalCancelledNum();
        
        return ReportInfo;
        }
        else
        {
            return null;
        }
    }
	
  @RolesAllowed("Customer")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/createOrder")
    public ArrayList<String> createOrder(@QueryParam("restaurantName")String restaurantName, @QueryParam("MealNames") ArrayList<String> MealNames)
    { 
    	ArrayList <String> receipt = new ArrayList<String>();
    	receipt.add("Restaurant Name: " + restaurantName + "\n");
        
        TypedQuery<restaurant> resquery = em.createQuery("SELECT r FROM restaurant r where r.name = :restaurantName", restaurant.class);
        resquery.setParameter("restaurantName", restaurantName);
        restaurant res = resquery.getSingleResult();
        
        TypedQuery<runner> runnerquery = em.createQuery("SELECT r FROM runner r WHERE r.status = :status", runner.class);
        runnerquery.setParameter("status", runner.runner_status.available);
        List<runner> resultList = runnerquery.getResultList();                
        
        runner R = resultList.get(0);     
                        
        if (res !=null)
        {
            orderEntity NewOrder = new orderEntity();
          
            
            int sum=0;
            receipt.add("Meals: ");
            for (String mealName : MealNames) {
                for (meal meal : res.getMeals()) {         	                	
                    if (mealName.equals(meal.getName())) {
                    	
                    	receipt.add(mealName + ",");
                    	sum += meal.getPrice();                        
                        NewOrder.getItem_array().add(meal);
                        meal.setOrder(NewOrder);
                        em.merge(meal);
 
                    }
                }
            }
            
            receipt.add("Delivery Fees: " + R.getDelivery_fees() + "\n");
            receipt.add("Runner Name: " + R.getName() + "\n");
            sum+=R.getDelivery_fees();
            receipt.add("Total Price: "+ sum + "\n");
            NewOrder.setTotal_price(sum);
            NewOrder.setRestaurant(res);
            NewOrder.setRunner(R);
            R.getOrders().add(NewOrder);
            res.getOrders().add(NewOrder);
            
            em.persist(NewOrder);
            em.merge(res);
            R.setStatus(runner.runner_status.busy);
            em.merge(R);
            return receipt;    
    }
        return null;
        
        }
	
	
    @Path("/editOrder")
    @RolesAllowed("Customer")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public ArrayList<String> editOrder(@QueryParam("orderid")int orderid,@QueryParam("name") String name)
    {
        TypedQuery<orderEntity> ord  = em.createQuery("SELECT o FROM orderEntity o where o.Id = :orderid", orderEntity.class);
        ord.setParameter("orderid", orderid);
        orderEntity updated = ord.getSingleResult();

        TypedQuery<meal> added  = em.createQuery("SELECT r FROM meal r where r.name = :name", meal.class);
        added.setParameter("name", name);
        meal newmeal = added.getSingleResult();
        
        ArrayList<String> orderInfoList = new ArrayList<String>();

        if(ord !=null && updated.getStatus()==orderEntity.order_status.preparing)
        {
            int sum =0;
            updated.getItem_array().add(newmeal);
            sum = updated.getTotal_price() +newmeal.getPrice();                        
            updated.setTotal_price(sum);
            em.merge(updated);
            
            
            restaurant restaurant = updated.getRestaurant();
            runner runner = updated.getRunner();

            String restaurantName = restaurant.getName();
            
            String itemsList = "";            
            for(int i = 0;i< updated.getItem_array().size();i++)
            {
            	itemsList += updated.getItem_array().get(i).getName();
            	itemsList += ",";
            }
            
            int deliveryFees = runner.getDelivery_fees();
            String runnerName = runner.getName();
            int totalReceiptValue = updated.getTotal_price();

            // Add information to the list
            orderInfoList.add("Restaurant Name: " + restaurantName);
            orderInfoList.add("Items List: " + itemsList);
            orderInfoList.add("Delivery Fees: " + deliveryFees);
            orderInfoList.add("Runner: " + runnerName);
            orderInfoList.add("Total Receipt Value: " + totalReceiptValue);

            return orderInfoList;
        } else {
            return null;
        }
    }
    
    
	
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Customer")
    @GET
    @Path("/getRests")
    public List<String> listRestaurants() 
    {
        ArrayList <String> restNames = new ArrayList<String>();

        TypedQuery<restaurant> myQuery = em.createQuery("SELECT r FROM restaurant r",restaurant.class);
        List<restaurant> restaurants = myQuery.getResultList();

        for(int i = 0; i< restaurants.size();i++)
        {
            restNames.add(restaurants.get(i).getName());
            restNames.add(", ");
        }

        return restNames;

    }
	
	
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Runner")
    @PUT
    @Path("/changeStatus")
    public String changeStatus(@QueryParam("runnerid")int runnerid ,@QueryParam("orderid") int orderid)
    {
    	
    	TypedQuery<orderEntity> orderquery = em.createQuery("SELECT o FROM orderEntity o where o.Id = :orderid", orderEntity.class);
		orderquery.setParameter("orderid", orderid);
		orderEntity ord = orderquery.getSingleResult();
		
    	TypedQuery<runner> runnerquery = em.createQuery("SELECT r FROM runner r where r.Id = :runnerid", runner.class);
		runnerquery.setParameter("runnerid", runnerid);
		runner run = runnerquery.getSingleResult();
		
		
        if (ord !=null && run!=null && ord.getStatus()==orderEntity.order_status.preparing)
        {
            ord.setStatus(orderEntity.order_status.delivered);
            run.setStatus(runner.runner_status.available);
            run.completed_orders++;
            em.merge(run);
            em.merge(ord);
            
            return "Runner set to " + run.getStatus().toString() + " Order set to " +  ord.getStatus().toString();
        }
        else
        	return null;
        
    }

	@RolesAllowed("Runner")
    @GET
    @Path("/numOfTrips")
	public int getCompletedTrips(@QueryParam("runnerid") int runnerid)
	{
    	TypedQuery<runner> runnerquery = em.createQuery("SELECT r FROM runner r where r.Id = :runnerid", runner.class);
		runnerquery.setParameter("runnerid", runnerid);
		runner run = runnerquery.getSingleResult();
		
		if(run!= null)
		{
		return run.completed_orders;
		}
		else
			
			return -1;
	}

    
    @RolesAllowed("Runner")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/ListRunners")
    public String ListRunners()
    {	
            TypedQuery<runner> myQuery = em.createQuery("SELECT r FROM runner r",runner.class);
            List<runner> runners = myQuery.getResultList();
                    
            String RunnerInfo = "";
            
                for(int i = 0; i< runners.size();i++)
                {
                	RunnerInfo +="Runner: ";
                	RunnerInfo += runners.get(i).getId()   + "\n";
                    RunnerInfo += runners.get(i).getName() + "\n";                    
                    RunnerInfo += runners.get(i).getDelivery_fees() + "\n";
                    RunnerInfo += runners.get(i).getStatus() + "\n";
                                
                }

                return RunnerInfo;
    }
    
    	 
	@RolesAllowed("Runner")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/createrunner")
    public String CreateRunner (@QueryParam("deliveryfees")int deliveryfees, @QueryParam("runnerName") String runnerName)
    {
        runner newrunner = new runner ();
        
        newrunner.setDelivery_fees(deliveryfees);
        newrunner.setName(runnerName);
        newrunner.setStatus(runner.runner_status.available);
        
        em.persist(newrunner);
        
        return "Runner Name: " + newrunner.getName() + " Fees: " + newrunner.getDelivery_fees() + " Status: " + newrunner.getStatus();
    }
      
}

	
	

