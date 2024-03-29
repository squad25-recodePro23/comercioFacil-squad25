package squad25.comercioFacil.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import squad25.comercioFacil.enums.AccesLevel;
import squad25.comercioFacil.enums.Category;
import squad25.comercioFacil.enums.State;
import squad25.comercioFacil.enums.SystemAlert;
import squad25.comercioFacil.models.Client;
import squad25.comercioFacil.models.Section;
import squad25.comercioFacil.services.ClientService;
import squad25.comercioFacil.services.MarketPlaceService;
import squad25.comercioFacil.services.ProductService;
import squad25.comercioFacil.services.SectionService;

@Controller
public class NavigationController {

	@Autowired
	private SectionService sectionSv;

	@Autowired
	private ClientService clientSv;
	
	@Autowired
	private MarketPlaceService marketService;
	
	@Autowired
	private ProductService productSv;

	private Section section;
	
	@GetMapping("/")
	public String home0(Model model) {
		model.addAttribute("alert", SystemAlert.SUCCESSFUL_OPERATION);
		
		return "index";
	}
	
	@GetMapping("/st={state}")
	public String homePage(Model model, @PathVariable("state") State state) {
		model.addAttribute("markets", this.marketService.getByState(state));
		model.addAttribute("alert", SystemAlert.SUCCESSFUL_OPERATION);
		
		return "index";
	}
	
	@GetMapping("/mk={market}")
	public String homePage2(Model model, @PathVariable("market") Long idMarket) {
		model.addAttribute("produtosCat1", this.productSv.getAllByIdMarketAndCategory(idMarket, Category.CATEGORY_ONE));
		model.addAttribute("produtosCat2", this.productSv.getAllByIdMarketAndCategory(idMarket, Category.CATEGORY_TWO));
		model.addAttribute("produtosCat3", this.productSv.getAllByIdMarketAndCategory(idMarket, Category.CATEGORY_TREE));
		model.addAttribute("produtosCat4", this.productSv.getAllByIdMarketAndCategory(idMarket, Category.CATEGORY_FOUR));
		model.addAttribute("alert", SystemAlert.SUCCESSFUL_OPERATION);
		
		return "index";
	}

	@GetMapping("/about")
	public String about() {
		return "pages/about";
	}

	@GetMapping("/contact")
	public String contact() {
		return "pages/contacts";
	}

	@GetMapping("/partnersClients")
	public String partClie() {
		return "pages/partnersClients";
	}

	@GetMapping("/adminSide")
	public String testeSide() {
		return "redirect:/admin/getAllMarkets";
	}

	@GetMapping("/employerAdmin")
	public String showEnterprisesPage() {

		if (this.section != null) {
			if (this.section.getUser().getAccesLevel().equals(AccesLevel.EMPLOYER)) {
				return "redirect:/employer/id=" + this.section.getUser().getIdUser() + "/getAllEnterprises";
			} else {
				return "redirect:/employerAdmin"; // Você não possui acesso a esta seção;
			}
		} else {
			return "redirect:/"; // Faça login para continuar;
		}
	}

	@GetMapping("/notfications")
	public String notifications() {
		return "employer/notificationsPage";
	}

	@GetMapping("/prodEnterprise")
	public String productEnterprise() {
		return "product/productEnterprise";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "user/loginPage";
	}
	
	@PostMapping("/login")
	public String loginClient(@RequestParam String email, @RequestParam String password) {
		Client user = this.clientSv.getByEmailAndPassword(email, password);

		if (this.section != null) {
			if (user != null) {
				this.sectionSv.logout(this.section.getIdSection());
				this.section = new Section(user);
				this.sectionSv.save(this.section);
			} else {
				return "redirect:/login?login=false";
			}
			
		} else {
			if (user != null) {
				this.section = new Section(user);
				this.sectionSv.save(this.section);
			} else {
				return "redirect:/login?login=false";
			}
		}

		return "redirect:/login?login=true";
	}

	@GetMapping("/cadClient")
	public String cadClient() {
		return "client/register";
	}

	@GetMapping("/help")
	public String help() {
		return "pages/help";
	}
}