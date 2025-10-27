package com.uteshop.services.impl.web.account;

import com.uteshop.dao.impl.web.account.AddressesDaoImpl;
import com.uteshop.dao.web.account.IAddressesDao;
import com.uteshop.entity.auth.Addresses;
import com.uteshop.services.web.account.IAddressesService;

import java.util.List;

public class AddressesServiceImpl implements IAddressesService {
    
    private final IAddressesDao addressesDao = new AddressesDaoImpl();
    
    @Override
    public List<Addresses> getUserAddresses(Integer userId) {
        if (userId == null) {
            return List.of();
        }
        return addressesDao.findByUserId(userId);
    }
    
    @Override
    public Addresses getDefaultAddress(Integer userId) {
        if (userId == null) {
            return null;
        }
        return addressesDao.findDefaultAddress(userId);
    }
    
    @Override
    public Addresses getAddressById(Integer id) {
        if (id == null) {
            return null;
        }
        return addressesDao.findById(id);
    }
    
    @Override
    public void addAddress(Addresses address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        
        // Nếu đây là địa chỉ đầu tiên, tự động set làm default
        if (addressesDao.countByUserId(address.getUser().getId()) == 0) {
            address.setIsDefault(true);
        }
        
        // Nếu set làm default, clear các default khác
        if (address.getIsDefault() != null && address.getIsDefault()) {
            addressesDao.clearDefaultAddresses(address.getUser().getId());
        }
        
        addressesDao.insert(address);
    }
    
    @Override
    public void updateAddress(Addresses address) {
        if (address == null || address.getId() == null) {
            throw new IllegalArgumentException("Address and ID cannot be null");
        }
        
        // Nếu set làm default, clear các default khác
        if (address.getIsDefault() != null && address.getIsDefault()) {
            addressesDao.clearDefaultAddresses(address.getUser().getId());
        }
        
        addressesDao.update(address);
    }
    
    @Override
    public void deleteAddress(Integer id, Integer userId) {
        if (id == null || userId == null) {
            throw new IllegalArgumentException("Address ID and User ID cannot be null");
        }
        
        // Kiểm tra xem địa chỉ có phải của user này không
        Addresses address = addressesDao.findById(id);
        if (address == null) {
            throw new IllegalArgumentException("Address not found");
        }
        
        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this address");
        }
        
        boolean wasDefault = address.getIsDefault();
        addressesDao.delete(id);
        
        // Nếu xóa địa chỉ default, set địa chỉ đầu tiên còn lại làm default
        if (wasDefault) {
            List<Addresses> remainingAddresses = addressesDao.findByUserId(userId);
            if (!remainingAddresses.isEmpty()) {
                Addresses firstAddress = remainingAddresses.get(0);
                firstAddress.setIsDefault(true);
                addressesDao.update(firstAddress);
            }
        }
    }
    
    @Override
    public void setDefaultAddress(Integer addressId, Integer userId) {
        if (addressId == null || userId == null) {
            throw new IllegalArgumentException("Address ID and User ID cannot be null");
        }
        
        Addresses address = addressesDao.findById(addressId);
        if (address == null) {
            throw new IllegalArgumentException("Address not found");
        }
        
        if (!address.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this address");
        }
        
        // Clear tất cả default addresses của user
        addressesDao.clearDefaultAddresses(userId);
        
        // Set address này làm default
        address.setIsDefault(true);
        addressesDao.update(address);
    }
    
    @Override
    public boolean isDuplicateAddress(Integer userId, String fullName, String phone, 
                                      String addressLine, String ward, String district, 
                                      String city, Integer excludeAddressId) {
        if (userId == null) {
            return false;
        }
        
        List<Addresses> userAddresses = addressesDao.findByUserId(userId);
        
        for (Addresses addr : userAddresses) {
            // Skip nếu là chính address đang edit
            if (excludeAddressId != null && addr.getId().equals(excludeAddressId)) {
                continue;
            }
            
            // Kiểm tra địa chỉ, phường, quận, thành phố có trùng không
            boolean sameAddress = isSameString(addr.getAddressLine(), addressLine);
            boolean sameWard = isSameString(addr.getWard(), ward);
            boolean sameDistrict = isSameString(addr.getDistrict(), district);
            boolean sameCity = isSameString(addr.getCity(), city);
            
            if (sameAddress && sameWard && sameDistrict && sameCity) {
                // Địa chỉ trùng, kiểm tra tên và SĐT
                boolean sameName = isSameString(addr.getFullName(), fullName);
                boolean samePhone = isSameString(addr.getPhone(), phone);
                
                // Nếu trùng cả tên VÀ SĐT thì báo duplicate
                if (sameName && samePhone) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isSameString(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.trim().equalsIgnoreCase(str2.trim());
    }
}
