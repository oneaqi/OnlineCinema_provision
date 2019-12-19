package ba.pehli.cinema.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ba.pehli.cinema.domain.User;
import ba.pehli.cinema.service.UserDao;
import ba.pehli.cinema.utils.EmailUtils;

/**
 * Controller class responsible for managing application users.
 * 
 * @author almir
 *
 */

@Controller
@RequestMapping("/users")
public class UsersController {
	
	private MessageSource messageSource;

	private EmailUtils emailUtils;
	
	private UserDao userDao;
	
	/**
	 * Shows jsp page for new users registration.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public String showRegistring(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "users/register";
	}
	
	/**
	 * Processing user registration. For new user methos sends email message with verification code.
	 * 
	 * @param user
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String register(@Valid User user,BindingResult bindingResult,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes,Locale locale) {
		if (bindingResult.hasErrors()) {
			String message = messageSource.getMessage("registration.error", null, locale);
			model.addAttribute("message", message);
			return "users/register";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(false);
		try {
			// generate verification code from username and password
			user.setVerificationCode(getMD5(user.getUsername()+user.getPassword()));
			user = userDao.save(user);
			sendEmail(user,locale);
		} catch(Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users/register";
		}
		String message = messageSource.getMessage("registration.email.message", null, locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users/register";
	}
	
	/**
	 * After user receives verification email and clicks on vericitaion link, this method tries
	 * to verify user and enable his record in database.
	 * 
	 * @param verificationCode
	 * @param model
	 * @param redirectAttributes
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/verification/{verificationCode}",method=RequestMethod.GET)
	public String verify(@PathVariable("verificationCode") String verificationCode,Model model,RedirectAttributes redirectAttributes,Locale locale) {
		User user = userDao.findUserByVerificationCode(verificationCode);
		String message;
		if (user != null) {
			user.setEnabled(true);
			user = userDao.save(user);
			model.addAttribute("user",user);
			message = messageSource.getMessage("verification.success", null, locale);
		} else {
			user = new User();
			message = messageSource.getMessage("verification.error", null, locale);
		}
		model.addAttribute("user",user);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users/register";
	}
	
	/**
	 * When user enters wrong authentication data this method shows appropriate message
	 *  
	 * @param model
	 * @param redirectAttributes
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/loginfail",method=RequestMethod.GET)
	public String loginFail(Model model,RedirectAttributes redirectAttributes,Locale locale) {
		String message = messageSource.getMessage("login.error", null, locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movies";
	}
	
	/**
	 * Sending email for user verification. Uses EmailUtils utility class.
	 * 
	 * @param user
	 * @param locale
	 * @throws MessagingException
	 */
	private void sendEmail(User user,Locale locale) throws MessagingException {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", user.getUsername());
		params.put("password",user.getPassword());
		params.put("country", user.getCountry());
		params.put("verificationCode", user.getVerificationCode());
		
		String subject = messageSource.getMessage("registration.title", null, locale);
		emailUtils.sendEmail(user,subject,"email/templateRegistration.vm",params);
	}
	
	/**
	 * Simple encryption of text. Used for generating verification code but could be used for other 
	 * purposes.
	 * 
	 * @param text Text to be encrypted
	 * @return Encrypted text in MD5
	 * @throws NoSuchAlgorithmException
	 */
	private String getMD5(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes());
		return Base64.getEncoder().encodeToString(md.digest());
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
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
