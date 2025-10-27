package com.uteshop.services.impl.web.order;

import com.uteshop.dao.impl.manager.CartsManagerDaoImpl;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.dao.manager.ICartsManagerDao;
import com.uteshop.entity.auth.Addresses;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.BranchInventory;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.cart.CartItems;
import com.uteshop.entity.cart.Carts;
import com.uteshop.entity.cart.Vouchers;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.order.OrderItems;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.web.order.IPlaceOrderService;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceOrderServiceImpl implements IPlaceOrderService {
    EntityDaoImpl<Users> usersEntityDao = new EntityDaoImpl<>(Users.class);
    EntityDaoImpl<Addresses> addressesEntityDao = new EntityDaoImpl<>(Addresses.class);
    EntityDaoImpl<Vouchers> vouchersEntityDao = new EntityDaoImpl<>(Vouchers.class);
    EntityDaoImpl<Branches>  branchesEntityDao = new EntityDaoImpl<>(Branches.class);
    EntityDaoImpl<BranchInventory> branchInventoryEntityDao = new EntityDaoImpl<>(BranchInventory.class);
    EntityDaoImpl<ProductVariants> productVariantsEntityDao = new EntityDaoImpl<>(ProductVariants.class);
    EntityDaoImpl<Orders> ordersEntityDao = new EntityDaoImpl<>(Orders.class);
    ICartsManagerDao cartDao = new CartsManagerDaoImpl();

    private static int nz(Integer i) { return i == null ? 0 : i; }

    @Override
    @Transactional
    public Orders placeOrder(String userEmail,
                             Integer addressId,
                             Integer branchId,
                             Map<Integer, Integer> listVarAndQty,
                             String voucherCode,
                             int paymentMethod,
                             BigDecimal totalAmount,
                             String note)
    {
        Users user = usersEntityDao.findByUnique("Email", userEmail).orElse(null);
        Addresses address = addressesEntityDao.findById(addressId);
        Branches branch = branchesEntityDao.findById(branchId);

        if (!Objects.equals(user.getId(), address.getUser().getId()))
            throw new IllegalArgumentException("Người dùng không hợp lệ");

        if (branch == null)
            throw new IllegalArgumentException("Chi nhánh không hợp lệ");

        if (paymentMethod != PaymentEnums.Method.COD &&
            paymentMethod != PaymentEnums.Method.VNPAY &&
            paymentMethod != PaymentEnums.Method.MOMO)
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");

        Carts cart = cartDao.findCartWithItems(user.getCart().getId());

        Map<Integer, CartItems> cartIdx = (cart == null ? List.<CartItems>of() : cart.getItems())
                .stream().filter(ci -> ci.getVariant() != null)
                .collect(Collectors.toMap(ci -> ci.getVariant().getId(), ci -> ci, (a, b)->a));

        // Đảm bảo tất cả variant yêu cầu có trong cart và đủ số lượng
        for (var e : listVarAndQty.entrySet()) {
            Integer variantId = e.getKey();
            int wantQty = nz(e.getValue());
            if (wantQty <= 0)
                throw new IllegalArgumentException("Số lượng cho biến thể không hợp lệ");

            CartItems ci = cartIdx.get(variantId);
            if (ci == null)
                throw new IllegalArgumentException("Tồn tại biến thể không có trong giỏ");
            if (wantQty > ci.getQuantity())
                throw new IllegalArgumentException("Số lượng yêu cầu cho không hợp lệ");
        }

        // Load variants + kiểm tra tồn kho chi nhánh
        List<Integer> variantIds = new ArrayList<>(listVarAndQty.keySet());
        List<ProductVariants> variants = new ArrayList<>();
        for (Integer variantId : variantIds) {
            ProductVariants productVariant = productVariantsEntityDao.findById(variantId);
            variants.add(productVariant);
        }

        Map<Integer, BranchInventory> binvMap = new HashMap<>();
        for (Integer vid : variantIds) {
            BranchInventory.Id id = new BranchInventory.Id(branchId, vid);
            BranchInventory bi = branchInventoryEntityDao.findById(id);

            if (bi == null)
                throw new IllegalArgumentException("Chi nhánh chưa có tồn kho cho biến thể "  + vid);

            int want = nz(listVarAndQty.get(vid));
            if (bi.getBranchStock() == null || bi.getBranchStock() < want)
                throw new IllegalArgumentException("Tồn kho tại chi nhánh không đủ cho biến thể " + vid);
            binvMap.put(vid, bi);
        }

        // Tính lại Subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ProductVariants v : variants) {
            BigDecimal price = v.getPrice();
            int qty = nz(listVarAndQty.get(v.getId()));
            subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        // Kiểm tra voucher
        Vouchers voucher = null;
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherCode != null && !voucherCode.isBlank()) {
            voucher = vouchersEntityDao.findByUnique("Code", voucherCode).orElse(null);
            if (voucher == null)
                throw new IllegalArgumentException("Voucher không tồn tại");

            if (!voucher.isValid())
                throw new IllegalArgumentException("Voucher không còn hiệu lực hoặc đã vượt số lượt dùng");

            discount = voucher.calculateDiscount(subtotal);

            if (discount.signum() < 0)
                discount = BigDecimal.ZERO;
            if (discount.compareTo(subtotal) > 0)
                discount = subtotal;
        }

        // Tính total và đối soát với client
        BigDecimal serverTotal = subtotal.subtract(discount);
        if (serverTotal.compareTo(totalAmount) != 0) {
            throw new IllegalArgumentException("Tổng tiền không khớp. Server: " + serverTotal.longValue() + " | Client: " + totalAmount);
        }

        // Tạo Orders + OrderItems
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderStatus(OrderEnums.OrderStatus.NEW);
        order.setPaymentStatus(OrderEnums.PaymentStatus.UNPAID);
        order.setSubtotal(subtotal);
        order.setDiscountAmount(discount);
        order.setShippingFee(BigDecimal.ZERO);
        order.setTotalAmount(serverTotal);
        order.setNote(note);
        order.setVoucherCode(voucher != null ? voucher.getCode() : null);

        // Gán địa chỉ giao
        order.setReceiverName(address.getFullName());
        order.setPhone(address.getPhone());
        order.setAddressLine(address.getAddressLine());
        order.setWard(address.getWard());
        order.setDistrict(address.getDistrict());
        order.setCity(address.getCity());

        // Chi nhánh
        order.setBranch(branch);

        // Order items
        for (ProductVariants v : variants) {
            int qty = nz(listVarAndQty.get(v.getId()));
            OrderItems oi = new OrderItems();
            oi.setOrder(order);
            oi.setProduct(v.getProduct());
            oi.setVariant(v);
            oi.setPrice(v.getPrice()); // snapshot giá
            oi.setQuantity(qty);
            order.getItems().add(oi);
        }

        // Tạo payments
        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setMethod(paymentMethod);
        payment.setStatus(PaymentEnums.Status.PENDING);
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setCreatedAt(LocalDateTime.now());
        order.setPayment(payment);

        ordersEntityDao.insert(order);
        return order;
    }
}
