package shopping.whworker.processor;

import org.springframework.batch.item.ItemProcessor;
import shopping.whworker.domain.Product;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product product) throws Exception {
        return product;
    }
}