package com.pratik.pharma.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.pratik.pharma.dao.CategoryDAO;
import com.pratik.pharma.dao.ManufacturerDAO;
import com.pratik.pharma.dao.ProductDAO;
import com.pratik.pharma.model.Category;
import com.pratik.pharma.model.Manufacturer;
import com.pratik.pharma.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProductDAO productDAO;
	private CategoryDAO categoryDAO;
	private ManufacturerDAO manufacturerDAO;

	@Override
	public void init() {

		productDAO = new ProductDAO();
		categoryDAO = new CategoryDAO();
		manufacturerDAO = new ManufacturerDAO();
	}

	// =========================
	// GET
	// =========================

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// EDIT PRODUCT
		// =========================

		if ("edit".equalsIgnoreCase(action)) {

			String productIdParam = request.getParameter("productId");

			try {

				int productId = Integer.parseInt(productIdParam);

				Product product = productDAO.getProductById(productId);

				if (product == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
					return;
				}

				List<Category> categories = categoryDAO.getAllCategories();

				List<Manufacturer> manufacturers = manufacturerDAO.getAllManufacturers();

				request.setAttribute("product", product);
				request.setAttribute("categories", categories);
				request.setAttribute("manufacturers", manufacturers);

				request.getRequestDispatcher("/product-form.jsp").forward(request, response);

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product ID");
			}

			return;
		}

		// =========================
		// ACTIVATE / DEACTIVATE
		// =========================

		if ("status".equalsIgnoreCase(action)) {

			String productIdParam = request.getParameter("productId");

			try {

				int productId = Integer.parseInt(productIdParam);

				Product product = productDAO.getProductById(productId);

				if (product != null) {

					String newStatus;

					if ("ACTIVE".equalsIgnoreCase(product.getStatus())) {

						newStatus = "INACTIVE";

					} else {

						newStatus = "ACTIVE";
					}

					productDAO.updateProductStatus(productId, newStatus);
				}

				response.sendRedirect(request.getContextPath() + "/products");

			} catch (NumberFormatException e) {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product ID");
			}

			return;
		}

		// =========================
		// ADD PRODUCT FORM
		// =========================

		if ("add".equalsIgnoreCase(action)) {

			List<Category> categories = categoryDAO.getAllCategories();

			List<Manufacturer> manufacturers = manufacturerDAO.getAllManufacturers();

			request.setAttribute("categories", categories);

			request.setAttribute("manufacturers", manufacturers);

			request.getRequestDispatcher("/product-form.jsp").forward(request, response);

			return;
		}

		// =========================
		// DEFAULT → LIST PRODUCTS
		// =========================

		List<Product> products = productDAO.getAllProducts();

		request.setAttribute("products", products);

		request.getRequestDispatcher("/products.jsp").forward(request, response);
	}

	// =========================
	// POST
	// =========================

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// UPDATE PRODUCT
		// =========================

		if ("update".equalsIgnoreCase(action)) {

			try {

				Product product = new Product();

				product.setProductId(Integer.parseInt(request.getParameter("productId")));

				product.setProductCode(request.getParameter("productCode"));

				product.setProductName(request.getParameter("productName"));

				product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

				product.setManufacturerId(Integer.parseInt(request.getParameter("manufacturerId")));

				product.setDescription(request.getParameter("description"));

				product.setDosageForm(request.getParameter("dosageForm"));

				product.setStrength(request.getParameter("strength"));

				product.setUnit(request.getParameter("unit"));

				product.setHsnCode(request.getParameter("hsnCode"));

				product.setGstRate(new BigDecimal(request.getParameter("gstRate")));

				product.setMrp(new BigDecimal(request.getParameter("mrp")));

				product.setSellingPrice(new BigDecimal(request.getParameter("sellingPrice")));

				product.setColdChainRequired("true".equalsIgnoreCase(request.getParameter("coldChainRequired")));

				product.setStatus(request.getParameter("status"));

				productDAO.updateProduct(product);

				response.sendRedirect(request.getContextPath() + "/products");

			} catch (Exception e) {

				e.printStackTrace();

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product data");
			}

			return;
		}

		// =========================
		// ADD PRODUCT
		// =========================

		try {

			Product product = new Product();

			product.setProductCode(request.getParameter("productCode"));

			product.setProductName(request.getParameter("productName"));

			product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

			product.setManufacturerId(Integer.parseInt(request.getParameter("manufacturerId")));

			product.setDescription(request.getParameter("description"));

			product.setDosageForm(request.getParameter("dosageForm"));

			product.setStrength(request.getParameter("strength"));

			product.setUnit(request.getParameter("unit"));

			product.setHsnCode(request.getParameter("hsnCode"));

			product.setGstRate(new BigDecimal(request.getParameter("gstRate")));

			product.setMrp(new BigDecimal(request.getParameter("mrp")));

			product.setSellingPrice(new BigDecimal(request.getParameter("sellingPrice")));

			product.setColdChainRequired("true".equalsIgnoreCase(request.getParameter("coldChainRequired")));

			product.setStatus("ACTIVE");

			productDAO.addProduct(product);

			response.sendRedirect(request.getContextPath() + "/products");

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product data");
		}
	}
}