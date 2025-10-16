/**
 * Product Comparison Helper
 * Utility functions cho chức năng so sánh sản phẩm
 */

const ProductComparison = {

    /**
     * So sánh 2 sản phẩm và chuyển hướng đến trang so sánh
     * @param {string|number} product1 - ID hoặc Slug của sản phẩm 1
     * @param {string|number} product2 - ID hoặc Slug của sản phẩm 2
     */
    compareProducts: function(product1, product2) {
        // Validate inputs
        if (!product1 || !product2) {
            alert('Vui lòng chọn 2 sản phẩm để so sánh');
            return;
        }

        if (product1 === product2) {
            alert('Không thể so sánh cùng một sản phẩm');
            return;
        }

        // Chuyển hướng đến trang so sánh (slug hoặc ID đều được)
        window.location.href = `/product-compare?product1=${product1}&product2=${product2}`;
    },

    /**
     * Initialize compare list UI khi load trang
     */
    init: function() {
        // Attach event listeners nếu có compare button
        const compareButton = document.getElementById('compareButton');
        if (compareButton) {
            compareButton.addEventListener('click', () => {
                const compareList = JSON.parse(localStorage.getItem('compareList') || '[]');
                if (compareList.length !== 2) {
                    alert('Vui lòng chọn đúng 2 sản phẩm để so sánh');
                    return;
                }

                const product1Id = compareList[0].productId;
                const product2Id = compareList[1].productId;

                this.compareProducts(product1Id, product2Id);
            });
        }

        const clearButton = document.getElementById('clearCompareList');
        if (clearButton) {
            clearButton.addEventListener('click', () => {
                if (confirm('Bạn có chắc muốn xóa danh sách so sánh?')) {
                    localStorage.removeItem('compareList');
                    alert('Danh sách so sánh đã được xóa');
                }
            });
        }
    }
};

// Auto init khi DOM loaded
document.addEventListener('DOMContentLoaded', function() {
    ProductComparison.init();
});
