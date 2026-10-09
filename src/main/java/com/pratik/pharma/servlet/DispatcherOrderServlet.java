package com.pratik.pharma.servlet;

import java.io.IOException;
import java.util.List;

import com.pratik.pharma.dao.DispatcherOrderDAO;
import com.pratik.pharma.dao.OrderItemDAO;
import com.pratik.pharma.model.Order;
import com.pratik.pharma.model.OrderItem;
import com.pratik.pharma.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dispatch-orders")
public class DispatcherOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DispatcherOrderDAO dispatcherOrderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {

        dispatcherOrderDAO = new DispatcherOrderDAO();
        orderItemDAO = new OrderItemDAO();
    }

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasDispatcherAccess(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to access dispatcher orders.");

            return;
        }

        String action = request.getParameter("action");

        // -----------------------------------------------------
        // VIEW ORDER
        // -----------------------------------------------------

        if ("view".equalsIgnoreCase(action)) {

            showOrderView(request, response);

            return;
        }

        // -----------------------------------------------------
        // DEFAULT - LIST ORDERS
        // -----------------------------------------------------

        showOrderList(request, response);
    }

    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!hasDispatcherAccess(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to perform dispatcher actions.");

            return;
        }

        String action = request.getParameter("action");

        String orderIdParameter =
                request.getParameter("orderId");

        if (orderIdParameter == null ||
                orderIdParameter.trim().isEmpty()) {

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        int orderId;

        try {

            orderId = Integer.parseInt(orderIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        int userId = getLoggedInUserId(request);

        // -----------------------------------------------------
        // APPROVE
        // -----------------------------------------------------

        if ("approve".equalsIgnoreCase(action)) {

            boolean success =
                    dispatcherOrderDAO.approveOrder(
                            orderId,
                            userId);

            if (success) {

                request.getSession().setAttribute(
                        "successMessage",
                        "Order #" + orderId +
                        " has been approved successfully.");

            } else {

                request.getSession().setAttribute(
                        "errorMessage",
                        "Unable to approve Order #" +
                        orderId + ".");
            }

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        // -----------------------------------------------------
        // HOLD
        // -----------------------------------------------------

        if ("hold".equalsIgnoreCase(action)) {

            boolean success =
                    dispatcherOrderDAO.holdOrder(
                            orderId,
                            userId);

            if (success) {

                request.getSession().setAttribute(
                        "successMessage",
                        "Order #" + orderId +
                        " has been placed on hold.");

            } else {

                request.getSession().setAttribute(
                        "errorMessage",
                        "Unable to put Order #" +
                        orderId + " on hold.");
            }

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        // -----------------------------------------------------
        // RELEASE
        // -----------------------------------------------------

        if ("release".equalsIgnoreCase(action)) {

            boolean success =
                    dispatcherOrderDAO.releaseOrder(
                            orderId);

            if (success) {

                request.getSession().setAttribute(
                        "successMessage",
                        "Order #" + orderId +
                        " has been released from hold.");

            } else {

                request.getSession().setAttribute(
                        "errorMessage",
                        "Unable to release Order #" +
                        orderId + ".");
            }

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        response.sendRedirect(
                "dispatch-orders");
    }

    // =========================================================
    // SHOW ORDER LIST
    // =========================================================

    private void showOrderList(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> pendingOrders =
                dispatcherOrderDAO.getPendingOrders();

        List<Order> onHoldOrders =
                dispatcherOrderDAO.getOnHoldOrders();

        List<Order> approvedOrders =
                dispatcherOrderDAO.getApprovedOrders();

        List<Order> allOrders =
                dispatcherOrderDAO.getAllDispatcherOrders();

        request.setAttribute(
                "pendingOrders",
                pendingOrders);

        request.setAttribute(
                "onHoldOrders",
                onHoldOrders);

        request.setAttribute(
                "approvedOrders",
                approvedOrders);

        request.setAttribute(
                "allOrders",
                allOrders);

        // -----------------------------------------------------
        // SUCCESS / ERROR MESSAGE
        // -----------------------------------------------------

        HttpSession session =
                request.getSession(false);

        if (session != null) {

            String successMessage =
                    (String) session.getAttribute(
                            "successMessage");

            String errorMessage =
                    (String) session.getAttribute(
                            "errorMessage");

            if (successMessage != null) {

                request.setAttribute(
                        "successMessage",
                        successMessage);

                session.removeAttribute(
                        "successMessage");
            }

            if (errorMessage != null) {

                request.setAttribute(
                        "errorMessage",
                        errorMessage);

                session.removeAttribute(
                        "errorMessage");
            }
        }

        request.getRequestDispatcher(
                "/dispatcher-orders.jsp")
                .forward(request, response);
    }

    // =========================================================
    // SHOW ORDER VIEW
    // =========================================================

    private void showOrderView(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdParameter =
                request.getParameter("id");

        if (orderIdParameter == null ||
                orderIdParameter.trim().isEmpty()) {

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        int orderId;

        try {

            orderId =
                    Integer.parseInt(orderIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "dispatch-orders");

            return;
        }

        Order order =
                dispatcherOrderDAO.getOrderForReview(
                        orderId);

        if (order == null) {

            request.setAttribute(
                    "error",
                    "Order #" + orderId +
                    " was not found.");

            request.getRequestDispatcher(
                    "/dispatcher-order-view.jsp")
                    .forward(request, response);

            return;
        }

        List<OrderItem> items =
                orderItemDAO.getItemsByOrderId(orderId);

        request.setAttribute(
                "order",
                order);

        request.setAttribute(
                "items",
                items);

        request.getRequestDispatcher(
                "/dispatcher-order-view.jsp")
                .forward(request, response);
    }

    // =========================================================
    // CHECK DISPATCHER ACCESS
    // =========================================================

    private boolean hasDispatcherAccess(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            return false;
        }

        Object loggedInUser =
                session.getAttribute("loggedInUser");

        if (!(loggedInUser instanceof User)) {

            return false;
        }

        User user =
                (User) loggedInUser;

        String roleName =
                user.getRoleName();

        if (roleName == null) {

            return false;
        }

        return "SUPER_ADMIN".equalsIgnoreCase(roleName)
                || "DISPATCH_MANAGER".equalsIgnoreCase(roleName);
    }

    // =========================================================
    // GET LOGGED-IN USER ID
    // =========================================================

    private int getLoggedInUserId(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            return 0;
        }

        Object userId =
                session.getAttribute("userId");

        if (userId instanceof Integer) {

            return (Integer) userId;
        }

        if (userId != null) {

            try {

                return Integer.parseInt(
                        userId.toString());

            } catch (NumberFormatException e) {

                return 0;
            }
        }

        Object loggedInUser =
                session.getAttribute(
                        "loggedInUser");

        if (loggedInUser instanceof User) {

            return ((User) loggedInUser)
                    .getUserId();
        }

        return 0;
    }
}