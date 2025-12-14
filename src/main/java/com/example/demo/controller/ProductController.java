package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.model.Category;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/products")
public class ProductController extends LoginBaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private FileUploadService fileUploadService;

	// 顯示所有商品
	@GetMapping
	public String listProducts(HttpSession session, Model model) {
		// 檢查是否登入
		Long userId = (Long) session.getAttribute("id");
		if (userId == null) {
			return "redirect:/login";
		}

		// 檢查是否為 customer (customer 不能進入)
		User user = userService.getUserById(userId);
		if (user == null || "customer".equals(user.getIdentifyName())) {
			return "redirect:/";
		}

		Long id = (Long) session.getAttribute("id");
		String name = (String) session.getAttribute("name");

		model.addAttribute("name", name);
		model.addAttribute("id", id);
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}

	// 回首頁（session 顯示）
	@GetMapping("/products")
	public String home(HttpSession session, Model model) {
		String name = (String) session.getAttribute("name");
		Long id = (Long) session.getAttribute("id");

		model.addAttribute("name", name);
		model.addAttribute("id", id);

		return "home";
	}

	// 顯示新增表單
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("product", new Product());
		return "add-product";
	}

	// 顯示編輯表單
	@GetMapping("/edit/{prodNum}")
	public String showEditForm(@PathVariable("prodNum") Long prodNum, Model model) {
		model.addAttribute("categories", categoryService.getAllCategories());
		Product product = productService.getProductById(prodNum);
		if (product.getCreatedTime() != null) {
			product.setCreatedTime(product.getCreatedTime().withZoneSameInstant(ZoneId.of("Asia/Taipei")));
		}
		model.addAttribute("product", product);
		return "edit-product";
	}

	// 儲存新商品（支援圖片上傳）- 修正版
	@PostMapping("/save")
	public String saveProduct(@RequestParam("prodName") String prodName, @RequestParam("category.id") Long categoryId,
			@RequestParam("prodPrice") BigDecimal prodPrice,
			@RequestParam(value = "prodInfo", required = false, defaultValue = "") String prodInfo,
			@RequestParam(value = "prodKeywords", required = false, defaultValue = "") String prodKeywords,
			@RequestParam(value = "prodTags", required = false, defaultValue = "") String prodTags,
			@RequestParam(value = "prodStockQty", required = false, defaultValue = "0") Integer prodStockQty,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpServletRequest request) {

		try {
			// 除錯：列印接收到的參數
			System.out.println("========== 接收到的參數 ==========");
			System.out.println("prodName: " + prodName);
			System.out.println("categoryId: " + categoryId);
			System.out.println("prodPrice: " + prodPrice);
			System.out.println("prodInfo: " + prodInfo);
			System.out.println("prodKeywords: " + prodKeywords);
			System.out.println("prodTags: " + prodTags);
			System.out.println("prodStockQty: " + prodStockQty);
			System.out.println("imageFile: " + (imageFile != null ? imageFile.getOriginalFilename() : "null"));
			System.out.println("===================================");

			// 建立新的 Product 物件
			Product product = new Product();
			product.setProdName(prodName.trim());
			product.setProdPrice(prodPrice);
			product.setProdInfo(prodInfo);
			product.setProdKeywords(prodKeywords);
			product.setProdTags(prodTags);
			product.setProdStockQty(prodStockQty);

			// 設定 Category
			Category category = new Category();
			category.setId(categoryId);
			product.setCategory(category);

			// 如果有上傳圖片，儲存並設定路徑
			if (imageFile != null && !imageFile.isEmpty()) {
				System.out.println("開始儲存圖片...");
				String imagePath = fileUploadService.saveFile(imageFile);
				product.setProdImages(imagePath);
				System.out.println("圖片儲存成功: " + imagePath);
			}

			productService.saveProduct(product);
			System.out.println("商品儲存成功！");
			return "redirect:/products";

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("圖片上傳失敗: " + e.getMessage());
			return "redirect:/products/add?error=upload_failed";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("儲存商品失敗: " + e.getMessage());
			return "redirect:/products/add?error=save_failed";
		}
	}

	// 更新商品（支援圖片上傳）- 修正版
	@PostMapping("/update/{prodNum}")
	public String updateProduct(
	        @PathVariable("prodNum") Long prodNum,
	        @RequestParam("prodName") String prodName,
	        @RequestParam("category.id") Long categoryId,
	        @RequestParam("prodPrice") BigDecimal prodPrice,
	        @RequestParam(value = "prodInfo", required = false, defaultValue = "") String prodInfo,
	        @RequestParam(value = "prodKeywords", required = false, defaultValue = "") String prodKeywords,
	        @RequestParam(value = "prodBarcode", required = false, defaultValue = "") String prodBarcode,
	        @RequestParam(value = "prodTags", required = false, defaultValue = "") String prodTags,
	        @RequestParam(value = "prodStockQty", required = false, defaultValue = "0") Integer prodStockQty,
	        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
	    
	    try {
	        // 除錯：列印接收到的參數
	        System.out.println("========== 更新商品參數 ==========");
	        System.out.println("prodNum: " + prodNum);
	        System.out.println("prodName: " + prodName);
	        System.out.println("categoryId: " + categoryId);
	        System.out.println("prodPrice: " + prodPrice);
	        System.out.println("prodInfo: " + prodInfo);
	        System.out.println("prodKeywords: " + prodKeywords);
	        System.out.println("prodBarcode: " + prodBarcode);
	        System.out.println("prodTags: " + prodTags);
	        System.out.println("prodStockQty: " + prodStockQty);
	        System.out.println("imageFile: " + (imageFile != null && !imageFile.isEmpty() ? imageFile.getOriginalFilename() : "無上傳"));
	        System.out.println("===================================");
	        
	        // 取得現有商品資料
	        Product existing = productService.getProductById(prodNum);
	        if (existing == null) {
	            System.out.println("錯誤：找不到商品 ID: " + prodNum);
	            return "redirect:/products?error=not_found";
	        }
	        
	        // 建立更新的 Product 物件
	        Product product = new Product();
	        product.setProdNum(prodNum);
	        product.setProdName(prodName.trim());
	        product.setProdPrice(prodPrice);
	        product.setProdInfo(prodInfo);
	        product.setProdKeywords(prodKeywords);
	        product.setProdBarcode(prodBarcode);
	        product.setProdTags(prodTags);
	        product.setProdStockQty(prodStockQty);
	        product.setCreatedTime(existing.getCreatedTime());
	        
	        // 設定 Category
	        Category category = new Category();
	        category.setId(categoryId);
	        product.setCategory(category);
	        
	        // 處理圖片
	        if (imageFile != null && !imageFile.isEmpty()) {
	            System.out.println("開始處理新圖片...");
	            
	            // 刪除舊圖片
	            if (existing.getProdImages() != null && !existing.getProdImages().isEmpty()) {
	                System.out.println("刪除舊圖片: " + existing.getProdImages());
	                fileUploadService.deleteFile(existing.getProdImages());
	            }
	            
	            // 儲存新圖片
	            String imagePath = fileUploadService.saveFile(imageFile);
	            product.setProdImages(imagePath);
	            System.out.println("新圖片儲存成功: " + imagePath);
	        } else {
	            // 保留原有圖片
	            product.setProdImages(existing.getProdImages());
	            System.out.println("保留原有圖片: " + existing.getProdImages());
	        }

	        productService.updateProduct(prodNum, product);
	        System.out.println("商品更新成功！");
	        return "redirect:/products";
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("圖片上傳失敗: " + e.getMessage());
	        return "redirect:/products/edit/" + prodNum + "?error=upload_failed";
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("更新商品失敗: " + e.getMessage());
	        return "redirect:/products/edit/" + prodNum + "?error=update_failed";
	    }
	}

	// 刪除商品
	@GetMapping("/delete/{prodNum}")
	public String deleteProduct(@PathVariable("prodNum") Long prodNum) {
		// 取得商品資料並刪除圖片
		Product product = productService.getProductById(prodNum);
		if (product != null && product.getProdImages() != null) {
			fileUploadService.deleteFile(product.getProdImages());
		}

		productService.deleteProduct(prodNum);
		return "redirect:/products";
	}

	// 根據條碼查找商品
	@GetMapping("/barcode/{barcode}")
	public String getProductByBarcode(@PathVariable("barcode") String barcode, Model model) {
		Product product = productService.findByBarcode(barcode);
		model.addAttribute("product", product);
		return "/";
	}

	// 關鍵字模糊查詢
	@GetMapping("/search")
	public String searchByKeyword(@RequestParam("keyword") String keyword, Model model) {
		List<Product> products = productService.findByKeyword(keyword);
		model.addAttribute("products", products);
		return "products";
	}

	@GetMapping("/details/{id}")
	public String getProductDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductByIdWithCategory(id);
		model.addAttribute("product", product);

		if (model.containsAttribute("customer")) {
			model.addAttribute("successMessage", "商品已成功加入購物車！");
		}
		return "products_details";
	}
}