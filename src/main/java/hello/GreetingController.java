package hello;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.model.Actor;
import hello.model.Film;
import hello.model.Greeting;


@RestController
@RequestMapping("/greet")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private Greeting x;
    
    @Autowired  //connect ke object tertentu yg sudah ada di server
    private EntityManagerFactory em;
    
    //@RequestMapping(method= {RequestMethod.POST,RequestMethod.GET})
    @PostMapping("addActor")
    public int addActor() {
    	int hasil = 0;
    	try {
    		Actor actor2 = new Actor();
    		actor2.setFirstName("Bandung");
    		actor2.setLastName("Lautan");
    		actor2.setLastUpdate(new Date());
    		
    	EntityManager e = em.createEntityManager(); //persist sama dengan save
    	e.getTransaction().begin();
    	e.persist(actor2);
    	e.getTransaction().commit();
    	
    } catch (Exception ex) {
    	System.out.println(ex.getMessage());
    	hasil = -1;
    }
    	return hasil;
    }
    
    
    @PostMapping("editActor")
    public int editActor(@RequestParam("id") Short id) {
    	int hasil = 0;
    	try {
    		EntityManager e = em.createEntityManager(); 
    		
    		e.getTransaction().begin();
    		
    		Actor actor2 = e.find(Actor.class, id);
    		actor2.setFirstName("Jakarta");
    		actor2.setLastName("Genangan");
    		actor2.setLastUpdate(new Date());
    	  		
        	e.getTransaction().commit();
    	
    } catch (Exception ex) {
    	System.out.println(ex.getMessage());
    	hasil = -1;
    }
    	return hasil;
    }
    
    
    @CrossOrigin(origins= {"*"}) //dpt mengakses data di wilayahnya
    @RequestMapping("/actors")
    public List<Actor> allActors() {
    	return em.createEntityManager().createQuery("from Actor").getResultList();
    }
    
    @CrossOrigin(origins= {"*"})
    @RequestMapping("/film")
    public List<Film> allFilm(){
    	return em.createEntityManager().createQuery("from Film").getResultList();
    }
    
    @RequestMapping("/filmactors")
    public List<FilmActorPK> allFilmActor(){
    	return em.createEntityManager().createQuery("from FilmActor").getResultList();
    }
    
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return x;}
        		//new Greeting(counter.incrementAndGet(),       		
    
        		//String.format(template, name));}
        
     @RequestMapping("/data")
     public List<String> dataNegara (@RequestParam("pre") String prefix){
    	 
    	 List<String> data = new ArrayList();
    	 data.add("Indonesia");
    	 data.add("Malaysia");
    	 data.add("Brunei");
    	 data.add("Timor Leste");
    	 data.add("Belanda");
    	 
    	 return data.stream()
    			 .filter(line -> 
    			 	line.contains(prefix))
    			 .collect(Collectors.toList());
          
    	     }
     
     @RequestMapping("countries")
     public String getCountries() throws IOException{
     	URL url = new URL("http://www.webservicex.net/country.asmx/GetCountries");

     URLConnection connection = url.openConnection();
     connection.setDoOutput(true);
     connection.setRequestProperty("Content-Type", "$application/x-www-form-urlenconded");
     connection.setRequestProperty("Content-Length","0");
     
     InputStream stream = connection.getInputStream();
     
     /* return new BufferedReader(new InputStreamReader(stream)).
    		 lines().collect(Collectors.joining("\n")); */
     
     InputStreamReader reader = new InputStreamReader(stream);
     BufferedReader buffer = new BufferedReader(reader);
     
     String line;
     StringBuilder builder = new StringBuilder();
     while ((line = buffer.readLine())!= null) {
    	 builder.append(line);
     }
     return builder.toString();
    
     /*return new BufferedReader(
    		 new InputStreamReader(stream)).
    		 lines().
    		 collect(Collectors.joining("\n"));*/
}
     
}
