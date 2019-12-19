package ba.pehli.cinema.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ba.pehli.cinema.domain.Movie;
import ba.pehli.cinema.domain.Rating;
import ba.pehli.cinema.domain.User;
import ba.pehli.cinema.domain.WSOmdbMovie;
import ba.pehli.cinema.service.MovieDao;
import ba.pehli.cinema.service.RatingDao;
import ba.pehli.cinema.service.UserDao;
import ba.pehli.cinema.utils.EmailUtils;

/**
 * Controller class responsible for showing, inserting and updating of movies. Also, class handles pagination.
 * @author almir
 *
 */

@Controller
@RequestMapping(value="/movies")
public class MovieController{
	
	private static final int PAGE_SIZE = 5;	// pagination
	private static final String URL_OMDB = "http://www.omdbapi.com";  // restful-ws
	private static final String URL_OMDB_GET = URL_OMDB + "?i=";	  // search form imdb id
	
	private EmailUtils emailUtils;
	
	private MovieDao movieDao;
	private UserDao userDao;
	private RatingDao ratingDao;
	private MessageSource messageSource;
	private DataSource dataSource;
	
	private RestTemplate restTemplate;
	
	private Facebook facebook;
	
	@Autowired
	public MovieController(MovieDao movieDao,UserDao userDao,RatingDao ratingDao) {
		this.movieDao = movieDao;
		this.userDao = userDao;
		this.ratingDao = ratingDao;
	}
	
	/**
	 * Default handler, just show movies list
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String list(Model model) {
		return showPage(1, model);
	}
	
	/**
	 * Shows page with PAGE_SIZE movies.
	 * @param id Start movie.id to show
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/page/{id}", method=RequestMethod.GET)
	public String showPage(@PathVariable("id") int id, Model model) {
		User user = userDao.getAuthenticatedUser();
		List<Movie> movies = movieDao.findAll(id,PAGE_SIZE);
		Map<Integer, Integer> ratings = new HashMap<Integer, Integer>();
		
		if (user != null) {
			for (Movie movie : movies) {
				Rating rating = ratingDao.findByMovieAndUser(movie, user); 
				ratings.put(movie.getId(), rating != null ? rating.getRating() : 0);
			}
		}
		
		model.addAttribute("movies", movies);
		model.addAttribute("ratings", ratings);
		
		int moviesCount = movieDao.findCount();
		int i = 1;
		List<Integer> pages = new ArrayList<Integer>();
		while (i <= moviesCount) {
			pages.add(i);
			i+= PAGE_SIZE;
		}
		model.addAttribute("pages", pages);
		model.addAttribute("pageSize", PAGE_SIZE);
		
		return "movies/list";
	}
	
	/**
	 * Generating image that is available for <img> html tag in jsp pages
	 * @param id Id of picture
	 * @return image as byte array
	 */
	@RequestMapping(value="/photo/{id}", method=RequestMethod.GET)
	@ResponseBody
	public byte[] downloadImage(@PathVariable("id") int id) {
		Movie movie = movieDao.findById(id);
		if (movie != null)
			return movie.getImage();
		return null;
	}
	
