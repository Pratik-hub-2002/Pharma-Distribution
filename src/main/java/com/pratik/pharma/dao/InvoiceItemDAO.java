package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.InvoiceItem;
import com.pratik.pharma.util.DBConnection;

public class InvoiceItemDAO {

	// =========================================================
	// GET INVOICE ITEMS BY ORDER ID
	// =========================================================

	public List<InvoiceItem> getInvoiceItemsByOrderId(int orderId) {

		List<InvoiceItem> items = new ArrayList<>();

		String sql = """
				SELECT
				    oi.order_item_id,

				    p.product_id,
				    p.product_name,
				    p.product_code,
				    p.hsn_code,
				    p.gst_rate,

				    oib.batch_id,
				    b.batch_number,
				    b.manufacturing_date,
				    b.expiry_date,

				    oi.ordered_quantity,

				    COALESCE(
				        SUM(oib.free_quantity),
				        0
				    ) AS free_quantity,

				    (
				        oi.ordered_quantity
				        +
				        COALESCE(SUM(oib.free_quantity), 0)
				    ) AS total_quantity,

				    oi.unit_price,
				    oi.discount_amount,
				    oi.tax_amount,
				    oi.line_total

				FROM order_items oi

				INNER JOIN products p
				    ON oi.product_id = p.product_id

				INNER JOIN order_item_batches oib
				    ON oi.order_item_id = oib.order_item_id

				INNER JOIN batches b
				    ON oib.batch_id = b.batch_id

				WHERE oi.order_id = ?

				GROUP BY
				    oi.order_item_id,
				    p.product_id,
				    p.product_name,
				    p.product_code,
				    p.hsn_code,
				    p.gst_rate,

				    oib.batch_id,
				    b.batch_number,
				    b.manufacturing_date,
				    b.expiry_date,

				    oi.ordered_quantity,
				    oi.unit_price,
				    oi.discount_amount,
				    oi.tax_amount,
				    oi.line_total

				ORDER BY
				    oi.order_item_id,
				    b.expiry_date
				""";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, orderId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					InvoiceItem item = mapInvoiceItem(resultSet);

					items.add(item);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return items;
	}

	// =========================================================
	// MAP RESULT SET
	// =========================================================

	private InvoiceItem mapInvoiceItem(ResultSet resultSet) throws Exception {

		InvoiceItem item = new InvoiceItem();

		// ORDER ITEM
		item.setOrderItemId(resultSet.getInt("order_item_id"));

		// PRODUCT
		item.setProductId(resultSet.getInt("product_id"));

		item.setProductName(resultSet.getString("product_name"));

		item.setProductCode(resultSet.getString("product_code"));

		item.setHsnCode(resultSet.getString("hsn_code"));

		item.setGstRate(resultSet.getBigDecimal("gst_rate"));

		// BATCH
		item.setBatchId(resultSet.getInt("batch_id"));

		item.setBatchNumber(resultSet.getString("batch_number"));

		item.setManufacturingDate(resultSet.getDate("manufacturing_date"));

		item.setExpiryDate(resultSet.getDate("expiry_date"));

		// QUANTITY
		item.setOrderedQuantity(resultSet.getInt("ordered_quantity"));

		item.setFreeQuantity(resultSet.getInt("free_quantity"));

		item.setTotalQuantity(resultSet.getInt("total_quantity"));

		// AMOUNT
		item.setUnitPrice(resultSet.getBigDecimal("unit_price"));

		item.setDiscountAmount(resultSet.getBigDecimal("discount_amount"));

		item.setTaxAmount(resultSet.getBigDecimal("tax_amount"));

		item.setLineTotal(resultSet.getBigDecimal("line_total"));

		return item;
	}
}