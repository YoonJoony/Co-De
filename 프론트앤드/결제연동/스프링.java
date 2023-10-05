@Controller
public class HomeController {
	
	private IamportClient api;
	
	public HomeController() {
    	// REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
		this.api = new IamportClient("3676837107582587","tdzajvB0dPlcJcSs6PnwbckaaurVF4sU0Ve4rvssghlxiCNwLeiuYTj8Cluu3zi3ggofyRVi3IX3MBmA");
	}
		
	@ResponseBody
	@RequestMapping(value="/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid(
			Model model
			, Locale locale
			, HttpSession session
			, @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException
	{	
			return api.paymentByImpUid(imp_uid);
	}
	
}