	/**
	 * Shows edit page for selected movie.
	 * 
	 * @param id Identification of selected movie
	 * @param model
	 * @return edit page
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String showEdit(@PathVariable int id,Model model) {
		Movie movie = movieDao.findById(id);
		model.addAttribute("movie", movie);
		return "movies/edit";
	}
	
	/**
	 * Shows page for entering new movie.
	 * 
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String showNew(Model model) {
		Movie movie = new Movie();
		model.addAttribute("movie", movie);
				
		return "movies/edit";
	}
	
	/**
	 * Processing of updated movie. If movie has no image attempt is made to recover image from database.
	 * Every movie is validated against errors in input process.
	 * 
	 * @param movie
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @param locale
	 * @param file
	 * @return
	 */
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
	public String edit(@Valid Movie movie,BindingResult bindingResult,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes,
				Locale locale,@RequestParam(value="file",required=false) MultipartFile file,
				@RequestParam(value="fileUrl",required=false) String fileUrl) {
		
		if (bindingResult.hasErrors()) {
			String message = messageSource.getMessage("movies.edit.error", null, locale);
			model.addAttribute("message", message);
			return "movies/edit"; 
		}
		
		if (!file.isEmpty()) {	
			try {
				byte[] imageContent = file.getBytes();
				movie.setImage(imageContent);
			} catch (Exception e) {
				
			}
		} else if (fileUrl != null && fileUrl.length() > 0) {
			try {
				System.out.println("fileUrl:" + fileUrl);
				URL posterUrl = new URL(fileUrl);
				byte[] imageContent = IOUtils.toByteArray(posterUrl);
				movie.setImage(imageContent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Movie dbMovie = movieDao.findById(movie.getId());
			movie.setImage(dbMovie.getImage());
		}
		
		movieDao.save(movie);
		
		String message = messageSource.getMessage("form.success", null, locale);
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/movies/edit/"+movie.getId();
	}
	
	/**
	 * Processing of new movie. After saving the movie, methos sends email message to every
	 * user belonging to ROLE_USER role.
	 * 
	 * @param movie
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @param locale
	 * @param file User selected image
	 * @param fileUrl hidden field generated from OMDB for image URL
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/new", method=RequestMethod.POST)
	public String newMovie(final @Valid Movie movie,BindingResult bindingResult,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes,
				final Locale locale,@RequestParam(value="file",required=false) MultipartFile file,
				@RequestParam(value="fileUrl",required=false) String fileUrl) {
		if (bindingResult.hasErrors()) {
			String message = messageSource.getMessage("movies.edit.error", null, locale);
			model.addAttribute("message", message);
			return "movies/edit"; 
		}
		
		if (!file.isEmpty()) {
			try {
				byte[] imageContent = file.getBytes();
				movie.setImage(imageContent);
			} catch (Exception e) {
				
			}
		} else if (fileUrl != null && fileUrl.length() > 0) {
			
			try {
				URL posterUrl = new URL(fileUrl);
				byte[] imageContent = IOUtils.toByteArray(posterUrl);
				movie.setImage(imageContent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		movieDao.save(movie);
		
		// send email to every user in seperate thread
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (User user : userDao.findByRole("ROLE_USER")) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("username", user.getUsername());
					params.put("movieName", movie.getName());
					params.put("movieDescription", movie.getDescription());
					try {
						String subject = messageSource.getMessage("movies.new", null, locale);
						emailUtils.sendEmail(user, subject, "email/templateNewMovie.vm", params);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		thread.start();
		
		String message = messageSource.getMessage("form.success", null, locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movies/edit/"+movie.getId();
	}
	
	/**
	 * Saves rating for user and movie. Called from ajax function in jsp file.
	 * 
	 * @param movieId Movie that we are rating
	 * @param rating User who is rating the movie
	 * @return Identification of rating object
	 */
	@RequestMapping(value="/rating", method=RequestMethod.GET)
	@ResponseBody 
	public int rateMovie(int movieId, int rating) {
		User user = userDao.getAuthenticatedUser();
		Movie movie = movieDao.findById(movieId);
		Rating rt = ratingDao.findByMovieAndUser(movie, user);
		if (rt != null) {
			rt.setRating(rating);
		} else {
			rt = new Rating();
			rt.setMovie(movie);
			rt.setUser(user);
			rt.setRating(rating);
		}
		ratingDao.save(rt);
		return rt.getId(); 
	}
	
	/**
	 * Generates Jasper report in PDF format.
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/catalog", method=RequestMethod.GET)
	public void generateCatalog(HttpServletRequest request, HttpServletResponse response,Locale locale) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("Title", messageSource.getMessage("movies.catalog.title", null, locale));
		params.put("Subtitle", messageSource.getMessage("movies.catalog.subtitle", null, locale));
		params.put("Page", messageSource.getMessage("movies.catalog.page", null, locale));
		params.put("PageOf", messageSource.getMessage("movies.catalog.pageof", null, locale));
		params.put("MoviesCount", messageSource.getMessage("movies.catalog.moviesCount", null, locale));
		
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			ClassPathResource reportFile = new ClassPathResource("reports/catalog.jasper");
			try {
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getInputStream());
				byte[] bytes = JasperRunManager.runReportToPdf(jasperReport,params,conn);
				
				response.reset();
				response.resetBuffer();
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				
				ServletOutputStream stream = response.getOutputStream();
				stream.write(bytes, 0, bytes.length);
				stream.flush();
				stream.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Gets movie information from Open Movie Database Restful-WS. Called via Ajax
	 * 
	 * @param movieId Movie that we are rating
	 * @param rating User who is rating the movie
	 * @return Identification of rating object
	 */
	@RequestMapping(value="/getinfo", method=RequestMethod.GET)
	@ResponseBody 
	public WSOmdbMovie getInfo(String imdbId) {
		WSOmdbMovie result = restTemplate.getForObject(URL_OMDB_GET + imdbId, WSOmdbMovie.class);
		System.out.println(result);
		return result;
	}
	
	/**
	 * Facebook like of movie
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/like/{id}", method=RequestMethod.GET)
	public String connectToFacebook(@PathVariable("id") int id,Model model,Locale locale) {
		try {
			if (facebook.isAuthorized()) {
				Movie movie = movieDao.findById(id);
				String message = messageSource.getMessage("movies.like.text", new Object[] {movie.getName()}, locale);
				facebook.feedOperations().updateStatus(message);
			} else {
				return "redirect:/connect/facebook";
			}
		} catch (NullPointerException e) {
			return "redirect:/connect/facebook";
		}
		return showPage(1, model);
	}
	
	// If we want to insert images we have to register property support editor who is responsible
	// for converting String into byte array and vice versa
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public void setEmailUtils(EmailUtils emailUtils) {
		this.emailUtils = emailUtils;
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Autowired
	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}
	
	
	
	
}
