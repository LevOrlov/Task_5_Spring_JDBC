package control;


import dao.UserDao;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

//ну здесь собственно объясняется спринг, что этот класс явялется сервлетом(контроллером)
@Controller
public class MyController {
    //Аннотация @Autowired неявно внедряет объектную зависимость.
    @Autowired
    private UserDao userDao;

    //он создает базовый URI, для которого будет использоваться метод
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView listContact(ModelAndView model) {
        List<User> listContact = userDao.getAllUsers();
        model.addObject("listContact", listContact);
        model.setViewName("home");
        return model;
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.GET)
    public ModelAndView addUser() {
        return new ModelAndView("add");
    }


    @RequestMapping(value = "/admin/addUser", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute User user) {
        userDao.addUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDao.getUserById(userId);
        ModelAndView model = new ModelAndView("edit");
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute User user, HttpServletRequest request) {
        user.setRole(request.getParameter("role"));
        user.setId(Integer.parseInt(request.getParameter("id")));
        userDao.updateUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String deleteContact(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(userId);
        return "redirect:/";
    }

}