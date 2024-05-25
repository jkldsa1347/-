package ecommerce.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ecommerce.dao.ProductDao;
import ecommerce.dao.OrderDao;
import ecommerce.model.Product;
import ecommerce.model.Order;
import ecommerce.model.OrderDetail;

public class Main {

    // 新增 escapeHtml 方法，防止 xss
	// 通過將用戶輸入中的特殊字符轉換為其對應的 HTML 實體，
	// 從而避免 HTML 注入攻擊並確保在網頁中顯示這些字符時不會出現問題。
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;").replaceAll("/", "&#x2F;");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductDao productDao = new ProductDao();
        OrderDao orderDao = new OrderDao();

        while (true) {
            System.out.println("歡迎使用電商購物系統:");
            System.out.println("1. 新增商品");
            System.out.println("2. 新增訂單");
            System.out.println("3. 顯示庫存量大於零的商品清單");
            System.out.println("4. 顯示單一訂單明細");
            System.out.println("5. 顯示所有訂單明細");
            System.out.println("6. 離開");
            System.out.println("當您中途想退出，請輸入-1");

            int choice = sc.nextInt();
            if (choice == -1) {
                System.out.println("程序結束。");
                break;
            }
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    // 新增商品
                    System.out.print("請輸入商品編號: ");
                    String productId = escapeHtml(sc.nextLine()); 
                    if (productId.equals("-1")) {
                        System.out.println("程序結束。");
                        return;
                    }
                    System.out.print("請輸入商品名稱: ");
                    String productName = escapeHtml(sc.nextLine()); 
                    if (productName.equals("-1")) {
                        System.out.println("程序結束。");
                        return;
                    }
                    System.out.print("請輸入售價: ");
                    int price = sc.nextInt();
                    if (price == -1) {
                        System.out.println("程序結束。");
                        return;
                    }
                    System.out.print("請輸入庫存: ");
                    int quantity = sc.nextInt();
                    if (quantity == -1) {
                        System.out.println("程序結束。");
                        return;
                    }
                    sc.nextLine(); 

                    Product product = new Product();
                    product.setProductID(productId);
                    product.setProductName(productName);
                    product.setPrice(price);
                    product.setQuantity(quantity);

                    productDao.addProduct(product);
                    System.out.println("商品新增成功!");
                    break;

                case 2:
                    // 新增訂單
                    System.out.print("請輸入訂單編號: ");
                    String orderId = escapeHtml(sc.nextLine()); 
                    if (orderId.equals("-1")) {
                        System.out.println("程序結束。");
                        return;
                    }
                    System.out.print("請輸入會員編號: ");
                    String memberId = escapeHtml(sc.nextLine()); 
                    if (memberId.equals("-1")) {
                        System.out.println("程序結束。");
                        return;
                    }

                    List<Product> productsInStock = productDao.getProductsInStock();
                    System.out.println("庫存量大於零的商品清單:");
                    for (Product p : productsInStock) {
                        System.out.println(p.getProductID() + " |" + escapeHtml(p.getProductName()) + " | 庫存: "
                                + p.getQuantity() + " | 價格: " + p.getPrice());
                    }

                    List<OrderDetail> orderDetails = new ArrayList<>();
                    int totalAmount = 0;

                    while (true) {
                        System.out.print("請輸入商品編號進行購買 (輸入 'done' 完成): ");
                        String pid = escapeHtml(sc.nextLine()); // 使用escapeHtml轉義輸入
                        if (pid.equals("-1")) {
                            System.out.println("程序結束。");
                            return;
                        }
                        if (pid.equalsIgnoreCase("done")) {
                            break;
                        }
                        System.out.print("請輸入購買數量: ");
                        int qty = sc.nextInt();
                        if (qty == -1) {
                            System.out.println("程序結束。");
                            return;
                        }
                        sc.nextLine(); 

                        Product selectedProduct = null;
                        for (Product p : productsInStock) {
                            if (p.getProductID().equals(pid) && p.getQuantity() >= qty) {
                                selectedProduct = p;
                                break;
                            }
                        }

                        if (selectedProduct != null) {
                            OrderDetail orderDetail = new OrderDetail();
                            orderDetail.setOrderID(orderId);
                            orderDetail.setProductID(selectedProduct.getProductID());
                            orderDetail.setQuantity(qty);
                            orderDetail.setStandPrice(selectedProduct.getPrice());
                            orderDetail.setItemPrice(selectedProduct.getPrice() * qty);

                            orderDetails.add(orderDetail);
                            totalAmount += orderDetail.getItemPrice();
                        } else {
                            System.out.println("商品不存在或庫存不足!");
                        }
                    }

                    System.out.print("請選擇付款狀態 (0: 未付款, 1: 已付款): ");
                    int payStatus = sc.nextInt();
                    if (payStatus == -1) {
                        System.out.println("程序結束。");
                        return;
                    }
                    sc.nextLine(); 

                    Order order = new Order();
                    order.setOrderID(orderId);
                    order.setMemberID(memberId);
                    order.setPrice(totalAmount);
                    order.setPayStatus(payStatus);

                    orderDao.createOrder(order, orderDetails);
                    System.out.println("訂單建立成功!");
                    break;

                case 3:
                    // 顯示庫存量大於零的商品清單
                    List<Product> inStockProducts = productDao.getProductsInStock();
                    System.out.println("庫存量大於零的商品清單:");
                    for (Product p : inStockProducts) {
                        System.out.println(p.getProductID() + " |" + escapeHtml(p.getProductName()) + " | 庫存: "
                                + p.getQuantity() + " | 價格: " + p.getPrice());
                    }
                    break;

                case 4:
                    // 顯示單一訂單明細
                    System.out.print("請輸入訂單編號: ");
                    String searchOrderId = escapeHtml(sc.nextLine()); 
                    if (searchOrderId.equals("-1")) {
                        System.out.println("程序結束。");
                        return;
                    }
                    List<OrderDetail> orderDetailsList = orderDao.getOrderDetailsByOrderId(searchOrderId);
                    System.out.println("訂單明細:");
                    for (OrderDetail od : orderDetailsList) {
                        System.out.println("訂單明細流水號: " + od.getOrderItemSN());
                        System.out.println("訂單編號: " + od.getOrderID());
                        System.out.println("商品編號: " + od.getProductID());
                        System.out.println("數量: " + od.getQuantity());
                        System.out.println("單價: " + od.getStandPrice());
                        System.out.println("單品項總價: " + od.getItemPrice());
                        System.out.println("------------------");
                    }
                    break;

                case 5:
                    // 顯示所有訂單明細
                    List<OrderDetail> allOrderDetails = orderDao.getAllOrderDetails();
                    System.out.println("所有訂單明細:");
                    for (OrderDetail od : allOrderDetails) {
                        System.out.println("訂單明細流水號: " + od.getOrderItemSN());
                        System.out.println("訂單編號: " + od.getOrderID());
                        System.out.println("商品編號: " + od.getProductID());
                        System.out.println("數量: " + od.getQuantity());
                        System.out.println("單價: " + od.getStandPrice());
                        System.out.println("單品項總價: " + od.getItemPrice());
                        System.out.println("------------------");
                    }
                    break;

                case 6:
                    // 離開
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("無效選項，請重新選擇。");
                    break;
            }
        }
    }
}
