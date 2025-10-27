package com.uteshop.dao.impl.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.entity.branch.BranchInventory;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.VariantOptions;

import jakarta.persistence.EntityManager;

public class BranchesDaoImpl extends AbstractDao<Branches> {

	public BranchesDaoImpl() {
		super(Branches.class);
	}

	public Long countInventory(int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			Long count = enma
					.createQuery("SELECT SUM(bi.BranchStock) FROM BranchInventory bi WHERE bi.branch.Id = :branchId",
							Long.class)
					.setParameter("branchId", branchId).getSingleResult();
			return count != null ? count : 0;
		} finally {
			enma.close();
		}
	}

	public boolean existsInInventory(int branchId) {
		return this.countInventory(branchId) > 0;
	}

	public List<ProductVariants> findVariantByBranchId(Integer branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT bi.variant v FROM BranchInventory bi LEFT JOIN FETCH v.options vo LEFT JOIN FETCH vo.optionType LEFT JOIN FETCH vo.optionValue WHERE bi.branch.id = :branchId";
			return enma.createQuery(jpql, ProductVariants.class).setParameter("branchId", branchId).getResultList();
		} finally {
			enma.close();
		}
	}

	public List<BranchInventory> findOrCreateInventoriesByBranchId(Integer branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			//Lấy danh sách tồn kho hiện có (load đầy đủ các quan hệ)
			List<BranchInventory> existing = enma.createQuery("""
					    SELECT DISTINCT bi
					    FROM BranchInventory bi
					    JOIN FETCH bi.variant v
					    LEFT JOIN FETCH v.options o
					    LEFT JOIN FETCH o.optionType
					    LEFT JOIN FETCH o.optionValue
					    WHERE bi.branch.id = :branchId
					""", BranchInventory.class).setParameter("branchId", branchId).getResultList();

			//Ép Hibernate load đầy đủ dữ liệu trước khi đóng enma
			for (BranchInventory bi : existing) {
				ProductVariants v = bi.getVariant();
				if (v != null && v.getOptions() != null) {
					v.getOptions().size();
					for (VariantOptions vo : v.getOptions()) {
						if (vo.getOptionType() != null)
							vo.getOptionType().getCode();
						if (vo.getOptionValue() != null)
							vo.getOptionValue().getValue();
					}
				}
			}

			//Lấy toàn bộ danh sách biến thể sản phẩm
			List<ProductVariants> variants = enma.createQuery("""
					    SELECT DISTINCT v
					    FROM ProductVariants v
					    LEFT JOIN FETCH v.options o
					    LEFT JOIN FETCH o.optionType
					    LEFT JOIN FETCH o.optionValue
					""", ProductVariants.class).getResultList();

			//Tạo map chứa variantId đã có tồn kho
			Set<Integer> existingVariantIds = existing.stream().map(bi -> bi.getVariant().getId())
					.collect(Collectors.toSet());

			//Tạo mới tồn kho cho các variant chưa có
			List<BranchInventory> newList = new ArrayList<>(existing);
			Branches branchRef = enma.getReference(Branches.class, branchId);

			for (ProductVariants v : variants) {
				if (!existingVariantIds.contains(v.getId())) {
					BranchInventory bi = BranchInventory.builder().branch(branchRef).variant(v).BranchStock(0)
							.build();

					//Ép load options cho biến thể
					if (v.getOptions() != null) {
						v.getOptions().size();
						for (VariantOptions vo : v.getOptions()) {
							if (vo.getOptionType() != null)
								vo.getOptionType().getCode();
							if (vo.getOptionValue() != null)
								vo.getOptionValue().getValue();
						}
					}

					newList.add(bi);
				}
			}

			return newList;

		} finally {
			enma.close();
		}
	}

	//Dùng khi thêm mới chi nhánh (branch chưa có id)
	public List<BranchInventory> createenmaptyInventoriesForAllVariants() {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			List<ProductVariants> variants = enma.createQuery(
					"SELECT pv FROM ProductVariants pv LEFT JOIN FETCH pv.options o LEFT JOIN FETCH o.optionType LEFT JOIN FETCH o.optionValue",
					ProductVariants.class).getResultList();

			List<BranchInventory> inventories = new ArrayList<>();
			for (ProductVariants v : variants) {
				BranchInventory inv = new BranchInventory();
				inv.setVariant(v);
				inv.setBranchStock(0);
				inventories.add(inv);
			}

			return inventories;
		} finally {
			enma.close();
		}
	}

	public List<BranchInventory> findInventoriesWithOptionsByBranchId(Integer branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
					    SELECT DISTINCT bi FROM BranchInventory bi
					    JOIN FETCH bi.variant v
					    LEFT JOIN FETCH v.options o
					    LEFT JOIN FETCH o.optionType
					    LEFT JOIN FETCH o.optionValue
					    WHERE bi.branch.id = :branchId
					""";

			return enma.createQuery(jpql, BranchInventory.class).setParameter("branchId", branchId).getResultList();
		} finally {
			enma.close();
		}
	}

}
